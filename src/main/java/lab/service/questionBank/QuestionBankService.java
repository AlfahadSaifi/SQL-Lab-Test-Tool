package lab.service.questionBank;
// author - Sarthak & Ashish

import lab.dto.questionBank.QuestionDto;

import java.util.List;

public interface QuestionBankService {
    List<QuestionDto> fetch();

    QuestionDto fetchById(int id);

    boolean insertQuestion(QuestionDto questionDto);

    boolean editQuestion(QuestionDto questionDto);
    boolean deleteQuestion(int id);

    boolean changeDeleteStatus(int id);

}
