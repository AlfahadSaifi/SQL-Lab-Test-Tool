package lab.repository.batches;

import lab.dto.admin.BatchDto;
import lab.dto.lab.LabTestDto;
import lab.dto.trainee.TraineeDto;
import lab.payload.lab.LabPayload;
import lab.payload.lab.LabStatusPayload;
import lab.payload.labTest.LabTestPayload;
import lab.payload.batch.BatchIdCode;
import lab.payload.batch.BatchInfo;
import lab.payload.batch.BatchStatusPayload;
import lab.payload.lab.LabStatusPayload;
import lab.payload.labTest.LabTestStatusPayload;
import lab.payload.labTest.LabTestStatusPayload;

import java.util.List;

public interface BatchRepo {
    void addNewBatch(BatchDto batchDto);
    void updateBatch(BatchDto batchDto);
    BatchDto getBatchById(int batchId);
    boolean saveBatchs(List<BatchDto> batchDtos);

    void updateBatch1(BatchDto batchDto);

    List<BatchDto> getAllBatches();

    List<Long> getAdminBatchDetails(String userName);
    List<Long> getAdminLabDetails(String userName);

    List<Long> getAdminLabTestDetails(String userName);

    List<BatchStatusPayload> getActiveBatchs(String userName);

    List<BatchStatusPayload> getClosedBatchs(String userName);

    List<LabStatusPayload> getAssignedLab(String userName);

    List<LabStatusPayload> getUnAssignedLab(String userName);

    List<LabTestStatusPayload> getAssignedLabTest(String userName);

    List<LabTestStatusPayload> getUnAssignedLabTest(String userName);

    BatchDto createBatch(BatchDto batchDto, String userId);

    List<BatchStatusPayload> getActiveAllBatchs();

    List<TraineeDto> getTraineeByBatchId(int batchId);

    BatchDto createBatchById(BatchDto batchDto, String userName);

    void insertBatchViaNativeSQL(Long adminId, int batchId);

    Long getAdminIdViaEmployeeId(String employeeId);

    List<BatchInfo> getBatchInfoWithCounts();

    List<LabPayload> getLabUsingBatchId(int batchId);

    List<LabTestPayload> getLabTestUsingBatchId(int batchId);

    BatchInfo getBatchInfoById(int batchId);

    List<BatchIdCode> getAllBatchIdCode();

    List<BatchIdCode> getBatchsIdBatchCode(String userName);

    List<BatchStatusPayload> getClosedAllBatchs();

    BatchIdCode getBatchIdCodeById(int batchId);

    void updateLabTestByLabTestIdBatchId(LabTestDto labTestDto, int batchId);

    BatchIdCode getBatchIdCodeByTrainee(String employeeId);

    List<BatchIdCode> getAllBatchIdCodeActive();


//    long getBatchIdByUsername(String username);
}
