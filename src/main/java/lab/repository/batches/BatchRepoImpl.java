package lab.repository.batches;

import lab.convert.Convert;
import lab.dto.admin.BatchDto;
import lab.dto.lab.LabTestDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.admin.Batch;
import lab.entity.lab.LabTest;
import lab.entity.trainee.Trainee;
import lab.payload.batch.BatchIdCode;
import lab.payload.batch.BatchInfo;
import lab.payload.batch.BatchStatusPayload;
import lab.payload.lab.LabPayload;
import lab.payload.lab.LabStatusPayload;
import lab.payload.labTest.LabTestPayload;
import lab.payload.labTest.LabTestStatusPayload;
import lab.repository.trainee.TraineeRepo;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Repository
public class BatchRepoImpl implements BatchRepo {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private TraineeRepo traineeRepo;
    private BatchIdCode batchIdCode;

    @Override
    public void addNewBatch(BatchDto batchDto) {
        Batch batch = Convert.getModelMapper().toEntity(batchDto, Batch.class);
        if (batch != null) {
            sessionFactory.getCurrentSession().save(batch);
        }
    }

    @Override
    public boolean saveBatchs(List<BatchDto> batchDtos) {
        boolean flag = false;
        List<Batch> batchList = Convert.getModelMapper().toEntityList(batchDtos, Batch.class);
        if (!batchList.isEmpty()) {
            Session session = sessionFactory.getCurrentSession();
            int batchSize = 5;
            for (int i = 0; i < batchList.size(); i++) {
                session.save(batchList.get(i));
                if (i % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
            }
            flag = true;
        }
        return flag;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateBatch(BatchDto batchDto) {
        Session session = sessionFactory.getCurrentSession();
        Batch batch = Convert.getModelMapper().toEntity(batchDto, Batch.class);
        for(LabTest labTest:batch.getLabTests()){
            labTest.setQuestions(null);
        }
        session.saveOrUpdate(batch);
    }

    @Override
    public void updateBatch1(BatchDto batchDto) {
        Session session = sessionFactory.getCurrentSession();
        Batch entity = Convert.getModelMapper().toEntity(batchDto, Batch.class);
        for (LabTest labTest : entity.getLabTests()) {
            String hql = "UPDATE LabTest " +
                    "SET labTestName = :labTestName, " +
                    "passPercentage = :passPercentage, " +
                    "startDate = :startDate, " +
                    "endDate = :endDate, " +
                    "duration = :duration, " +
                    "attemptsPerQuestion = :attemptsPerQuestion, " +
                    "assignedBy = :assignedBy " +
                    "WHERE labTestId = :labTestId";
            int updatedEntities = session.createQuery(hql)
                    .setParameter("labTestName", labTest.getLabTestName())
                    .setParameter("passPercentage", labTest.getPassPercentage())
                    .setParameter("startDate", labTest.getStartDate())
                    .setParameter("endDate", labTest.getEndDate())
                    .setParameter("duration", labTest.getDuration())
                    .setParameter("attemptsPerQuestion", labTest.getAttemptsPerQuestion())
                    .setParameter("assignedBy", labTest.getAssignedBy())
                    .setParameter("labTestId", labTest.getLabTestId())
                    .executeUpdate();
        }
    }

    @Override
    public List<BatchDto> getAllBatches() {
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "From Batch";
        Query<Batch> query = session.createQuery(hqlQuery, Batch.class);
        List<Batch> resultList = query.getResultList();
        List<BatchDto> batchDtoList = Convert.getModelMapper().toDtoList(resultList, BatchDto.class);
        return batchDtoList;
    }

    @Override
    public List<Long> getAdminBatchDetails(String userName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "Select count(case when b.enrollmentExpiryDate > current_date() then 1 end), " +
                "count(case when b.enrollmentExpiryDate <= current_date() then 1 end) " +
                "from Admin a join a.batches b " +
                "where a.employeeId = :username";
        Query<Object[]> query = session.createQuery(hql);
        query.setParameter("username", userName);
        List<Object[]> resultList = query.getResultList();
        List<Long> batchCounts = new ArrayList<>();
        if (resultList.size() > 0) {
            Long exceededCount = (Long) resultList.get(0)[0];
            Long notExceededCount = (Long) resultList.get(0)[1];
            batchCounts.add(exceededCount != null ? exceededCount : 0L);
            batchCounts.add(notExceededCount != null ? notExceededCount : 0L);
        } else {
            batchCounts.add(0L);
            batchCounts.add(0L);
        }
        return batchCounts;
    }

    @Override
    public List<Long> getAdminLabDetails(String userName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(CASE WHEN l.isAssigned = 0 THEN 1 END), " +
                "COUNT(CASE WHEN l.isAssigned = 1 THEN 1 END) " +
                "FROM Admin a JOIN a.labs l " +
                "WHERE a.employeeId = :username";

        Query<Object[]> query = session.createQuery(hql);
        query.setParameter("username", userName);

        List<Object[]> resultList = query.getResultList();
        List<Long> labCounts = new ArrayList<>();

        if (resultList.size() > 0) {
            Long notAssignedCount = (Long) resultList.get(0)[0];
            Long assignedCount = (Long) resultList.get(0)[1];

            labCounts.add(notAssignedCount != null ? notAssignedCount : 0L);
            labCounts.add(assignedCount != null ? assignedCount : 0L);
        } else {
            labCounts.add(0L);
            labCounts.add(0L);
        }

        return labCounts;
    }

    @Override
    public List<Long> getAdminLabTestDetails(String userName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "Select sum(case when lt.assignedBy is null then 1 else 0 end), " +
                "sum(case when lt.assignedBy is not null then 1 else 0 end) " +
                "from Admin a join a.labTests lt " +
                "where a.employeeId = :username";
        Query<Object[]> query = session.createQuery(hql);
        query.setParameter("username", userName);

        List<Object[]> resultList = query.getResultList();
        List<Long> labTestCounts = new ArrayList<>();

        if (resultList.size() > 0) {
            Long nullAssignedByCount = (Long) resultList.get(0)[0];
            Long notNullAssignedByCount = (Long) resultList.get(0)[1];

            labTestCounts.add(nullAssignedByCount != null ? nullAssignedByCount : 0L);
            labTestCounts.add(notNullAssignedByCount != null ? notNullAssignedByCount : 0L);
        } else {
            labTestCounts.add(0L);
            labTestCounts.add(0L);
        }

        return labTestCounts;
    }

    @Override
    public List<BatchStatusPayload> getActiveBatchs(String userName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT b.id, b.batchCode " +
                "FROM Admin a " +
                "JOIN a.batches b " +
                "WHERE a.employeeId = :username " +
                "AND b.enrollmentExpiryDate >= current_date() ";
        Query<Object[]> query = session.createQuery(hql);
        query.setParameter("username", userName);
        List<Object[]> resultList = query.getResultList();
        List<BatchStatusPayload> batchStatusPayloadList = new ArrayList<>();
        for (Object[] row : resultList) {
            BatchStatusPayload batchStatusPayload = new BatchStatusPayload();
            batchStatusPayload.setBatchId((Integer) row[0]);
            batchStatusPayload.setBatchCode((String) row[1]);
            batchStatusPayloadList.add(batchStatusPayload);
        }
        return batchStatusPayloadList;
    }

    @Override
    public List<BatchStatusPayload> getClosedBatchs(String userName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT b.id, b.batchCode " +
                "FROM Admin a " +
                "JOIN a.batches b " +
                "WHERE a.employeeId = :username " +
                "AND b.enrollmentExpiryDate < current_date() ";
        Query<Object[]> query = session.createQuery(hql);
        query.setParameter("username", userName);
        List<Object[]> resultList = query.getResultList();
        List<BatchStatusPayload> batchStatusPayloadList = new ArrayList<>();
        for (Object[] row : resultList) {
            BatchStatusPayload batchStatusPayload = new BatchStatusPayload();
            batchStatusPayload.setBatchId((Integer) row[0]);
            batchStatusPayload.setBatchCode((String) row[1]);
            batchStatusPayloadList.add(batchStatusPayload);
        }
        return batchStatusPayloadList;
    }

    @Override
    public List<LabStatusPayload> getAssignedLab(String userName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT l.labId, l.labName " +
                "FROM Admin a " +
                "JOIN a.labs l " +
                "WHERE a.employeeId = :username " +
                "AND l.isAssigned = true";
        Query<Object[]> query = session.createQuery(hql);
        query.setParameter("username", userName);

        List<Object[]> resultList = query.getResultList();
        List<LabStatusPayload> labStatusPayloadList = new ArrayList<>();

        for (Object[] row : resultList) {
            LabStatusPayload labStatusPayload = new LabStatusPayload();
            labStatusPayload.setLabId((int) row[0]);
            labStatusPayload.setLabName((String) row[1]);
            labStatusPayloadList.add(labStatusPayload);
        }
        return labStatusPayloadList;
    }

    @Override
    public List<LabStatusPayload> getUnAssignedLab(String userName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT l.labId, l.labName " +
                "FROM Admin a " +
                "JOIN a.labs l " +
                "WHERE a.employeeId = :username " +
                "AND l.isAssigned = false";
        Query<Object[]> query = session.createQuery(hql);
        query.setParameter("username", userName);

        List<Object[]> resultList = query.getResultList();
        List<LabStatusPayload> labStatusPayloadList = new ArrayList<>();

        for (Object[] row : resultList) {
            LabStatusPayload labStatusPayload = new LabStatusPayload();
            labStatusPayload.setLabId((int) row[0]);
            labStatusPayload.setLabName((String) row[1]);
            labStatusPayloadList.add(labStatusPayload);
        }
        return labStatusPayloadList;
    }

    @Override
    public List<LabTestStatusPayload> getAssignedLabTest(String userName) {
        Session session = sessionFactory.getCurrentSession();
//        String hql = "SELECT lt.labTestId, lt.labTestName " +
//                "FROM Admin a " +
//                "JOIN a.labTests lt " +
//                "WHERE a.employeeId = :username " +
//                "GROUP BY lt.labTestName " +
//                "HAVING COUNT(lt.labTestName) > 1";
        String hql = "SELECT lt.labTestId, lt.labTestName " +
                "FROM Admin a " +
                "JOIN a.labTests lt " +
                "WHERE a.employeeId = :username " +
                "AND lt.assignedBy <> null";
        Query<Object[]> query = session.createQuery(hql);
        query.setParameter("username", userName);

        List<Object[]> resultList = query.getResultList();
        List<LabTestStatusPayload> labTestStatusPayloadList = new ArrayList<>();

        for (Object[] row : resultList) {
            LabTestStatusPayload labTestStatusPayload = new LabTestStatusPayload();
            labTestStatusPayload.setLabTestId((int) row[0]);
            labTestStatusPayload.setLabTestName((String) row[1]);
            labTestStatusPayloadList.add(labTestStatusPayload);
        }

        return labTestStatusPayloadList;
    }

    @Override
    public List<LabTestStatusPayload> getUnAssignedLabTest(String userName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT lt.labTestId, lt.labTestName " +
                "FROM Admin a " +
                "JOIN a.labTests lt " +
                "WHERE a.employeeId = :username " +
                "AND lt.assignedBy = null";
        Query<Object[]> query = session.createQuery(hql);
        query.setParameter("username", userName);
        List<Object[]> resultList = query.getResultList();
        List<LabTestStatusPayload> labTestStatusPayloadList = new ArrayList<>();
        for (Object[] row : resultList) {
            LabTestStatusPayload labTestStatusPayload = new LabTestStatusPayload();
            labTestStatusPayload.setLabTestId((int) row[0]);
            labTestStatusPayload.setLabTestName((String) row[1]);
            labTestStatusPayloadList.add(labTestStatusPayload);
        }
        return labTestStatusPayloadList;
    }

    @Override
    public BatchDto createBatch(BatchDto batchDto, String userId) {
        Session session = sessionFactory.getCurrentSession();
        Batch batch = Convert.getModelMapper().toEntity(batchDto, Batch.class);
        if (batch != null) {
            session.save(batch);
        }
        batchDto = Convert.getModelMapper().toDto(batch, BatchDto.class);
        return batchDto;
    }

    @Override
    public void insertBatchViaNativeSQL(Long adminId, int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String nativeSQLInsert = "INSERT INTO nSbt_sql_17239_sql_batch_17239 (ADMIN_ID, BATCHES_ID) " +
                "VALUES (:adminId, :batchId)";
        SQLQuery query = session.createSQLQuery(nativeSQLInsert);
        query.setParameter("adminId", adminId);
        query.setParameter("batchId", batchId);
        int rowsAffected = query.executeUpdate();
    }

    @Override
    public Long getAdminIdViaEmployeeId(String employeeId) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "SELECT a.id FROM Admin a WHERE a.employeeId = :employeeId";
        Query<Long> query = session.createQuery(hql, Long.class)
                .setParameter("employeeId", employeeId);
        Long adminId = query.uniqueResult();
        return adminId != null ? adminId : 0L;
    }

    @Override
    public List<BatchInfo> getBatchInfoWithCounts() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT b.id, " +
                "       b.batchCode, " +
                "       (SELECT COUNT(DISTINCT t.id) FROM Batch b1 JOIN b1.trainees t WHERE b1.id = b.id) AS totalTrainees, " +
                "       (SELECT COUNT(DISTINCT l.id) FROM Batch b2 JOIN b2.labs l WHERE b2.id = b.id) AS totalLabs, " +
                "       (SELECT COUNT(DISTINCT lt.id) FROM Batch b3 JOIN b3.labTests lt WHERE b3.id = b.id) AS totalLabTests " +
                "FROM Batch b";

        Query<Object[]> query = session.createQuery(hql, Object[].class);

        List<Object[]> resultList = query.getResultList();
        List<BatchInfo> batchInfoList = new ArrayList<>();

        for (Object[] result : resultList) {
            BatchInfo batchInfo = new BatchInfo();
            batchInfo.setId(((Integer) result[0]));
            batchInfo.setBatchCode((String) result[1]);
            batchInfo.setTotalTrainees(((Long) result[2]).intValue());
            batchInfo.setTotalLabs(((Long) result[3]).intValue());
            batchInfo.setTotalLabTests(((Long) result[4]).intValue());
            batchInfoList.add(batchInfo);
        }
        return batchInfoList;
    }

