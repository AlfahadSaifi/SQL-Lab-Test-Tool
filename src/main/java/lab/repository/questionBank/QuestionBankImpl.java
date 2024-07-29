package lab.repository.questionBank;


import lab.convert.Convert;
import lab.dto.questionBank.QuestionDto;
import lab.entity.lab.Lab;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import lab.entity.questionBank.Question;

import java.util.List;

@Repository
public class QuestionBankImpl implements QuestionBank{


    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<QuestionDto> fetch() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Question where deleteStatus='FALSE'";

        List<Question> list= session.createQuery(hql, Question.class).list();
        List<QuestionDto> dtoList = Convert.getModelMapper().toDtoList(list, QuestionDto.class);
        System.out.println("Question list = "+dtoList);
        return dtoList;
    }

    @Override
    public QuestionDto fetchById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Question question = session.get(Question.class,id);
        QuestionDto questionDto = Convert.getModelMapper().toDto(question, QuestionDto.class);
        return questionDto;
    }

    @Override
    public List<QuestionDto> getQuestionBankByLabId(int labId) {
        Session session = sessionFactory.getCurrentSession();
        Lab lab = session.get(Lab.class,labId);
        List<Question> questions = lab.getQuestions();
        List<QuestionDto> questionDtos = Convert.getModelMapper().toDtoList(questions, QuestionDto.class);
        return questionDtos;
    }

    @Override
    public boolean insertQuestion(QuestionDto questionDto) {
        Session session = sessionFactory.getCurrentSession();
        Question question = Convert.getModelMapper().toEntity(questionDto, Question.class);
        System.out.println("Question inside DAO-->"+question);
        try {
            session.save(question);
            return true;
        }
        catch (Exception e)
        {
            e.getMessage();
            return false;
        }
    }


    @Override
    public boolean editQuestion(QuestionDto questionDto) {
        Session session = sessionFactory.getCurrentSession();
        int questionId = questionDto.getQuestionId();
        String hql = "UPDATE Question SET questionDescription = :questionDescription, questionAnswer = :questionAnswer, questionPoints = :questionPoints WHERE questionId = :questionId";
        Query<?> query = session.createQuery(hql);
        query.setParameter("questionDescription", questionDto.getQuestionDescription());
        query.setParameter("questionAnswer", questionDto.getQuestionAnswer());
        query.setParameter("questionPoints", questionDto.getQuestionPoints());
        query.setParameter("questionId", questionId);
        int updatedEntities = query.executeUpdate();
        return updatedEntities>0;
    }

    @Override
    public boolean deleteQuestion(int id) {
        Session session = sessionFactory.getCurrentSession();
        Question question = session.get(Question.class, id);
        if (question != null) {
            session.delete(question);
            return true;
        }
        return false;
    }

    @Override
    public boolean changeDeleteStatus(int id){
        Session session = sessionFactory.getCurrentSession();
        String hql = "UPDATE Question SET deleteStatus = 'TRUE' where questionId = :id";
        Query<?> query = session.createQuery(hql);

        int updatedEntities = query.executeUpdate();

        return updatedEntities>0;
    }


}
