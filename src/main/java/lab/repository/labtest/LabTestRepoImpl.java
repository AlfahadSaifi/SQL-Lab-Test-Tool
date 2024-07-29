package lab.repository.labtest;

import lab.convert.Convert;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.admin.Batch;
import lab.entity.evaluation.RecordLabTestAttempt;
import lab.entity.evaluation.TraineeLabTestReports;
import lab.entity.lab.LabStatus;
import lab.entity.lab.LabTest;
import lab.entity.lab.LabTestInfo;
import lab.entity.questionBank.Question;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.labTest.LabTestDetail;
import lab.payload.labTest.LabTestPayload;
import lab.payload.labTest.LabTestQuestion;
import lab.payload.report.LabTestPassPercentage;
import lab.payload.report.LabTestReport;
import lab.payload.report.TraineeMarksPayload;
import lab.repository.trainee.TraineeRepo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class LabTestRepoImpl implements LabTestRepo {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private TraineeRepo traineeRepo;

    @Override
    public void addLabTest(LabTestDto labTestDto, String username) {
        LabTest labTest = Convert.getModelMapper().toEntity(labTestDto, LabTest.class);
        sessionFactory.getCurrentSession().save(labTest);
    }

    @Override
    public LabTestDto getLabTest(int labTestId) {
        Session session = sessionFactory.getCurrentSession();
        LabTest labTest = session.get(LabTest.class, labTestId);
        if (labTest != null) {
            LabTestDto labTestDto = Convert.getModelMapper().toDto(labTest, LabTestDto.class);
            session.clear();
            return labTestDto;
        }
        return null;
    }

    @Override
    public LabTestDto updateLabTest(LabTestDto labTestDto) {
        Session session = sessionFactory.getCurrentSession();
        LabTest labTest = Convert.getModelMapper().toEntity(labTestDto, LabTest.class);
        session.update(labTest);
        labTestDto = Convert.getModelMapper().toDto(labTest, LabTestDto.class);
        return labTestDto;
    }

    @Override
    public boolean deleteLabTest(LabTestDto labTestDto) {
        Session session = sessionFactory.getCurrentSession();
        LabTest labTest = Convert.getModelMapper().toEntity(labTestDto, LabTest.class);
        session.delete(labTest);
        return true;
    }

    @Override
    public List<LabTestDto> getTestsByTraineeUsername(String employeeId) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(employeeId);
        int batchId = traineeDto.getBatchId();
        Session session = sessionFactory.getCurrentSession();
        Batch batch = session.get(Batch.class, batchId);
        List<LabTest> labTestList = batch.getLabTests();
        return Convert.getModelMapper().toDtoList(labTestList, LabTestDto.class);
    }

    @Override
    public LabTestDto getTest(int labId) {
        Session session = sessionFactory.getCurrentSession();
        LabTest labTest = session.get(LabTest.class, labId);
        LabTestDto labTestDto = Convert.getModelMapper().toDto(labTest, LabTestDto.class);
        session.evict(labTest);
        session.clear();
        return labTestDto;
    }

    @Override
    public QuestionDto getLabTestQuestion(int id, int questionId) {
        Session session = sessionFactory.getCurrentSession();
        Question question = session.get(Question.class, questionId);
        QuestionDto questionDto = Convert.getModelMapper().toDto(question, QuestionDto.class);
        return questionDto;
    }

    @Override
    public void editQuestionInLabTest(QuestionDto questionDto) {
        Session session = sessionFactory.getCurrentSession();
        Question question = Convert.getModelMapper().toEntity(questionDto, Question.class);
        session.update(question);
    }

    @Override
    public void addLabTestInfo(LabTestInfo labTestInfo) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(labTestInfo);
        session.flush();
    }
    @Override  // to map questions to Assessment
    public boolean mapQuestionsToLabTest(int labId,List<Integer> questionIds)
    {
        String hql = "insert into sql_lab_test_17239_sql_question_answer_bank_17239 values(:labTestID,:questionID)";
        Session session = sessionFactory.getCurrentSession();
        boolean flag = true;
        for(Integer questionId:questionIds){
            if(questionId==null) {continue;}
            Query query = session.createNativeQuery(hql);
            query.setParameter("labTestID",labId);
            query.setParameter("questionID",questionId);
            int result =  query.executeUpdate();
            if(result<0)
            {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public LabTestInfo getLabTestInfo(int labTestId, String traineeId, int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "From LabTestInfo " +
                "where labTestId =:labTestId " +
                "and traineeId =:traineeId " +
                "and batch =:batchId";
        Query<LabTestInfo> query = session.createQuery(hql, LabTestInfo.class);
        query.setParameter("labTestId", labTestId);
        query.setParameter("traineeId", traineeId);
        query.setParameter("batchId", batchId);
        List<LabTestInfo> labTestInfoList = query.list();
        return labTestInfoList.get(0);
    }

    @Override
    public List<RecordLabTestAttempt> getLabTestReport(int labTestId, String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT r " +
                "FROM RecordLabTestAttempt r " +
                "WHERE r.labTestId = :labTestId " +
                "AND r.traineeId = :traineeId";
        Query<RecordLabTestAttempt> query = session.createQuery(hql, RecordLabTestAttempt.class);
        query.setParameter("labTestId", labTestId);
        query.setParameter("traineeId", traineeId);
        List<RecordLabTestAttempt> recordLabTestAttempt = query.getResultList();
        return recordLabTestAttempt;
    }

    @Override
    public LabTestQuestion getLabTestQuestionData(String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        LabTestQuestion labTestQuestion = new LabTestQuestion();
        try {
            String hql = "SELECT " +
                    "SUM(CASE WHEN qs.status = 'UNATTEMPTED' THEN 1 ELSE 0 END), " +
                    "SUM(CASE WHEN qs.status = 'CORRECT' THEN 1 ELSE 0 END), " +
                    "SUM(CASE WHEN qs.status = 'INCORRECT' THEN 1 ELSE 0 END) " +
                    "FROM LabTestInfo li " +
                    "JOIN li.questionStatusList qs " +
                    "WHERE li.traineeId = :traineeId";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("traineeId", traineeId);
            Object[] result = query.uniqueResult();
            if (result != null && result.length >= 3) {
                labTestQuestion.setUnattemptedLabTestQuestion(result[0] != null ? ((Number) result[0]).intValue() : 0);
                labTestQuestion.setCorrectLabTestQuestion(result[1] != null ? ((Number) result[1]).intValue() : 0);
                labTestQuestion.setIncorrectLabTestQuestion(result[2] != null ? ((Number) result[2]).intValue() : 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return labTestQuestion;
    }

    @Override
    public LabTestInfo getLabTestData(int batchId, int labTestId, String traineeId) {
        String hql = "from LabTestInfo where batch=:batchId AND labTestId=:labTestId AND traineeId=:traineeId";
        Session session = sessionFactory.getCurrentSession();
        Query<LabTestInfo> query = session.createQuery(hql, LabTestInfo.class);
        query.setParameter("batchId", batchId);
        query.setParameter("labTestId", labTestId);
        query.setParameter("traineeId", traineeId);
        LabTestInfo labTestInfo = query.uniqueResult();
        if (labTestInfo == null) {
            labTestInfo = new LabTestInfo();
        }
        session.clear();
        return labTestInfo;
    }

    @Override
    public void updateLabTestInfo(LabTestInfo labTestInfo) {
        Session session = sessionFactory.getCurrentSession();
        session.update(labTestInfo);
    }

    @Override
    public List<LabTestDto> getIncompleteLabTest(String userId) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(userId);
        int batchId = traineeDto.getBatchId();
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "SELECT lt.labTestId " +
                "FROM LabTestInfo lt " +
                "WHERE lt.batch = :batchId " +
                "AND lt.traineeId = :traineeId " +
                "AND lt.labTestStatus =:status";
        Query<Integer> query = session.createQuery(hqlQuery, Integer.class);
        query.setParameter("batchId", batchId);
        query.setParameter("traineeId", traineeDto.getEmployeeId());
        query.setParameter("status", LabStatus.COMPLETED);
        List<Integer> labTestList = query.getResultList();
        List<LabTestDto> labTestDtos = getTestsByTraineeUsername(userId);
        Iterator<LabTestDto> iterator = labTestDtos.iterator();
        while (iterator.hasNext()) {
            LabTestDto labTestDto = iterator.next();
            for (int labTest : labTestList) {
                if (labTestDto.getLabTestId() == labTest) {
                    iterator.remove();
                }
            }
        }
        return labTestDtos;
    }

    @Override
    public List<LabTestDto> getCompleteLabTest(String userId) {
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(userId);
        int batchId = traineeDto.getBatchId();
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "SELECT lt.labTestId " +
                "FROM LabTestInfo lt " +
                "WHERE lt.batch = :batchId " +
                "AND lt.traineeId = :traineeId " +
                "AND lt.labTestStatus =:status";
        Query<Integer> query = session.createQuery(hqlQuery, Integer.class);
        query.setParameter("batchId", batchId);
        query.setParameter("traineeId", traineeDto.getEmployeeId());
        query.setParameter("status", LabStatus.COMPLETED);
        List<Integer> labTestList = query.getResultList();
        List<LabTestDto> labTestDtos = getTestsByTraineeUsername(userId);
        Iterator<LabTestDto> iterator = labTestDtos.iterator();
        while (iterator.hasNext()) {
            LabTestDto labTestDto = iterator.next();
            boolean flag = false;
            for (int labTest : labTestList) {
                if (labTestDto.getLabTestId() == labTest) {
                    flag = true;
                }
            }
            if (flag == false) iterator.remove();
        }
        return labTestDtos;
    }

    @Override
    public int gettotalQuestion(int currentLabTestId) {
        Session session = sessionFactory.getCurrentSession();
        LabTest labTest = session.get(LabTest.class, currentLabTestId);
        int noOfQuestions = labTest.getQuestions().size();
        return noOfQuestions;
    }

    @Override
    public List<RecordLabTestAttempt> getLabTestReports(int labTestId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT r " +
                "FROM RecordLabTestAttempt r " +
                "WHERE r.labTestId = :labTestId ";
        Query<RecordLabTestAttempt> query = session.createQuery(hql, RecordLabTestAttempt.class);
        query.setParameter("labTestId", labTestId);
        List<RecordLabTestAttempt> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public List<LabTestDto> getAllLabTest() {
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "From LabTest";
        Query<LabTest> labQuery = session.createQuery(hqlQuery, LabTest.class);
        List<LabTest> resultList = labQuery.getResultList();
        List<LabTestDto> labTestDtoList = Convert.getModelMapper().toDtoList(resultList, LabTestDto.class);
        return labTestDtoList;
    }

    @Override
    public List<LabTestPayload> getTraineeLabTests(String userId, String status) {
        List<LabTestDto> labTest = getLabTestAccordingToStatus(userId, status);
        List<LabTestPayload> traineeLabTests = new ArrayList<>();
        for (LabTestDto labTestDto : labTest) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            if ((!status.equals("completed")) && labTestDto.getEndDate().isBefore(currentDateTime)) {
                continue;
            }
            List<QuestionDto> questions = labTestDto.getQuestions();
            int totalPoints = 0;
            for (QuestionDto questionDto : questions) {
                totalPoints += questionDto.getQuestionPoints();
            }
            LabTestPayload labTestPayload = new LabTestPayload();
            labTestPayload.setLabTestId(labTestDto.getLabTestId());
            labTestPayload.setLabTestName(labTestDto.getLabTestName());
            labTestPayload.setNoOfQuestions(labTestDto.getQuestions().size());
            labTestPayload.setDuration(labTestDto.getDuration());
            labTestPayload.setStartDate(labTestDto.getStartDate());
            labTestPayload.setEndDate(labTestDto.getEndDate());
            labTestPayload.setTotalLabTestPoints(totalPoints);
            traineeLabTests.add(labTestPayload);
        }
        return traineeLabTests;
    }

    @Override
    public void addLabTestInfoList(List<LabTestInfo> labTestInfoList) {
        Session session = sessionFactory.getCurrentSession();
        for (LabTestInfo labTestInfo : labTestInfoList) {
            session.saveOrUpdate(labTestInfo);
        }
        session.flush();
    }

    @Override
    public LabTestDetail getLabTestDetails(int batchId, String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT " +
                "SUM(CASE WHEN li.labTestStatus = 'UNATTEMPTED' THEN 1 ELSE 0 END) AS unattemptedLabTest, " +
                "SUM(CASE WHEN li.labTestStatus = 'RESUME' THEN 1 ELSE 0 END) AS resumeLabTest, " +
                "SUM(CASE WHEN li.labTestStatus = 'COMPLETED' THEN 1 ELSE 0 END) AS completedLabTest " +
                "FROM LabTestInfo li " +
                "WHERE li.batch = :batchId AND li.traineeId = :traineeId";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("batchId", batchId);
        query.setParameter("traineeId", traineeId);
        Object[] result = query.uniqueResult();
        LabTestDetail labTestDetail = new LabTestDetail();
        if (result.length >= 3) {
            labTestDetail.setUnattemptedLabTest(result[0] != null ? ((Number) result[0]).intValue() : 0);
            labTestDetail.setResumeLabTest(result[1] != null ? ((Number) result[1]).intValue() : 0);
            labTestDetail.setCompletedLabTest(result[2] != null ? ((Number) result[2]).intValue() : 0);
        }
        return labTestDetail;
    }

    @Override
    public int getNoOfQuestionInLabTests(String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(q) AS qSize " +
                "FROM Admin a " +
                "JOIN a.labTests lt " +
                "JOIN lt.questions q " +
                "JOIN a.batches b " +
                "JOIN b.trainees t " +
                "WHERE t.employeeId = :traineeId " +
                "GROUP BY lt.labTestId";
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("traineeId", traineeId);
        List<Long> labQuestionCounts = query.getResultList();
        long sumOfQuestions = labQuestionCounts.stream().mapToLong(Long::longValue).sum();
        return (int) sumOfQuestions;
    }

    @Override
    public List<LabTestReport> getLabTestInfoById(int labTestId, int batchId) {
        String hql = "SELECT lti, u.name FROM LabTestInfo lti " +
                "JOIN User u ON lti.traineeId = u.employeeId " +
                "WHERE lti.labTestId = :labTestId AND lti.batch = :batchId";

        Session session = sessionFactory.getCurrentSession();
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("labTestId", labTestId);
        query.setParameter("batchId", batchId);
        List<Object[]> resultList = query.getResultList();
        List<LabTestReport> labTestReports = new ArrayList<>();
        Set<String> addedTraineeIds = new HashSet<>();

        for (Object[] result : resultList) {
            LabTestInfo labTestInfo = (LabTestInfo) result[0];
            String traineeName = (String) result[1];
            if (!addedTraineeIds.contains(labTestInfo.getTraineeId())) {
                LabTestReport labTestReport = new LabTestReport();
                labTestReport.setLabTestInfo(labTestInfo);
                labTestReport.setTraineeName(traineeName);
                labTestReports.add(labTestReport);
                addedTraineeIds.add(labTestInfo.getTraineeId());
            }
        }
        return labTestReports;
    }

    @Override
    public List<LabTestInfo> getLabTestInfoById(int id) {
        String hql = "From LabTestInfo where labTestId=:labTestId";
        Session session = sessionFactory.getCurrentSession();
        Query<LabTestInfo> query = session.createQuery(hql, LabTestInfo.class);
        query.setParameter("labTestId", id);
        List<LabTestInfo> labInfo = query.getResultList();
        return labInfo;
    }

    @Override
    public int getQuestionPoint(int labTestId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select l.pointsPerQuestion from LabTest l where l.labTestId = :labTestId";
        Query query = session.createQuery(hql);
        query.setParameter("labTestId", labTestId);
        int questionPoint = (int) query.uniqueResult();
        return questionPoint;
    }

    @Override
    public List<QuestionPointsPayLoad> getQuestionListViaLabTestId(int labTestId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT lt.passPercentage, q.questionId, q.questionPoints ,q.questionDescription FROM LabTest lt " +
                "JOIN lt.questions q " +
                "WHERE lt.labTestId = :labTestId";

        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("labTestId", labTestId);
        List<Object[]> result = query.getResultList();
        List<QuestionPointsPayLoad> questionPointsList = new ArrayList<>();
        for (Object[] res : result) {
            QuestionPointsPayLoad questionPoint = new QuestionPointsPayLoad();
            questionPoint.setPassPercentage((Double) res[0]);
            questionPoint.setQuestionId((Integer) res[1]);
            questionPoint.setQuestionPoints((Integer) res[2]);
            questionPoint.setQuestionDescription((String) res[3]);
            questionPointsList.add(questionPoint);
        }
        return questionPointsList;
    }

    @Override
    public List<LabTestPayload> getLabTestsByBatch(int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select new lab.payload.labTest.LabTestPayload(l.labTestId, l.labTestName) " + "from Batch b " +
                "join b.labTests l " +
                "where b.id = :batchId";
        Query<LabTestPayload> query = session.createQuery(hql, LabTestPayload.class);
        query.setParameter("batchId", batchId);
        List<LabTestPayload> labInfo = query.getResultList();
        return labInfo;
    }

    @Override
    public List<RecordLabTestAttempt> getReportsOfTraineeByLabTestId(String traineeId, int labTestId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT ra " +
                "FROM RecordLabTestAttempt ra " +
                "WHERE ra.traineeId = :traineeId AND ra.labTestId = :labTestId";
        Query<RecordLabTestAttempt> query = session.createQuery(hql, RecordLabTestAttempt.class);
        query.setParameter("traineeId", traineeId);
        query.setParameter("labTestId", labTestId);
        List<RecordLabTestAttempt> recordLabTestAttempts = query.getResultList();
        return recordLabTestAttempts;
    }

    @Override
    public LabTestReport getLabTestReportInfoByTraniee(int labTestId, int batchId, String traineeId) {
        String hql = "SELECT li, u.name, lt.passPercentage " +
                "FROM LabTestInfo li " +
                "JOIN User u ON li.traineeId = u.employeeId " +
                "JOIN LabTest lt ON li.labTestId = lt.labTestId " +
                "WHERE li.labTestId = :labTestId AND li.batch = :batchId AND u.employeeId = :traineeId";

        Session session = sessionFactory.getCurrentSession();
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("labTestId", labTestId);
        query.setParameter("batchId", batchId);
        query.setParameter("traineeId", traineeId);
        List<Object[]> list = query.list();
        Object[] result = list.get(0);
        if (result != null) {
            LabTestReport labReport = new LabTestReport();
            labReport.setLabTestInfo((LabTestInfo) result[0]);
            labReport.setTraineeName((String) result[1]);
            labReport.setPassPercentage((Double) result[2]);
            return labReport;
        } else {
            return null;
        }
    }

    @Override
    public LabTestDto getLabTestByIdAndBatchId(int labTestId, int batchId, String labTestName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT lt FROM Batch b " +
                "JOIN b.labTests lt " +
                "WHERE b.id = :batchId AND lt.labTestName = :labTestName";
        LabTest labTest = session.createQuery(hql, LabTest.class)
                .setParameter("batchId", batchId)
                .setParameter("labTestName", labTestName)
                .uniqueResult();
        if (labTest != null) {
            return Convert.getModelMapper().toDto(labTest, LabTestDto.class);
        }
        return null;
    }

    @Override
    public List<LabTestInfo> getALlLabTestInfoByTraineeId(String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "FROM LabTestInfo lti WHERE lti.traineeId = :traineeId";
        Query<LabTestInfo> query = session.createQuery(hqlQuery, LabTestInfo.class);
        query.setParameter("traineeId", traineeId);
        return query.getResultList();
    }

    @Override
    public List<LabTestPassPercentage> getAllLabTestReportInfoByTrainee(int batchId, String traineeId) {
        String hql = "SELECT li.batch, li.labTestId, li.traineeId, li.labTestStatus, u.name, lt.passPercentage " +
                "FROM LabTestInfo li " +
                "JOIN User u ON li.traineeId = u.employeeId " +
                "JOIN LabTest lt ON li.labTestId = lt.labTestId " +
                "WHERE li.batch = :batchId AND u.employeeId = :traineeId";

        Session session = sessionFactory.getCurrentSession();
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("batchId", batchId);
        query.setParameter("traineeId", traineeId);
        List<Object[]> resultList = query.list();
        List<LabTestPassPercentage> labReports = new ArrayList<>();
        for (Object[] result : resultList) {
            LabTestPassPercentage labReport = new LabTestPassPercentage();
            labReport.setBatchId((int) result[0]);
            labReport.setLabTestId((int) result[1]);
            labReport.setTraineeId((String) result[2]);
            labReport.setLabTestStatus((LabStatus) result[3]);
            labReport.setTraineeName((String) result[4]);
            labReport.setPassPercentage((Double) result[5]);
            labReports.add(labReport);
        }
        return labReports;
    }

    @Override
    public List<TraineeLabTestReports> getReportsOfTraineeByTraineeId(String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT ra " +
                "FROM RecordLabTestAttempt ra " +
                "WHERE ra.labTestId = :labTestId";
        Query<RecordLabTestAttempt> query = session.createQuery(hql, RecordLabTestAttempt.class);
        query.setParameter("traineeId", traineeId);
//        return query.getResultList();
        return null;
    }

    @Override
    public List<TraineeMarksPayload> getTraineeMarksByTraineeIdBatchId(int batchId, String traineeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT DISTINCT NEW lab.payload.report.TraineeMarksPayload(r.labTestId, SUM(r.traineeCurrentQuestionPoints), r.traineeId) " +
                "FROM RecordLabTestAttempt r " +
                "WHERE r.traineeId = :traineeId " +
                "GROUP BY r.labTestId, r.traineeId";

        Query query = session.createQuery(hql, TraineeMarksPayload.class);
        query.setParameter("traineeId", traineeId);
        List<TraineeMarksPayload> traineeMarksPayloads = query.getResultList();
        return traineeMarksPayloads;
    }

    @Override
    public List<LabTestPayload> getLabTestsByBatchWithTotalMarks(int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT NEW lab.payload.labTest.LabTestPayload(l.labTestId, l.labTestName, SUM(q.questionPoints)) " +
                "FROM Batch b " +
                "JOIN b.labTests l " +
                "JOIN l.questions q " +
                "WHERE b.id = :batchId " +
                "GROUP BY l.labTestId, l.labTestName";

        Query<LabTestPayload> query = session.createQuery(hql, LabTestPayload.class);
        query.setParameter("batchId", batchId);
        List<LabTestPayload> labTestInfo = query.getResultList();
        return labTestInfo;
    }

    @Override
    public LabTestDto getLabTestWithoutReport(int labTestId) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String hql = "SELECT l.labTestId, l.labTestName, q.questionId, q.questionDescription, q.questionAnswer, q.questionPoints" +
                    ",l.createdDate,l.endDate, l.duration, l.startDate, l.passPercentage, l.negativeMarkingFactor" +
                    "  FROM LabTest l " +
                    "JOIN l.questions q " +
                    "WHERE l.labTestId = :labTestId";

            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("labTestId", labTestId);
            List<Object[]> results = query.getResultList();
            if (!results.isEmpty()) {
                LabTestDto labTestDto = new LabTestDto();
                labTestDto.setLabTestId((int) results.get(0)[0]);
                labTestDto.setLabTestName((String) results.get(0)[1]);
                List<QuestionDto> questions = new ArrayList<>();
                for (Object[] result : results) {
                    QuestionDto questionDto = new QuestionDto();
                    questionDto.setQuestionId((int) result[2]);
                    questionDto.setQuestionDescription((String) result[3]);
                    questionDto.setQuestionAnswer((String) result[4]);
                    questionDto.setQuestionPoints((int) result[5]);
                    questions.add(questionDto);
                }
                labTestDto.setQuestions(questions);
                labTestDto.setCreatedDate((LocalDateTime) results.get(0)[6]);
                labTestDto.setEndDate((LocalDateTime) results.get(0)[7]);
                labTestDto.setDuration((int) results.get(0)[8]);
                labTestDto.setStartDate((LocalDateTime) results.get(0)[9]);
                labTestDto.setPassPercentage((double) results.get(0)[10]);
                labTestDto.setNegativeMarkingFactor((double) results.get(0)[11]);
                return labTestDto;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public LabTestDto getLabTestSummaryById(int labTestId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT NEW lab.dto.lab.LabTestDto(l.labTestId, l.labTestName, l.createdDate, l.startDate, l.endDate, l.duration, l.passPercentage, l.attemptsPerQuestion, l.pointsPerQuestion, " +
                "l.negativeMarkingFactor, l.assignedBy) " +
                "FROM LabTest l " +
                "WHERE l.labTestId = :labTestId";

        Query<LabTestDto> query = session.createQuery(hql, LabTestDto.class);
        query.setParameter("labTestId", labTestId);

        LabTestDto labTestDto = query.uniqueResult();
        return labTestDto;
    }


    @Override
    public List<LabTestDto> getUnAttemptedLabTest(String userId) {
        int batchId = traineeRepo.getBatchIdByUserName(userId);
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "SELECT " +
                "SUM(CASE WHEN li.labTestStatus = 'UNATTEMPTED' THEN 1 ELSE 0 END) AS unattemptedLabTest, " +
                "FROM LabTestInfo li " +
                "WHERE li.batch = :batchId AND li.traineeId = :traineeId";
        Query<Integer> query = session.createQuery(hqlQuery, Integer.class);
        query.setParameter("batchId", batchId);
        query.setParameter("traineeId", userId);
        List<Integer> labTestList = query.getResultList();
        List<LabTestDto> labTestDtos = getTestsByTraineeUsername(userId);
        Iterator<LabTestDto> iterator = labTestDtos.iterator();
        while (iterator.hasNext()) {
            LabTestDto labTestDto = iterator.next();
            boolean flag = false;
            for (int labTest : labTestList) {
                if (labTestDto.getLabTestId() == labTest) {
                    flag = true;
                }
            }
            if (flag == true) iterator.remove();
        }
        return labTestDtos;
    }

    @Override
    public List<LabTestDto> getLabTestAccordingToStatus(String userId, String status) {
        Session session = sessionFactory.getCurrentSession();
        TraineeDto traineeDto = traineeRepo.getTraineeByUserName(userId);
        int batchId = traineeDto.getBatchId();
        String hqlQuery = "SELECT lt.labTestId " +
                "FROM LabTestInfo lt " +
                "WHERE lt.batch = :batchId " +
                "AND lt.traineeId = :traineeId " +
                "AND lt.labTestStatus =:status";
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
            return (List<LabTestDto>) new LabTestDto();
        }
        List<Integer> labTestList = query.getResultList();
        List<LabTestDto> labTestDtos = getTestsByTraineeUsername(userId);
        Iterator<LabTestDto> iterator = labTestDtos.iterator();
        while (iterator.hasNext()) {
            LabTestDto labTestDto = iterator.next();
            boolean flag = false;
            for (int labTest : labTestList) {
                if (labTestDto.getLabTestId() == labTest) {
                    flag = true;
                }
            }
            if (flag == false) iterator.remove();
        }
        return labTestDtos;
    }
}