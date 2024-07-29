package lab.repository.labEngine;

import lab.payload.answer.AnswerResponseMessage;
import oracle.jdbc.rowset.OracleCachedRowSet;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Repository
public class LabEngineImpl implements LabEngine {

    @Autowired
    private DataSource dataSource;
    private String submittedQuery;
    private String referenceQuery;
    @Override
    public void setSubmittedQuery(String submittedQuery) {
        this.submittedQuery = submittedQuery;
    }
    @Override
    public void setReferenceQuery(String referenceQuery) {
        this.referenceQuery = referenceQuery;
    }

    private final List<String> restrictedKeywords = Arrays.asList("TRUNCATE", "DROP", "ALTER", "CREATE");

    private final Map<String, String> associateKeywords = new HashMap<String, String>() {{
        put("INSERT", "INTO");
        put("UPDATE", "UPDATE");
        put("DELETE", "FROM");
    }};


    @Override
    public AnswerResponseMessage checkAns() {
        if (isRestrictedQuery(submittedQuery))
            return getInvalidAnswerResponseMessage(new Exception("Restricted"));

        String associatedKeyword = getAssociatedKeywordForSelectQuery(submittedQuery);
        String questionAnswerKeyword = getAssociatedKeywordForSelectQuery(referenceQuery);

        if (associatedKeyword != null && questionAnswerKeyword != null) {
            String submittedTableName = getTableName(submittedQuery, associatedKeyword);
            String referenceTableName = getTableName(referenceQuery, questionAnswerKeyword);
            return submittedTableName!=null && !submittedTableName.equals(referenceTableName)?
                    getInvalidAnswerResponseMessage(new Exception("Wrong table")) : executeQuery(submittedTableName, referenceTableName);
        } else return (associatedKeyword != null || questionAnswerKeyword != null) ?
                getInvalidAnswerResponseMessage(new Exception("Invalid Attempt")) : executeSelectQuery();
    }

    /*   getAnswer*/
    private AnswerResponseMessage getAnswerResponseMessage(ResultSet resultSet0, ResultSet resultSet1, ResultSet resultSet2) {
        try {
            AnswerResponseMessage answerResponseMessage = new AnswerResponseMessage();
            List outputList = answerResponseMessage.getOutput();
            ResultSetMetaData metaData1 = resultSet0.getMetaData();
            ResultSetMetaData metaData2 = resultSet1.getMetaData();
            int columnCount1 = metaData1.getColumnCount();
            int columnCount2 = metaData2.getColumnCount();

            List listOfColumnName = new ArrayList();
            for (int i = 1; i <= columnCount1; i++) {
                listOfColumnName.add(metaData1.getColumnName(i));
            }
            if(listOfColumnName.isEmpty())
                return getInvalidAnswerResponseMessage(new Exception("Please recheck your query carefully! "));
            else
                outputList.add(listOfColumnName);

            while (resultSet0.next()) {
                List<String> temp = new ArrayList<>();
                for (int i = 1; i <= columnCount1; i++) {
                    Object temp1 = resultSet0.getObject(i);
                    String val1 = (temp1 != null) ? resultSet0.getObject(i).toString() : null;
                    temp.add(val1);
                }
                outputList.add(temp);
            }

            boolean matched = matchResults(resultSet1, resultSet2, columnCount1, columnCount2);
            if(matched)
                answerResponseMessage.setCorrect(true);
            else
                answerResponseMessage.setIncorrect(true);
            return answerResponseMessage;

        }catch (SQLException | EmptyDataException e){
            LogManager.getLogger().error(e);
            return getInvalidAnswerResponseMessage(e);
        }
    }
    /*                  -------------------------                  */


    /*   For matching Result Sets   */
    private boolean matchResults(ResultSet resultSet1, ResultSet resultSet2, int columnCount1, int columnCount2) throws SQLException, EmptyDataException {
        if (columnCount1 != columnCount2)
            return false;

        boolean emptyData = true;
        boolean rs1next = resultSet1.next();
        boolean rs2next = resultSet2.next();

        while (rs1next && rs2next) {
            emptyData = false;
            for (int i = 1; i <= columnCount1; i++) {
                Object val1 = resultSet1.getObject(i);
                Object val2 = resultSet2.getObject(i);
                if ((val1 == null && val2 != null) || (val1 != null && !val1.equals(val2))) {
                    return false;
                }
            }
            rs1next = resultSet1.next();
            rs2next = resultSet2.next();
        }

        if (rs1next || rs2next)
            return false;
        if(emptyData)
            throw new EmptyDataException("Please leave this question. It will be normalized");
        return true;
    }
    /*                  -------------------------                  */


