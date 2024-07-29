package lab.repository.trainee;
import lab.convert.Convert;
import lab.entity.trainee.Trainee;
import lab.payload.report.TraineePayload;
import lab.payload.report.TraineeReportPayload;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import lab.dto.trainee.TraineeDto;

import java.util.List;

@Repository
public class TraineeRepoImpl implements TraineeRepo {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public TraineeDto getTraineeByUserName(String employeeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Trainee where employeeId =:employeeId";
        Query<Trainee> query = session.createQuery(hql, Trainee.class);
        query.setParameter("employeeId",employeeId);
        Trainee trainee = query.uniqueResult();
        TraineeDto traineeDto = Convert.getModelMapper().toDto(trainee, TraineeDto.class);
        session.clear();
        return traineeDto;
    }

    @Override
    public TraineeDto getTraineeByEmployeeId(String employeeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Trainee where employeeId =:employeeId";
        Query<Trainee> query = session.createQuery(hql, Trainee.class);
        query.setParameter("employeeId",employeeId);
        Trainee trainee = query.uniqueResult();
        TraineeDto traineeDto = Convert.getModelMapper().toDto(trainee, TraineeDto.class);
        session.clear();
        return traineeDto;
    }
    @Override
    public void saveTrainee(TraineeDto traineeDto) {
        Trainee trainee = Convert.getModelMapper().toEntity(traineeDto,Trainee.class);
        sessionFactory.getCurrentSession().save(trainee);
    }

    @Override
    public double getTraineeLabTotalMarks(int labId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select sum(q.questionPoints) " +
                "from Lab l join l.questions q " +
                "where l.labId = :labId";
        Query query = session.createQuery(hql);
        query.setParameter("labId", labId);
        Long totalQuestionPoints = (Long) query.uniqueResult();
        if (totalQuestionPoints != null) {
            return totalQuestionPoints.doubleValue();
        } else {
            return 0.0;
        }
    }

    @Override
    public List<String> getTraineeIdByBatchId(int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT t.employeeId FROM Trainee t WHERE t.batchId = :batchId";
        Query<String> query = session.createQuery(hql, String.class);
        query.setParameter("batchId", batchId);
        return query.getResultList();
    }

    @Override
    public int getBatchIdByUserName(String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select t.batchId from Trainee t where t.employeeId = :traineeId";
        Integer batchId = (Integer) session.createQuery(hql)
                .setParameter("traineeId", traineeId)
                .uniqueResult();
        return (batchId != null) ? batchId : 0;
    }

    @Override
    public double getTraineeLabTestTotalPoints(int labTestId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select sum(q.questionPoints) " +
                "from LabTest lt join lt.questions q " +
                "where lt.labTestId = :labTestId";
        Query query = session.createQuery(hql);
        query.setParameter("labTestId", labTestId);
        Long totalQuestionPoints = (Long) query.uniqueResult();
        if (totalQuestionPoints != null) {
            return totalQuestionPoints.doubleValue();
        } else {
            return 0.0;
        }
    }

    public List<TraineeReportPayload> getTraineeReportByBatchId(int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT NEW lab.payload.report.TraineeReportPayload(t.employeeId, t.name) FROM Trainee t WHERE t.batchId = :batchId";

        Query<TraineeReportPayload> query = session.createQuery(hql, TraineeReportPayload.class);
        query.setParameter("batchId", batchId);

        List<TraineeReportPayload> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public List<TraineePayload> getAllTrainee() {
        Session session = sessionFactory.getCurrentSession();

        String hql="select New lab.payload.report.TraineePayload(t.employeeId, t.name,b.batchCode,b.id) from Trainee t " +
                "join Batch b on t.batchId=b.id";

        Query<TraineePayload> query = session.createQuery(hql, TraineePayload.class);

        List<TraineePayload> resultList = query.getResultList();

        return resultList;
    }

    @Override
    public void changePassword(String newPassword, String employeeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "UPDATE User SET password = :newPassword WHERE employeeId = :empId";
        int rowsUpdated = session.createQuery(hql)
                .setParameter("newPassword", newPassword)
                .setParameter("empId", employeeId)
                .executeUpdate();

    }
}