    @Override
    public List<LabPayload> getLabUsingBatchId(int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT l.labId AS labId, l.labName AS labName FROM Batch b " +
                "JOIN b.labs l " +
                "WHERE b.id = :batchId";
        Query<Object[]> query = session.createQuery(hql)
                .setParameter("batchId", batchId);
        List<Object[]> results = query.getResultList();
        List<LabPayload> labPayloads = new ArrayList<>();
        for (Object[] result : results) {
            int labId = (int) result[0];
            String labName = (String) result[1];
            LabPayload labPayload = new LabPayload();
            labPayload.setLabId(labId);
            labPayload.setLabName(labName);
            labPayloads.add(labPayload);
        }
        return labPayloads;
    }

    @Override
    public List<LabTestPayload> getLabTestUsingBatchId(int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT l.labTestId AS labTestId, l.labTestName AS labTestName FROM Batch b " +
                "JOIN b.labTests l " +
                "WHERE b.id = :batchId";
        Query<Object[]> query = session.createQuery(hql)
                .setParameter("batchId", batchId);
        List<Object[]> results = query.getResultList();
        List<LabTestPayload> labTestPayloads = new ArrayList<>();
        for (Object[] result : results) {
            int labTestId = (int) result[0];
            String labTestName = (String) result[1];
            LabTestPayload labTestPayload = new LabTestPayload();
            labTestPayload.setLabTestId(labTestId);
            labTestPayload.setLabTestName(labTestName);
            labTestPayloads.add(labTestPayload);
        }
        return labTestPayloads;
    }

