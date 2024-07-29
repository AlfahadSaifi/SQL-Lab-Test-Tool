package lab.service.questionBank;
// author - Sarthak & Ashish

import lab.dto.questionBank.QuestionDto;
import lab.repository.questionBank.QuestionBank;
import lab.service.formatter.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class QuestionBankServiceImpl implements QuestionBankService{

    @Autowired
    QuestionBank questionBank;
    @Override
    public List<QuestionDto> fetch() {
        return questionBank.fetch();
    }

    @Override
    public QuestionDto fetchById(int id) {
        return questionBank.fetchById(id);
    }

    @Override
    public boolean insertQuestion(QuestionDto questionDto) {
        questionDto.setQuestionAnswer(Formatter.querySuffixChecker(questionDto.getQuestionAnswer()));
        return questionBank.insertQuestion(questionDto);
    }

    @Override
    public boolean editQuestion(QuestionDto questionDto) {
        return questionBank.editQuestion(questionDto);
    }

    @Override
    public boolean deleteQuestion(int id) {
        return questionBank.deleteQuestion(id);
    }

    @Override
    public boolean changeDeleteStatus(int id){
        return questionBank.changeDeleteStatus(id);
    }

}
