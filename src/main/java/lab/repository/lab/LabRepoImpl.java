package lab.repository.lab;


import lab.convert.Convert;
import lab.dto.evaluation.LabReportsDto;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.admin.Admin;
import lab.entity.admin.Batch;
import lab.entity.evaluation.LabReports;
import lab.entity.evaluation.RecordLabAttempt;
import lab.entity.lab.Lab;
import lab.entity.lab.LabInfo;
import lab.entity.lab.LabStatus;
import lab.entity.lab.LabTest;
import lab.entity.questionBank.Question;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.lab.*;
import lab.payload.report.LabReport;
import lab.payload.report.TraineeLabMarksPayload;
import lab.repository.batches.BatchRepo;
import lab.repository.trainee.TraineeRepo;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


@Repository
public class LabRepoImpl implements LabRepo {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private BatchRepo batchRepo;

    @Autowired
    private TraineeRepo traineeRepo;

    @Override
    public boolean addNewLab(LabDto labDto) {
        Session session = sessionFactory.getCurrentSession();
        Lab lab = Convert.getModelMapper().toEntity(labDto, Lab.class);
        if (lab != null) {
            session.save(lab);
        }
        return true;
    }

    @Override
    public LabDto getLab(int labId) {
        Session session = sessionFactory.getCurrentSession();
        Lab lab = session.get(Lab.class, labId);
        if (lab != null) {
            LabDto labDto = Convert.getModelMapper().toDto(lab, LabDto.class);
            session.clear();
            return labDto;
        }
        return null;
    }
    @Override
    public LabDto getLabWithoutReport(int labId) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String hql = "SELECT l.labId, l.labName, q.questionId, q.questionDescription, q.questionAnswer, q.questionPoints " +
                    "FROM Lab l " +
                    "JOIN l.questions q " +
                    "WHERE l.labId = :labId";

            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("labId", labId);
            List<Object[]> results = query.getResultList();
            if (!results.isEmpty()) {
                LabDto labDto = new LabDto();
                labDto.setLabId((int) results.get(0)[0]);
                labDto.setLabName((String) results.get(0)[1]);
                List<QuestionDto> questions = new ArrayList<>();
                for (Object[] result : results) {
                    QuestionDto questionDto = new QuestionDto();
                    questionDto.setQuestionId((int) result[2]);
                    questionDto.setQuestionDescription((String) result[3]);
                    questionDto.setQuestionAnswer((String) result[4]);
                    questionDto.setQuestionPoints((int) result[5]);
                    questions.add(questionDto);
                }
                labDto.setQuestions(questions);
                return labDto;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public LabTestDto getLabTest(int labId) {
        Session session = sessionFactory.getCurrentSession();
        LabTest labTest = session.get(LabTest.class, labId);

        if (labTest != null) {
            LabTestDto labTestDto = Convert.getModelMapper().toDto(labTest, LabTestDto.class);
            session.clear();
            return labTestDto;
        }
        return null;
    }

    @Override
    public Lab getLabById(int labId) {
        Session session = sessionFactory.getCurrentSession();
        Lab lab = session.get(Lab.class, labId);
        return lab;
    }

    @Override
    public void updateLab(LabDto labDto) {
        Session session = sessionFactory.getCurrentSession();
        Lab lab = Convert.getModelMapper().toEntity(labDto, Lab.class);
        session.update(lab);
    }

    @Override
    public void updateLabTest(LabTestDto labTestDto) {
        Session session = sessionFactory.getCurrentSession();
        LabTest labTest = Convert.getModelMapper().toEntity(labTestDto, LabTest.class);
        session.saveOrUpdate(labTest);
    }

    @Override
    public LabQuestion getLabQuestionData(String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        LabQuestion labQuestion = new LabQuestion();
        try {
            String hql = "SELECT " +
                    "SUM(CASE WHEN qs.status = 'UNATTEMPTED' THEN 1 ELSE 0 END), " +
                    "SUM(CASE WHEN qs.status = 'CORRECT' THEN 1 ELSE 0 END), " +
                    "SUM(CASE WHEN qs.status = 'INCORRECT' THEN 1 ELSE 0 END) " +
                    "FROM LabInfo li " +
                    "JOIN li.questionStatusList qs " +
                    "WHERE li.traineeId = :traineeId";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("traineeId", traineeId);
            Object[] result = query.uniqueResult();
            if (result != null && result.length >= 3) {
                labQuestion.setUnattemptedLabQuestion(result[0] != null ? ((Number) result[0]).intValue() : 0);
                labQuestion.setCorrectLabQuestion(result[1] != null ? ((Number) result[1]).intValue() : 0);
                labQuestion.setIncorrectLabQuestion(result[2] != null ? ((Number) result[2]).intValue() : 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return labQuestion;
    }


    @Override
    public LabDetail getLabData(String traineeId, int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT " +
                "SUM(CASE WHEN li.labStatus = 'UNATTEMPTED' THEN 1 ELSE 0 END) AS unattemptedLab, " +
                "SUM(CASE WHEN li.labStatus = 'RESUME' THEN 1 ELSE 0 END) AS resumeLab, " +
                "SUM(CASE WHEN li.labStatus = 'COMPLETED' THEN 1 ELSE 0 END) AS completedLab " +
                "FROM LabInfo li " +
                "WHERE li.batch = :batchId AND li.traineeId = :traineeId";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("batchId", batchId);
        query.setParameter("traineeId", traineeId);
        Object[] result = query.uniqueResult();
        LabDetail labDetail = new LabDetail();
        if (result != null && result.length >= 3) {
            labDetail.setUnattemptedLab(result[0] != null ? ((Number) result[0]).intValue() : 0);
            labDetail.setResumeLab(result[1] != null ? ((Number) result[1]).intValue() : 0);
            labDetail.setCompletedLab(result[2] != null ? ((Number) result[2]).intValue() : 0);
        }
        return labDetail;
    }

    @Override
    public List<LabPayload> getIncompleteLab(String userId) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(userId);
        int batchId = traineeDto.getBatchId();
        Session session = sessionFactory.getCurrentSession();
        Batch batch = session.get(Batch.class, batchId);
        String hqlQuery = "SELECT lt.labId " +
                "FROM LabInfo lt " +
                "WHERE lt.batch = :batchId " +
                "AND lt.traineeId = :traineeId " +
                "AND lt.labStatus =:status";
        Query<Integer> query = session.createQuery(hqlQuery, Integer.class);
        query.setParameter("batchId", batchId);
        query.setParameter("traineeId", traineeDto.getEmployeeId());
        query.setParameter("status", LabStatus.COMPLETED);
        List<Integer> labList = query.getResultList();
        List<LabPayload> labDtos = getLabsByTraineeUsername(userId);
        Iterator<LabPayload> iterator = labDtos.iterator();
        while (iterator.hasNext()) {
            LabPayload labDto = iterator.next();
            for (int lab : labList) {
                if (labDto.getLabId() == lab) {
                    iterator.remove();
                }
            }
        }
        return labDtos;
    }

    @Override
    public List<LabPayload> getCompleteLab(String userId) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(userId);
        int batchId = traineeDto.getBatchId();
        Session session = sessionFactory.getCurrentSession();
        Batch batch = session.get(Batch.class, batchId);
        String hqlQuery = "SELECT lt.labId " +
                "FROM LabInfo lt " +
                "WHERE lt.batch = :batchId " +
                "AND lt.traineeId = :traineeId " +
                "AND lt.labStatus =:status";
        Query<Integer> query = session.createQuery(hqlQuery, Integer.class);
        query.setParameter("batchId", batchId);
        query.setParameter("traineeId", traineeDto.getEmployeeId());
        query.setParameter("status", LabStatus.COMPLETED);
        List<Integer> labList = query.getResultList();
        List<LabPayload> labDtos = getLabsByTraineeUsername(userId);
        Iterator<LabPayload> iterator = labDtos.iterator();
        while (iterator.hasNext()) {
            LabPayload labDto = iterator.next();
            boolean flag = false;
            for (int lab : labList) {
                if (labDto.getLabId() == lab) {
                    flag = true;
                    break;
                }
            }
            if (!flag) iterator.remove();
        }
        return labDtos;
    }

    @Override
    public List<RecordLabAttempt> getLabReport(int labId, String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT r " +
                "FROM RecordLabAttempt r " +
                "WHERE r.labId = :labId " +
                "AND r.traineeId = :traineeId";
        Query<RecordLabAttempt> query = session.createQuery(hql, RecordLabAttempt.class);
        query.setParameter("labId", labId);
        query.setParameter("traineeId", traineeId);
        List<RecordLabAttempt> recordLabAttempt = query.getResultList();
        return recordLabAttempt;
    }

    @Override
    public void addLabInfo(LabInfo labInfo) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(labInfo);
    }

    @Override
    public LabInfo getLabInfo(int labId, String traineeId, int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from LabInfo where labId=:labId and traineeId = :traineeId and batch = :batchId";
        Query<LabInfo> query = session.createQuery(hql, LabInfo.class);
        query.setParameter("labId", labId);
        query.setParameter("traineeId", traineeId);
        query.setParameter("batchId", batchId);
        LabInfo labInfo = query.uniqueResult();
        return labInfo;
    }

    @Override
    public List<LabDto> getAllLab() {
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "From Lab";
        Query<Lab> labQuery = session.createQuery(hqlQuery, Lab.class);
        List<Lab> resultList = labQuery.getResultList();
        List<LabDto> labDtoList = Convert.getModelMapper().toDtoList(resultList, LabDto.class);
        return labDtoList;
    }

    @Override
    public int totalQuestion(int labId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(q) FROM Lab l JOIN l.questions q WHERE l.labId = :labId";
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("labId", labId);
        Long totalQuestions = query.getSingleResult();
        return totalQuestions != null ? totalQuestions.intValue() : 0;
//        Lab lab = session.get(Lab.class, labId);
//        int noOfQuestions = lab.getQuestions().size();
//        return noOfQuestions;
    }

    @Override
    public List<RecordLabAttempt> getLabReport(int labId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT r " +
                "FROM RecordLabAttempt r " +
                "WHERE r.labId = :labId ";
        Query<RecordLabAttempt> query = session.createQuery(hql, RecordLabAttempt.class);
        query.setParameter("labId", labId);
        List<RecordLabAttempt> recordLabAttempt = query.getResultList();
        return recordLabAttempt;
    }

    @Override
    public void updateLabInfo(LabInfo labInfo) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(labInfo);
    }