    @Override
    public BatchInfo getBatchInfoById(int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT b.batchCode, " +
                "       (SELECT COUNT(DISTINCT t.id) FROM Batch b1 JOIN b1.trainees t WHERE b1.id = :batchId), " +
                "       (SELECT COUNT(DISTINCT l.id) FROM Batch b2 JOIN b2.labs l WHERE b2.id = :batchId), " +
                "       (SELECT COUNT(DISTINCT lt.id) FROM Batch b3 JOIN b3.labTests lt WHERE b3.id = :batchId) " +
                "FROM Batch b " +
                "WHERE b.id = :batchId";
        Query<Object[]> query = session.createQuery(hql)
                .setParameter("batchId", batchId);
        Object[] result = query.uniqueResult();
        if (result != null) {
            BatchInfo batchInfo = new BatchInfo();
            batchInfo.setBatchCode((String) result[0]);
            batchInfo.setTotalTrainees(((Long) result[1]).intValue());
            batchInfo.setTotalLabs(((Long) result[2]).intValue());
            batchInfo.setTotalLabTests(((Long) result[3]).intValue());
            return batchInfo;
        }
        return null;
    }

    @Override
    public List<BatchIdCode> getAllBatchIdCode() {
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "select new lab.payload.batch.BatchIdCode(b.id, b.batchCode) from Batch b";
        Query<BatchIdCode> query = session.createQuery(hqlQuery, BatchIdCode.class);
        return query.getResultList();
    }
    @Override
    public List<BatchIdCode> getAllBatchIdCodeActive() {
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "select new lab.payload.batch.BatchIdCode(b.id, b.batchCode) from Batch b " +
                "where b.enrollmentDate <= current_date() and b.enrollmentExpiryDate >= current_date()";
        Query<BatchIdCode> query = session.createQuery(hqlQuery, BatchIdCode.class);
        return query.getResultList();
    }