    /*                  For executing Select query                  */
    private AnswerResponseMessage executeSelectQuery() {
        try {
            Connection connection = dataSource.getConnection();
            ResultSet resultSet1 = connection.prepareStatement(submittedQuery).executeQuery();
            ResultSet resultSet2 = connection.prepareStatement(referenceQuery).executeQuery();
            ResultSet resultSet3 = connection.prepareStatement(submittedQuery).executeQuery();
            return getAnswerResponseMessage(resultSet1, resultSet2, resultSet3);
        } catch (SQLException e) {
            return getInvalidAnswerResponseMessage(e);
        }
    }
    /*                  -------------------------                  */


    /*                  For executing I U D query                  */
    private AnswerResponseMessage executeQuery(String submitTableName, String referenceTableName) {
        AnswerResponseMessage answerResponseMessage = new AnswerResponseMessage();
        try{
            AtomicInteger rowsEffected = new AtomicInteger(-11);
            AtomicInteger expectingRowsEffected = new AtomicInteger(-12);
            ResultSet resultSet1 = getAllTableRecords(rowsEffected, submittedQuery, submitTableName);
            ResultSet resultSet2 = getAllTableRecords(expectingRowsEffected, referenceQuery, referenceTableName);
            OracleCachedRowSet resultSet3 = new OracleCachedRowSet();
            resultSet3.populate(resultSet1);
            resultSet1.beforeFirst();
            if(rowsEffected.get()!=expectingRowsEffected.get())
                answerResponseMessage.setIncorrect(true);
            else
                answerResponseMessage = getAnswerResponseMessage(resultSet1, resultSet2, resultSet3);
            answerResponseMessage.getOutput().clear();
            answerResponseMessage.getOutput().add("rows effected: "+rowsEffected);
        } catch (SQLException e){
            return getInvalidAnswerResponseMessage(e);
        }

        return answerResponseMessage;
    }

    private ResultSet getAllTableRecords(AtomicInteger rowsEffected, String query, String tableName) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        //create select query with orderBy clause to get resultSet after query executes
        String selectQuery = "Select * from " + tableName + orderByClauseOnPrimaryKey(connection, tableName);

        //prepare the statement for select query
        PreparedStatement selectQueryStatement = connection.prepareStatement(selectQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        //execute the query to get the effected rows
        rowsEffected.set(connection.prepareStatement(query).executeUpdate());

        //get the resultSet and copy it another object so that after rollback we can still return that
        OracleCachedRowSet resultSet = new OracleCachedRowSet();
        resultSet.populate(selectQueryStatement.executeQuery());

        //rollback
        connection.rollback();
        connection.setAutoCommit(true);
        //return the result set
        return resultSet;
    }

    /*                  -------------------------                  */


    /*                  Lab Engine Utils                  */

    boolean isRestrictedQuery(String query) {
        String thisQuery = query.trim().toUpperCase();
        for (String keyWord : restrictedKeywords) {
            if (thisQuery.contains(keyWord))
                return true;
        }
        return false;
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

    private String orderByClauseOnPrimaryKey(Connection connection, String tableName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(null, null, tableName);
            return primaryKeyResultSet.next() ? " ORDER BY " + primaryKeyResultSet.getString("COLUMN_NAME") : "";
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    private AnswerResponseMessage getInvalidAnswerResponseMessage(Exception e) {
        AnswerResponseMessage answerResponseMessage = new AnswerResponseMessage();
        String errorMessage = e.getMessage();
        int startIndex = errorMessage.indexOf(":") + 1;
        String message = errorMessage.substring(startIndex).trim();
        answerResponseMessage.setIsInvalidSyntax(message);
        return answerResponseMessage;
    }
    /*                  -------------------------                  */
}
