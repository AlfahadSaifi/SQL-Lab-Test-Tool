package lab.service.formatter;

import lab.dto.questionBank.QuestionDto;

import java.util.HashSet;
import java.util.Set;

public class Formatter {
    public static String querySuffixChecker(String query){
        query = query.trim();
        if(query.endsWith(";")){
            query = query.substring(0,query.length()-1);
        }
        return query;
    }

    public static Set<Integer> stringIdsToQuestionsIds(String questionIds) {
        Set<Integer> questionsIdsInt = new HashSet<>(); // used set to handle duplicates
        for (String questionId : questionIds.split(",")) {
            String trimmedValue = questionId.trim(); // for removal of white spaces
            if (!trimmedValue.isEmpty()) {
                try {
                    int intValue = Integer.parseInt(trimmedValue); // Parse string to integer
                    questionsIdsInt.add(intValue);
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return questionsIdsInt;

    }
}