    @Override
    public List<LabPayload> getUnAttemptedLab(String userId) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(userId);
        int batchId = traineeDto.getBatchId();
        Session session = sessionFactory.getCurrentSession();
        Batch batch = session.get(Batch.class, batchId);
        String hqlQuery = "SELECT lt.labId " +
                "FROM LabInfo lt " +
                "WHERE lt.batch = :batchId " +
                "AND lt.traineeId = :traineeId";
        Query<Integer> query = session.createQuery(hqlQuery, Integer.class);
        query.setParameter("batchId", batchId);
        query.setParameter("traineeId", traineeDto.getEmployeeId());
        List<Integer> labList = query.getResultList();
        List<LabPayload> labDtos = getLabsByTraineeUsername(userId);
        Iterator<LabPayload> iterator = labDtos.iterator();
        while (iterator.hasNext()) {
            LabPayload labDto = iterator.next();
            boolean flag = false;
            for (int lab : labList) {
                if (labDto.getLabId() == lab) {
                    flag = true;
                    break;
                }
            }
            if (flag) iterator.remove();
        }
        return labDtos;
    }

    @Override
    public List<LabPayload> getTraineeLabs(String userId, String status) {
        List<LabPayload> lab = getLabAccordingToStatus(userId, status);
        return lab;
    }

    @Override
    public int getQuestionPoint(int labId) {
        Session session = sessionFactory.getCurrentSession();
        try {
            String hql = "select l.pointsPerQuestion from Lab l where l.labId = :labId";
            Query query = session.createQuery(hql);
            query.setParameter("labId", labId);
            return (int) query.uniqueResult();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void addLabInfoList(List<LabInfo> labInfoList) {
        Session session = sessionFactory.getCurrentSession();
        for (LabInfo labInfo : labInfoList) {
            session.saveOrUpdate(labInfo);
        }
        session.flush();
    }

    @Override
    public int getNoOfQuestionInLabs(String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(q) " +
                "FROM Admin a " +
                "JOIN a.labs l " +
                "JOIN l.questions q " +
                "JOIN a.batches b " +
                "JOIN b.trainees t " +
                "WHERE t.employeeId = :traineeId";
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("traineeId", traineeId);
        Long totalQuestions = query.getSingleResult();
        return totalQuestions != null ? totalQuestions.intValue() : 0;
    }

    @Override
    public List<LabInfo> getLabInfoList(int labId, int batchId) {
        String hql = "from LabInfo where batch=:batchId AND labId=:labId";
        Session session = sessionFactory.getCurrentSession();
        Query<LabInfo> query = session.createQuery(hql, LabInfo.class);
        query.setParameter("batchId", batchId);
        query.setParameter("labId", labId);
        List<LabInfo> labInfo = query.getResultList();

        return labInfo;
    }

    @Override
    public List<LabInfo> getLabInfoList(int id) {
        String hql = "from LabInfo where labId=:labId";
        Session session = sessionFactory.getCurrentSession();
        Query<LabInfo> query = session.createQuery(hql, LabInfo.class);
        query.setParameter("labId", id);
        List<LabInfo> labInfo = query.getResultList();
        return labInfo;
    }

    @Override
    public List<LabReport> getLabReportInfo(int labId, int batchId) {
        String hql = "SELECT li, u.name FROM LabInfo li " +
                "JOIN User u ON li.traineeId = u.employeeId " +
                "WHERE li.labId = :labId AND li.batch = :batchId";
        Session session = sessionFactory.getCurrentSession();
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("labId", labId);
        query.setParameter("batchId", batchId);
        List<Object[]> resultList = query.getResultList();
        List<LabReport> labReports = new ArrayList<>();
        for (Object[] result : resultList) {
            LabReport labReport = new LabReport();
            labReport.setLabInfo((LabInfo) result[0]);
            labReport.setTraineeName((String) result[1]);
            labReports.add(labReport);
        }
        return labReports;
    }

    @Override
    public List<QuestionPointsPayLoad> getQuestionListViaLabId(int labId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT q.questionId, q.questionPoints, q.questionDescription FROM Lab l " +
                "JOIN l.questions q " +
                "WHERE l.labId = :labId";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("labId", labId);
        List<Object[]> result = query.getResultList();
        List<QuestionPointsPayLoad> questionPointsList = new ArrayList<>();
        for (Object[] res : result) {
            QuestionPointsPayLoad questionPoint = new QuestionPointsPayLoad();
            questionPoint.setQuestionId((Integer) res[0]);
            questionPoint.setQuestionPoints((Integer) res[1]);
            questionPoint.setQuestionDescription((String) res[2]);
            questionPointsList.add(questionPoint);
        }
        return questionPointsList;
    }

    @Override
    public List<LabInfoIdBatch> getLabInfoIdBatch(int labId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select new lab.payload.lab.LabInfoIdBatch(li.labInfoId, li.batch, lab.labName) " +
                "from LabInfo li, Lab lab " +
                "where li.labId = :labId and li.labId = lab.labId";
        Query<LabInfoIdBatch> query = session.createQuery(hql, LabInfoIdBatch.class);
        query.setParameter("labId", labId);
        List<LabInfoIdBatch> labInfo = query.getResultList();
        return labInfo;
    }

    @Override
    public List<LabSummary> getLabByIdBatch(int labId, int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select new lab.payload.lab.LabSummary(ls.labId, ls.labName, t.name, t.employeeId, b.id) " +
                "from Batch b " +
                "join b.labs ls " +
                "join b.trainees t " +
                "where ls.labId = :labId and b.id = :batchId";
        Query<LabSummary> query = session.createQuery(hql, LabSummary.class);
        query.setParameter("labId", labId);
        query.setParameter("batchId", batchId);
        List<LabSummary> labInfo = query.getResultList();
        return labInfo;
    }

    @Override
    public List<LabPayload> getLabByBatch(int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select new lab.payload.lab.LabPayload(l.labId, l.labName) " +
                "from Batch b " +
                "join b.labs l " +
                "where b.id = :batchId";
        Query<LabPayload> query = session.createQuery(hql, LabPayload.class);
        query.setParameter("batchId", batchId);
        List<LabPayload> labInfo = query.getResultList();
        return labInfo;
    }

    @Override
    public LabReport getLabReportInfoByTraniee(int labId, int batchId, String traineeId) {
        String hql = "SELECT li, u.name FROM LabInfo li " +
                "JOIN User u ON li.traineeId = u.employeeId " +
                "WHERE li.labId = :labId AND li.batch = :batchId AND u.employeeId = :traineeId";

        Session session = sessionFactory.getCurrentSession();
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("labId", labId);
        query.setParameter("batchId", batchId);
        query.setParameter("traineeId", traineeId);

        Object[] result = query.uniqueResult();

        if (result != null) {
            LabReport labReport = new LabReport();
            labReport.setLabInfo((LabInfo) result[0]);
            labReport.setTraineeName((String) result[1]);
            return labReport;
        } else {
            return null;
        }
    }

    @Override
    public List<RecordLabAttempt> getReportsOfTraineeByLabId(String traineeId, int labId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT ra " +
                "FROM RecordLabAttempt ra " +
                "WHERE ra.traineeId = :traineeId AND ra.labId = :labId";

        Query<RecordLabAttempt> query = session.createQuery(hql, RecordLabAttempt.class);
        query.setParameter("traineeId", traineeId);
        query.setParameter("labId", labId);

        return query.getResultList();
    }

    @Override
    public LabPayload getLabPayloadById(int labId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select new lab.payload.lab.LabPayload(l.labId, l.labName) " +
                "from Lab l " +
                "where l.labId = :labId";
        Query<LabPayload> query = session.createQuery(hql, LabPayload.class);
        query.setParameter("labId", labId);
        LabPayload labInfo = query.uniqueResult();
        return labInfo;
    }

    @Override
    public LabDto createLab(LabDto labDto) {
        Session session = sessionFactory.getCurrentSession();
        Lab lab = Convert.getModelMapper().toEntity(labDto, Lab.class);
        session.saveOrUpdate(lab);
        labDto = Convert.getModelMapper().toDto(lab, LabDto.class);
        return labDto;
    }

    @Override
    public void addLabByAdminIdAndLab(long adminId, int labId) {
        Session session = sessionFactory.getCurrentSession();
        String nativeSQLInsert = "INSERT INTO nSbt_sql_17239_sql_lab_17239 (ADMIN_ID, LABS_LABID) " +
                "VALUES (:adminId, :labId)";
        SQLQuery query = session.createSQLQuery(nativeSQLInsert);
        query.setParameter("adminId", adminId);
        query.setParameter("labId", labId);
        int rowsAffected = query.executeUpdate();
    }

    @Override
    public void removeQuestionById(int labId, int questionId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "DELETE FROM Lab l JOIN l.questions q WHERE l.labId = :labId AND q.questionId = :questionId";
        Query<?> query = session.createQuery(hql);
        query.setParameter("labId", labId);
        query.setParameter("questionId", questionId);
        int rowsAffected = query.executeUpdate();
    }

    @Override
    public boolean removeQuestionFromJoinTable(int labId, int questionId) {
        Session session = sessionFactory.getCurrentSession();
        String nativeSQLDelete = "DELETE FROM sql_lab_17239_sql_question_answer_bank_17239 " +
                "WHERE LAB_LABID = :labId AND QUESTIONS_QUESTIONID = :questionId";
        SQLQuery query = session.createSQLQuery(nativeSQLDelete);
        query.setParameter("labId", labId);
        query.setParameter("questionId", questionId);
        int rowsAffected = query.executeUpdate();
        return true;
    }

    @Override
    public void removeQuestionFromTableById(int questionId) {
        Session session = sessionFactory.getCurrentSession();
        Question question = session.get(Question.class, questionId);
        if (question != null)
            session.delete(question);
    }


    private List<LabPayload> getLabAccordingToStatus(String userId, String status) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(userId);
        int batchId = traineeDto.getBatchId();
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "SELECT lt.labId " +
                "FROM LabInfo lt " +
                "WHERE lt.batch = :batchId " +
                "AND lt.traineeId = :traineeId " +
                "AND lt.labStatus =:status";
        Query<Integer> query = session.createQuery(hqlQuery, Integer.class);
        query.setParameter("batchId", batchId);
        query.setParameter("traineeId", traineeDto.getEmployeeId());
        if (status.equals("completed")) {
            query.setParameter("status", LabStatus.COMPLETED);
        } else if (status.equals("inprogress")) {
            query.setParameter("status", LabStatus.RESUME);
        } else if (status.equals("unattempted")) {
            query.setParameter("status", LabStatus.UNATTEMPTED);
        } else {
            return (List<LabPayload>) new LabTestDto();
        }
        List<Integer> labList = query.getResultList();
        List<LabPayload> labDtos = getLabsByTraineeUsername(userId);
        Iterator<LabPayload> iterator = labDtos.iterator();
        while (iterator.hasNext()) {
            LabPayload labDto = iterator.next();
            boolean flag = false;
            for (int lab : labList) {
                if (labDto.getLabId() == lab) {
                    flag = true;
                    break;
                }
            }
            if (!flag) iterator.remove();
        }
        return labDtos;
    }

    @Override
    public void submitLabReport(LabDto labDto) {
        Session session = sessionFactory.openSession();
        Transaction txn = session.beginTransaction();
        Lab lab = session.get(Lab.class, labDto.getLabId());
        Lab lab1 = Convert.getModelMapper().toEntity(labDto, Lab.class);
        lab.setReports(lab1.getReports());
        txn.commit();
        session.close();
    }

    @Override
    public List<LabPayload> getLabsByTraineeUsername(String traineeUserName) {
        Session session = sessionFactory.getCurrentSession();
        List<Object[]> results = session.createQuery(
                        "SELECT lab.labId, lab.labName, COUNT(ques) ,SUM(ques.questionPoints)" +
                                "FROM Lab lab LEFT JOIN lab.questions ques " +
                                "GROUP BY lab.labId, lab.labName", Object[].class)
                .getResultList();

        List<LabPayload> labPayloads = results.stream().map(result -> {
            LabPayload payload = new LabPayload();
            payload.setLabId((int) result[0]);
            payload.setLabName((String) result[1]);
            payload.setNoOfQuestions(result[2] != null ? ((Long) result[2]).intValue() : 0);
            payload.setTotalMarks(result[3] != null ? ((Long) result[3]).intValue() : 0);
            return payload;
        }).collect(Collectors.toList());
        return labPayloads;
    }

    @Override
    public LabReportsDto getLabReportByBatchId(int batchId) {
        String hql = "from Reports where batchId=:batchId1";
        Session session = sessionFactory.getCurrentSession();
        Query<LabReports> query = session.createQuery(hql, LabReports.class);
        query.setParameter("batchId1", batchId);
        LabReports reports = query.uniqueResult();
        if (reports == null) {
            reports = new LabReports();
        }
        LabReportsDto reportsDto = Convert.getModelMapper().toDto(reports, LabReportsDto.class);
        session.evict(reports);
        session.clear();
        return reportsDto;
    }

    @Override
    public QuestionDto getQuestion(int labId, int questionId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select q.questionId, q.questionDescription, q.questionAnswer, q.questionPoints From Lab l join l.questions q where l.labId=:labId and q.questionId=:questionId";
        Query queue = session.createQuery(hql);
        queue.setParameter("labId", labId);
        queue.setParameter("questionId", questionId);
        Object[] questionDetail = (Object[]) queue.uniqueResult();
        QuestionDto questionDto = new QuestionDto();
        questionDto.setQuestionId((int) questionDetail[0]);
        questionDto.setQuestionDescription((String) questionDetail[1]);
        questionDto.setQuestionAnswer((String) questionDetail[2]);
        questionDto.setQuestionPoints((int) questionDetail[3]);
        session.clear();
        return questionDto;
    }

    @Override
    public boolean updateQuestion(QuestionDto questionDto) {
        Session session = sessionFactory.getCurrentSession();
        Question question = Convert.getModelMapper().toEntity(questionDto, Question.class);
        session.update(question);
        session.clear();
        return true;
    }
    @Override
    public void updateQuestionByHql(QuestionDto questionDto) {
        Session session = sessionFactory.getCurrentSession();
        int questionId = questionDto.getQuestionId();
        String hql = "UPDATE Question SET questionDescription = :questionDescription, questionAnswer = :questionAnswer, questionPoints = :questionPoints WHERE questionId = :questionId";
        Query<?> query = session.createQuery(hql);
        query.setParameter("questionDescription", questionDto.getQuestionDescription());
        query.setParameter("questionAnswer", questionDto.getQuestionAnswer());
        query.setParameter("questionPoints", questionDto.getQuestionPoints());
        query.setParameter("questionId", questionId);
        int updatedEntities = query.executeUpdate();
    }

    @Override
    public List<LabInfo> getAllLabInfoByTrainee(String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "FROM LabInfo lti WHERE lti.traineeId = :traineeId";
        Query<LabInfo> query = session.createQuery(hqlQuery, LabInfo.class);
        query.setParameter("traineeId", traineeId);
        return query.getResultList();
    }

    @Override
    public List<LabPayload> getLabByBatchWithTotalMarks(int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT NEW lab.payload.lab.LabPayload(l.labId, l.labName, SUM(q.questionPoints)) " +
                "FROM Batch b " +
                "JOIN b.labs l " +
                "JOIN l.questions q " +
                "WHERE b.id = :batchId " +
                "GROUP BY l.labId, l.labName";

        Query<LabPayload> query = session.createQuery(hql, LabPayload.class);
        query.setParameter("batchId", batchId);
        List<LabPayload> labInfo = query.getResultList();
        return labInfo;
    }

    @Override
    public List<TraineeLabMarksPayload> getTraineeMarksByTraineeIdBatchId(int batchId, String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT NEW lab.payload.report.TraineeLabMarksPayload(r.labId, SUM(r.traineeCurrentQuestionPoints), r.traineeId) " +
                "FROM RecordLabAttempt r " +
                "WHERE r.traineeId = :traineeId " +
                "GROUP BY r.labId, r.traineeId";

        Query query = session.createQuery(hql, TraineeLabMarksPayload.class);
        query.setParameter("traineeId", traineeId);
        List<TraineeLabMarksPayload> traineeMarksPayloads= query.getResultList();
        return traineeMarksPayloads;
    }

    @Override
    public boolean deleteQuestion(QuestionDto questionDto, int labId) {
        Session session = sessionFactory.getCurrentSession();
        Lab lab = session.get(Lab.class, labId);
        List<Question> questionList = lab.getQuestions();
        Iterator<Question> it = questionList.iterator();
        while (it.hasNext()) {
            Question question1 = it.next();
            if (question1.getQuestionId() == questionDto.getQuestionId())
                it.remove();
        }
        session.update(lab);
        return true;
    }

    @Override
    public void removeQuestion(QuestionDto questionDto) {
        Session session = sessionFactory.getCurrentSession();
        Question question = session.get(Question.class, questionDto.getQuestionId());
//        Question question=Convert.getModelMapper().toEntity(questionDto, Question.class);
        session.delete(question);
    }

//    public AnswerResponseMessage checkEquality(String userQue, String adminQue) {
//        AnswerResponseMessage answerResponseMessage = new AnswerResponseMessage();
//        Connection con = Connections.getConnection();
//        try {
//            PreparedStatement ps1 = con.prepareStatement(userQue);
//            ResultSet rs1 = ps1.executeQuery();
//            ResultSetMetaData metaData1 = rs1.getMetaData();
//            int colCount1 = metaData1.getColumnCount();
//            PreparedStatement ps2 = con.prepareStatement(adminQue);
//            ResultSet rs2 = ps2.executeQuery();
//            ResultSetMetaData metaData2 = rs2.getMetaData();
//            int colCount2 = metaData2.getColumnCount();
//            ResultSet rs3 = ps1.executeQuery();
//            List listOfColumnName = new ArrayList();
//            for (int i = 1; i <= colCount1; i++) {
//                listOfColumnName.add(metaData1.getColumnName(i));
//            }
//            arr.add(listOfColumnName);
//            while (rs3.next()) {
//                List temp = new ArrayList();
//                for (int i = 1; i <= colCount1; i++) {
//                    Object temp1 = rs3.getObject(i);
//                    String val1 = null;
//                    if (temp1 != null) {
//                        val1 = rs3.getObject(i).toString();
//                    }
//                    temp.add(val1);
//                }
//                arr.add(temp);
//            }
//            if (colCount1 == colCount2) {
//                while (rs1.next() && rs2.next()) {
//                    for (int i = 1; i < colCount1; i++) {
//                        Object temp1 = rs1.getObject(i);
//                        Object temp2 = rs2.getObject(i);
//                        String val1 = null;
//                        String val2 = null;
//                        if (temp1 != null) {
//                            val1 = rs1.getObject(i).toString();
//                        }
//                        if (temp2 != null) {
//                            val2 = rs2.getObject(i).toString();
//                        }
//                        assert val1 != null;
//                        if (!val1.equalsIgnoreCase(val2)) {
//                            answerResponseMessage.setIncorrect(true);
//                            return answerResponseMessage;
//                        } else {
//                        }
//                    }
//                }
//                answerResponseMessage.setCorrect(true);
//                return answerResponseMessage;
//            } else {
//                answerResponseMessage.setIncorrect(true);
//                return answerResponseMessage;
//            }
//        } catch (SQLException e) {
//            String errorMessage = e.getMessage();
//            int startIndex = errorMessage.indexOf(":") + 1;
//            String message = errorMessage.substring(startIndex).trim();
//            answerResponseMessage.setIsInvalidSyntax(message);
//            return answerResponseMessage;
//        }
//    }

}
