package lab.service.batches;

import lab.dto.admin.BatchDto;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.trainee.TraineeDto;
import lab.exceptionHandler.CustomDatabaseException;
import lab.payload.lab.LabPayload;
import lab.payload.lab.LabStatusPayload;
import lab.payload.labTest.LabTestPayload;
import lab.payload.batch.BatchIdCode;
import lab.payload.batch.BatchStatusPayload;
import lab.payload.lab.LabStatusPayload;
import lab.payload.labTest.LabTestStatusPayload;
import lab.payload.batch.BatchInfo;
import lab.payload.labTest.LabTestStatusPayload;

import java.util.List;
import java.time.LocalDateTime;

public interface BatchService {

    boolean addBatch(BatchDto batchDto, String userName) throws CustomDatabaseException;

    List<BatchDto> getBatchs(String userName);

    void assignBatch(int labId, int id);

    List<LabDto> getAssignBatchById(int batchId);

    BatchDto getBatchs(int batchId);

    void deassignTestToBatch(int labId, int id, String deassignedBy, String reason);

    void assignTestToBatch(int labTestId, int id, LocalDateTime startdate, LocalDateTime endDate, int duration, double percentage, double negativeMarkingFactor, String assignedBy);

    List<BatchDto> getAllBatch();

    List<Long> getAdminBatchDetails(String userName);
    List<Long> getAdminLabDetails(String userName);

    List<Long> getAdminLabTestDetails(String userName);

    List<BatchStatusPayload> getActiveBatchs(String userName);

    List<BatchStatusPayload> getClosedBatchs(String userName);

    List<LabStatusPayload> getAssignedLab(String userName);

    List<LabStatusPayload> getUnAssignedLab(String userName);

    List<LabTestStatusPayload> getAssignedLabTest(String userName);

    List<LabTestStatusPayload> getUnAssignedLabTest(String userName);

    List<LabTestDto> getAssignTestBatchById(int id);

    void editassignTestToBatch(int labTestId, int batchId, LocalDateTime parsedStartDate, LocalDateTime parsedEndDate, int duration, double percentage, double negativeMarkingFactor, String assignedBy);

    BatchDto createBatch(BatchDto batchDto, String userId);

    List<BatchStatusPayload> getActiveAllBatchs();

    List<TraineeDto> getTraineeByBatchId(int batchId);

    BatchDto createBatchById(BatchDto batchDto, String userName);

    List<BatchInfo> getBatchInfo();

    List<LabPayload> getLabUsingBatchId(int batchId);

    List<LabTestPayload> getLabTestUsingBatchId(int batchId);

    BatchInfo getBatchInfoById(int batchId);

    List<BatchIdCode> getAllBatchIdCode();

    List<BatchIdCode> getBatchsIdBatchCode(String userName);
    List<BatchStatusPayload> getClosedAllBatchs();

    BatchIdCode getBatchIdCodeById(int batchId);

    BatchIdCode getBatchIdCodeByTrainee(String userId);

    void updateBatch(BatchDto batchDto, String userId);

    List<BatchIdCode> getAllBatchIdCodeActive();
}


