package lab.repository.reports;


import lab.convert.Convert;
import lab.dto.evaluation.*;
import lab.dto.lab.LabDto;
import lab.entity.evaluation.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportRepoImpl implements ReportRepo {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<RecordLabAttemptDto> fetchAllReports(int reportId) {
        return null;
    }

    @Override
    public int getBatchCount(String userName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(b) FROM Admin admin JOIN admin.batches b WHERE admin.employeeId = :adminId";
        Query<Number> query = session.createQuery(hql, Number.class);
        query.setParameter("adminId", userName);
        Number result = query.uniqueResult();
        return (result != null) ? result.intValue() : 0;
    }
    @Override
    public int getTraineeCount() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(b) FROM Trainee b";
        Query<Number> query = session.createQuery(hql, Number.class);
        Number result = query.uniqueResult();
        return (result != null) ? result.intValue() : 0;
    }

    @Override
    public int getLabCount(String userName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(b) FROM Admin admin JOIN admin.labs b WHERE admin.employeeId = :adminId";
        Query<Number> query = session.createQuery(hql, Number.class);
        query.setParameter("adminId", userName);
        Number result = query.uniqueResult();
        return (result != null) ? result.intValue() : 0;
    }

    @Override
    public int getLabTestCount(String userName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(b) FROM Admin admin JOIN admin.labTests b WHERE admin.employeeId = :adminId";
        Query<Number> query = session.createQuery(hql, Number.class);
        query.setParameter("adminId", userName);
        Number result = query.uniqueResult();
        return (result != null) ? result.intValue() : 0;
    }

    @Override
    public void saveRecordAttempt(RecordLabAttemptDto recordLabAttemptDto) {
        Session session = sessionFactory.getCurrentSession();
        List<LabSubmitQueryDto> submitQuery = recordLabAttemptDto.getLabSubmitQueries();
        List<LabSubmitQuery> submitQueries = Convert.getModelMapper().toEntityList(submitQuery, LabSubmitQuery.class);
        RecordLabAttempt recordAttempt = Convert.getModelMapper().toEntity(recordLabAttemptDto, RecordLabAttempt.class);
        recordAttempt.setLabSubmitQueries(submitQueries);
        session.saveOrUpdate(recordAttempt);
    }

    @Override
    public void saveTraineeAttemptRecord(RecordLabAttemptDto attemptDto) {
        sessionFactory.getCurrentSession().saveOrUpdate(attemptDto);
    }

    @Override
    public void SubmitLabReports(LabDto labDto) {
        sessionFactory.getCurrentSession().saveOrUpdate(labDto);
    }

    @Override
    public RecordLabAttemptDto fetchRecordAttempt(String traineeId, int labId, int questionId) {
        String hql = "from RecordLabAttempt where traineeId=:traineeId and labId=:labId and questionId=:questionId";
        Session session = sessionFactory.getCurrentSession();
        Query<RecordLabAttempt> query = session.createQuery(hql, RecordLabAttempt.class);
        query.setParameter("traineeId", traineeId);
        query.setParameter("labId", labId);
        query.setParameter("questionId", questionId);
        RecordLabAttempt recordAttempt = query.uniqueResult();
        if (recordAttempt == null) {
            return new RecordLabAttemptDto();
        }
        RecordLabAttemptDto recordLabAttemptDto = Convert.getModelMapper().toDto(recordAttempt, RecordLabAttemptDto.class);
        session.evict(recordAttempt);
        return recordLabAttemptDto;
    }

    @Override
    public List<RecordLabAttemptDto> fetchRecordAttempt(String traineeId, int labId) {
        String hql = "from RecordLabAttempt where traineeId=:traineeId and labId=:labId";
        Session session = sessionFactory.getCurrentSession();
        Query<RecordLabAttempt> query = session.createQuery(hql, RecordLabAttempt.class);
        query.setParameter("traineeId", traineeId);
        query.setParameter("labId", labId);
        List<RecordLabAttempt> recordAttempts = query.getResultList();
        List<RecordLabAttemptDto> recordLabAttemptDto = Convert.getModelMapper().toDtoList(recordAttempts, RecordLabAttemptDto.class);
        session.clear();
        return recordLabAttemptDto;
    }

    @Override
    public List<RecordLabTestAttemptDto> fetchLabTestRecordAttempt(String traineeId, int labTestId) {
        String hql = "from RecordLabTestAttempt where traineeId=:traineeId and labTestId=:labTestId";
        Session session = sessionFactory.getCurrentSession();
        Query<RecordLabTestAttempt> query = session.createQuery(hql, RecordLabTestAttempt.class);
        query.setParameter("traineeId", traineeId);
        query.setParameter("labTestId", labTestId);
        List<RecordLabTestAttempt> recordLabTestAttempts = query.getResultList();
        List<RecordLabTestAttemptDto> recordLabTestAttemptDto = Convert.getModelMapper().toDtoList(recordLabTestAttempts, RecordLabTestAttemptDto.class);
        session.clear();
        return recordLabTestAttemptDto;
    }

    @Override
    public TraineeLabReportsDto fetchTraineeReports(int reportId) {
        TraineeLabReports traineeLabReports = sessionFactory.getCurrentSession().get(TraineeLabReports.class, reportId);
        return Convert.getModelMapper().toDto(traineeLabReports, TraineeLabReportsDto.class);
    }

    @Override
    public RecordLabTestAttemptDto fetchRecordLabTestAttempt(String traineeId, int labTestId, int questionId) {
        String hql = "from RecordLabTestAttempt where traineeId=:traineeId and labTestId=:labTestId and questionId=:questionId";
        Session session = sessionFactory.getCurrentSession();
        Query<RecordLabTestAttempt> query = session.createQuery(hql, RecordLabTestAttempt.class);
        query.setParameter("traineeId", traineeId);
        query.setParameter("labTestId", labTestId);
        query.setParameter("questionId", questionId);
        RecordLabTestAttempt recordLabTestAttempt = query.uniqueResult();
        if (recordLabTestAttempt == null) {
            return new RecordLabTestAttemptDto();
        }
        RecordLabTestAttemptDto recordLabTestAttemptDto = Convert.getModelMapper().toDto(recordLabTestAttempt, RecordLabTestAttemptDto.class);
        session.evict(recordLabTestAttempt);
        return recordLabTestAttemptDto;
    }

    @Override
    public void saveRecordLabTestAttempt(RecordLabTestAttemptDto recordLabTestAttemptDto) {
        Session session = sessionFactory.getCurrentSession();
        List<LabTestSubmitQueryDto> submitQuery = recordLabTestAttemptDto.getLabTestSubmitQueries();
        List<LabTestSubmitQuery> submitQueries = Convert.getModelMapper().toEntityList(submitQuery, LabTestSubmitQuery.class);
        RecordLabTestAttempt recordAttempt = Convert.getModelMapper().toEntity(recordLabTestAttemptDto, RecordLabTestAttempt.class);
        recordAttempt.setLabTestSubmitQueries(submitQueries);
        session.saveOrUpdate(recordAttempt);
    }
}
