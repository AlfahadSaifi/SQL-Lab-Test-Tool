package lab.service.labEngine;

import lab.exceptions.labEngine.EmptyDataException;
import lab.exceptions.labEngine.ForcedRollBackWithResult;
import lab.payload.answer.AnswerResponseMessage;
import lab.repository.labEngineRepo.LabEngineRepo;
import lab.repository.questionBank.QuestionBank;
import lab.service.formatter.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class LabEngineServiceImpl implements LabEngineService {

    @Autowired
    private LabEngineRepo labEngineRepo;

    @Autowired
    private QuestionBank questionBank;


    private final List<String> restrictedKeywords = Arrays.asList("TRUNCATE", "DROP", "ALTER", "CREATE", "MODIFY");

    private final Map<String, String> associateKeywords = new HashMap<String, String>() {{
        put("INSERT", "INTO");
        put("UPDATE", "UPDATE");
        put("DELETE", "FROM");
    }};

    private String preProcess(String submittedQuery, String referenceQuery) throws Exception {

        if(isRestrictedQuery(submittedQuery))
            throw new Exception("Restricted!");

        String associatedKeyword = getAssociatedKeywordForSelectQuery(submittedQuery);
        String questionAnswerKeyword = getAssociatedKeywordForSelectQuery(referenceQuery);

        if (associatedKeyword == null && questionAnswerKeyword == null) {
            return null;
        }
        if (associatedKeyword != null ^ questionAnswerKeyword != null && !associatedKeyword.equals(questionAnswerKeyword)) {
            throw new Exception("Invalid Attempt");
        }
        else {
            String submittedTableName = getTableName(submittedQuery, associatedKeyword);
            if(submittedTableName != null && !submittedTableName.toString().equals(getTableName(referenceQuery, questionAnswerKeyword))) {
                throw new Exception("Wrong Table");
            }
            else {
                System.out.println("Returning table name : "+submittedTableName);
                return submittedTableName;
            }
        }
    }

    @Override
    public AnswerResponseMessage submitAns(String id, String submittedQuery) {
        String referenceQuery = questionBank.fetchById(Integer.parseInt(id)).getQuestionAnswer();
        submittedQuery = Formatter.querySuffixChecker(submittedQuery);

//        AnswerResponseMessage res = new AnswerResponseMessage();

        try {
            String tableName = preProcess(submittedQuery, referenceQuery);
            if(tableName==null) {
                return executeSelectQuery(submittedQuery, referenceQuery);
            }
            else {
                return executeQuery(submittedQuery, referenceQuery, tableName);
            }
        } catch (Exception e){
            e.printStackTrace();
            return getInvalidAnswerResponseMessage(e);
        }
    }

    private int getRowCount(SqlRowSet rowSet) {
        rowSet.last();
        int lastRow = rowSet.getRow();
        rowSet.beforeFirst();
        return lastRow;
    }

    private AnswerResponseMessage executeSelectQuery(String sqlQuery1, String sqlQuery2) {
        AnswerResponseMessage answerResponseMessage = new AnswerResponseMessage();

        SqlRowSet rowSet1 = labEngineRepo.extract(sqlQuery1);
        SqlRowSet rowSet2 = labEngineRepo.extract(sqlQuery2);
        List<List<String>> displayData = answerResponseMessage.getOutput();
        labEngineRepo.populateDisplayData(displayData, rowSet1);

        int count1 = getRowCount(rowSet1);
        int count2 = getRowCount(rowSet2);

        if(count2==0 && count1==0)
            throw new EmptyDataException("No Data Found. You may leave this question!");

        if(count1!=count2) {
            answerResponseMessage.setIncorrect(true);
            return answerResponseMessage;
        }

        if(labEngineRepo.match(rowSet1, rowSet2)) {
            System.out.println("row set matched!");
            answerResponseMessage.setCorrect(true);
        }
        else {
            System.out.println("row set didn't match");
            answerResponseMessage.setIncorrect(true);
        }

        closeResultSet((ResultSetWrappingSqlRowSet) rowSet1);
        closeResultSet((ResultSetWrappingSqlRowSet) rowSet2);

        return answerResponseMessage;
    }

    private AnswerResponseMessage executeQuery(String query1, String query2, String tableName)  {
        AnswerResponseMessage answerResponseMessage = new AnswerResponseMessage();
        int count1 = -11;
        int count2 = -12;
        SqlRowSet rowSet1 = null;
        SqlRowSet rowSet2 = null;

        try {
            labEngineRepo.executeAndExtract(query1, tableName);
        }catch (ForcedRollBackWithResult result){
            rowSet1 = result.getResult();
            if(result.getRowCount()>=0)
                count1 = result.getRowCount();
        }

        List<List<String>> displayData = answerResponseMessage.getOutput();
        labEngineRepo.populateDisplayData(displayData, rowSet1);

        try {
            labEngineRepo.executeAndExtract(query2, tableName);
        }catch (ForcedRollBackWithResult result){
            rowSet2 = result.getResult();
            if(result.getRowCount()>=0)
                count2 = result.getRowCount();
        }

        if(count2==0 && count1==0)
            throw new EmptyDataException("No Data Found. You may leave this question!");

        if(count1!=count2) {
            answerResponseMessage.setIncorrect(true);
            return answerResponseMessage;
        }

        if(labEngineRepo.match(rowSet1, rowSet2))
            answerResponseMessage.setCorrect(true);
        else
            answerResponseMessage.setIncorrect(true);

        closeResultSet((ResultSetWrappingSqlRowSet) rowSet1);
        closeResultSet((ResultSetWrappingSqlRowSet) rowSet2);

        return answerResponseMessage;
    }

    private static void closeResultSet(ResultSetWrappingSqlRowSet rowSet1) {
        try {
            ResultSetWrappingSqlRowSet rowSet = rowSet1;
            rowSet.getResultSet().close();
        } catch (SQLException e) {
        }
    }

    AnswerResponseMessage getInvalidAnswerResponseMessage(Exception e) {
        AnswerResponseMessage answerResponseMessage = new AnswerResponseMessage();
        String errorMessage = e.getMessage();
        int startIndex = errorMessage.indexOf(":") + 1;
        String message = errorMessage.substring(startIndex).trim();
        answerResponseMessage.setIsInvalidSyntax(message);
        return answerResponseMessage;
    }

    String getAssociatedKeywordForSelectQuery(String query) {
        String thisQuery = query.trim().toUpperCase();
        for (String keyword : associateKeywords.keySet()) {
            if (thisQuery.startsWith(keyword))
                return associateKeywords.get(keyword);
        }
        return null;
    }

    String getTableName(String query, String keyWord) {
        String regex = "(?i)\\b" + keyWord + "\\s+([\\w.]+)\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        return matcher.find() ? matcher.group(1).toUpperCase() : null;
    }


    boolean isRestrictedQuery(String query) {
        String thisQuery = query.trim().toUpperCase();
        for (String keyWord : restrictedKeywords) {
            if (thisQuery.contains(keyWord))
                return true;
        }
        return false;
    }
}