    @Override
    public List<BatchIdCode> getBatchsIdBatchCode(String userName) {
        return null;
    }

    @Override
    public List<BatchStatusPayload> getClosedAllBatchs() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT b.id, b.batchCode " +
                "FROM Admin a " +
                "JOIN a.batches b " +
                "WHERE " +
                " b.enrollmentExpiryDate < current_date() ";
        Query<Object[]> query = session.createQuery(hql);
        List<Object[]> resultList = query.getResultList();
        List<BatchStatusPayload> batchStatusPayloadList = new ArrayList<>();
        for (Object[] row : resultList) {
            BatchStatusPayload batchStatusPayload = new BatchStatusPayload();
            batchStatusPayload.setBatchId((Integer) row[0]);
            batchStatusPayload.setBatchCode((String) row[1]);
            batchStatusPayloadList.add(batchStatusPayload);
        }
        return batchStatusPayloadList;
    }

    @Override
    public BatchIdCode getBatchIdCodeById(int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hqlQuery = "select new lab.payload.batch.BatchIdCode(b.id, b.batchCode) from Batch b " +
                "where b.id = :batchId"; // Added space before "where" and adjusted parameter syntax
        Query<BatchIdCode> query = session.createQuery(hqlQuery, BatchIdCode.class)
                .setParameter("batchId", batchId);
        return query.uniqueResult();
    }


    //    @Override
    public void updateLabTestByLabTestIdBatchId(LabTestDto labTestDto, int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "UPDATE LabTest lt " +
                "SET lt.labTestName = :labTestName, " +
                "lt.passPercentage = :passPercentage, " +
                "lt.startDate = :startDate, " +
                "lt.endDate = :endDate, " +
                "lt.duration = :duration, " +
                "lt.attemptsPerQuestion = :attemptsPerQuestion, " +
                "lt.assignedBy = :assignedBy " +
                "WHERE lt IN (SELECT DISTINCT lt FROM LabTest lt " +
                "JOIN lt.labTests bt " +
                "WHERE bt = :batch)";
//        String hql = "UPDATE LabTest lt " +
//                "SET lt.labTestName = :newLabTestName " +
//                "WHERE lt.labTestId = :labTestId " +
//                "AND :batchId IN (SELECT b.id FROM Batch b JOIN b.labTests lt WHERE lt.labTestId = :labTestId)";
//
//        String hql2="UPDATE LabTest " +
//                "SET labTestName = :labTestName, " +
//                "passPercentage = :passPercentage, " +
//                "startDate = :startDate, " +
//                "endDate = :endDate, " +
//                "duration = :duration, " +
//                "attemptsPerQuestion = :attemptsPerQuestion, " +
//                "assignedBy = :assignedBy " +
//                "WHERE labTestId = :labTestId";

//        int updatedEntities = session.createQuery(hql)
//                .setParameter("newLabTestName", newLabTestName)
//                .setParameter("labTestId", labTestId)
//                .setParameter("batchId", batchId)
//                .executeUpdate();
    }

    @Override
    public BatchIdCode getBatchIdCodeByTrainee(String employeeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Trainee WHERE employeeId = :employeeId";

        Query<Trainee> query = session.createQuery(hql, Trainee.class);
        query.setParameter("employeeId", employeeId);
        BatchIdCode batchIdCode =new BatchIdCode();
        Trainee trainee = query.uniqueResult();
        batchIdCode.setId(trainee.getBatchId());
        return batchIdCode;

    }

    @Override
    public List<BatchStatusPayload> getActiveAllBatchs() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT b.id, b.batchCode " +
                "FROM Batch b " +
                "WHERE b.enrollmentExpiryDate >= current_date() ";
        Query<Object[]> query = session.createQuery(hql);
        List<Object[]> resultList = query.getResultList();
        List<BatchStatusPayload> batchStatusPayloadList = new ArrayList<>();
        for (Object[] row : resultList) {
            BatchStatusPayload batchStatusPayload = new BatchStatusPayload();
            batchStatusPayload.setBatchId((Integer) row[0]);
            batchStatusPayload.setBatchCode((String) row[1]);
            batchStatusPayloadList.add(batchStatusPayload);
        }
        return batchStatusPayloadList;
    }

    @Override
    public List<TraineeDto> getTraineeByBatchId(int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT t FROM Batch b JOIN b.trainees t " +
                "WHERE b.id = :batchId";

        Query<Trainee> query = session.createQuery(hql, Trainee.class)
                .setParameter("batchId", batchId);
        List<Trainee> traineeList = query.getResultList();
        List<TraineeDto> traineeDtoList = Convert.getModelMapper().toDtoList(traineeList, TraineeDto.class);
        return traineeDtoList;
    }

    @Override
    public BatchDto createBatchById(BatchDto batchDto, String userName) {
        return null;
    }


    @Override
    public BatchDto getBatchById(int batchId) {
        Session session = sessionFactory.getCurrentSession();
        Batch batch = session.get(Batch.class, batchId);
        BatchDto batchDto = Convert.getModelMapper().toDto(batch, BatchDto.class);
        session.evict(batch);
        session.clear();
        return batchDto;
    }


}
