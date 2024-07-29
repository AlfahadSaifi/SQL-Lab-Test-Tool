package lab.repository.questionBank;
// author - Sarthak & Ashish

import lab.dto.questionBank.QuestionDto;

import java.util.List;

public interface QuestionBank {

    List<QuestionDto> fetch();

    QuestionDto fetchById(int id);

    List<QuestionDto> getQuestionBankByLabId(int labId);

    boolean insertQuestion(QuestionDto questionDto);

    boolean editQuestion(QuestionDto questionDto);
    boolean deleteQuestion(int id);
    boolean changeDeleteStatus(int id);

}
