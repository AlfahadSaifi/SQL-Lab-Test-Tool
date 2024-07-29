package lab.service.batches;

import lab.dto.admin.AdminDto;
import lab.dto.admin.BatchDto;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.trainee.TraineeDto;
import lab.exceptionHandler.CustomDatabaseException;
import lab.payload.lab.LabStatusPayload;
import lab.payload.labTest.LabTestStatusPayload;
import lab.payload.batch.BatchIdCode;
import lab.payload.batch.BatchInfo;
import lab.payload.batch.BatchStatusPayload;
import lab.payload.lab.LabPayload;
import lab.payload.labTest.LabTestPayload;
import lab.repository.admin.AdminRepo;
import lab.repository.batches.BatchRepo;
import lab.repository.lab.LabRepo;
import lab.repository.labtest.LabTestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BatchServiceImpl implements BatchService {
    @Autowired
    private BatchRepo batchRepo;

    @Autowired
    private LabRepo labRepo;

    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private LabTestRepo labTestRepo;


    private boolean addBatchCopy(BatchDto batchDto, String userName) throws CustomDatabaseException {
        AdminDto adminDto = adminRepo.getAdminByUserName(userName);
        List<BatchDto> batchList = adminDto.getBatches();
        batchList.add(batchDto);
        adminDto.setBatches(batchList);
        try {
            adminRepo.updateAdmin(adminDto);
        } catch (CustomDatabaseException | DataIntegrityViolationException e) {
            throw new CustomDatabaseException("Batch Already Exist.", e);
        } catch (Exception e) {
            throw new CustomDatabaseException("Something went wrong");
        }
        return false;
    }

    @Override
    public boolean addBatch(BatchDto batchDto, String userName) throws CustomDatabaseException {
        return this.addBatchCopy(batchDto, userName);
    }

    @Override
    public List<BatchDto> getBatchs(String userName) {
        return adminRepo.getBatchDtoList(userName);
    }

    @Override
    public void assignBatch(int labId, int id) {
        BatchDto batchDto = batchRepo.getBatchById(id);
        LabDto labDto = labRepo.getLab(labId);
        labDto.setAssigned(true);
        List<LabDto> labDtoList = batchDto.getLabs();
        labDtoList.add(labDto);
        batchDto.setLabs(labDtoList);
        batchRepo.updateBatch(batchDto);
    }

    @Override
    public List<LabDto> getAssignBatchById(int batchId) {
        return batchRepo.getBatchById(batchId).getLabs();
    }

    @Override
    public BatchDto getBatchs(int batchId) {
        return batchRepo.getBatchById(batchId);
    }

    @Override
    public void deassignTestToBatch(int labId, int id, String deassignedBy, String reason) {
        BatchDto batchDto = batchRepo.getBatchById(id);
        batchRepo.updateBatch(batchDto);
    }

    @Override
    public void assignTestToBatch(int labTestId, int batchId, LocalDateTime startDate, LocalDateTime endDate, int duration, double percentage, double negativeMarkingFactor, String assignedBy) {
        BatchDto batchDto = batchRepo.getBatchById(batchId);
        if (batchDto.getLabTests() == null) {
            batchDto.setLabTests(new ArrayList<>());
        }
        LabTestDto labTestDto = labTestRepo.getTest(labTestId);
        System.out.println("Getting Assessment -->"+labTestDto);
        labTestDto.setAssignedBy(assignedBy);
//        labTestRepo.updateLabTest(labTestDto);
        labTestDto.setStartDate(startDate);
        labTestDto.setEndDate(endDate);
        labTestDto.setDuration(duration);
        labTestDto.setPassPercentage(percentage);
//        labTestDto.setLabTestId(0);
        labTestDto.setNegativeMarkingFactor(negativeMarkingFactor);
        List<LabTestDto> labTestDtoList = batchDto.getLabTests();
        System.out.println("Getting all Assessments for batch "+batchDto +"\nLabtests--->  "+labTestDtoList);
        labTestDtoList.add(labTestDto);
        batchDto.setLabTests(labTestDtoList);
        System.out.println("Updating batch");
        batchRepo.updateBatch(batchDto);
        System.out.println("Function exit");
    }

    @Override
    public List<BatchDto> getAllBatch() {
        return batchRepo.getAllBatches();
    }

    @Override
    public List<Long> getAdminBatchDetails(String userName) {
        return batchRepo.getAdminBatchDetails(userName);
    }

    @Override
    public List<Long> getAdminLabDetails(String userName) {
        return batchRepo.getAdminLabDetails(userName);
    }

    @Override
    public List<Long> getAdminLabTestDetails(String userName) {
        return batchRepo.getAdminLabTestDetails(userName);
    }

    @Override
    public List<BatchStatusPayload> getActiveBatchs(String userName) {
        return batchRepo.getActiveBatchs(userName);
    }

    @Override
    public List<BatchStatusPayload> getClosedBatchs(String userName) {
        return batchRepo.getClosedBatchs(userName);
    }

    @Override
    public List<LabStatusPayload> getAssignedLab(String userName) {
        return batchRepo.getAssignedLab(userName);
    }

    @Override
    public List<LabStatusPayload> getUnAssignedLab(String userName) {
        return batchRepo.getUnAssignedLab(userName);
    }

    @Override
    public List<LabTestStatusPayload> getAssignedLabTest(String userName) {
        return batchRepo.getAssignedLabTest(userName);
    }

    @Override
    public List<LabTestStatusPayload> getUnAssignedLabTest(String userName) {
        return batchRepo.getUnAssignedLabTest(userName);
    }

    @Override
    public List<LabTestDto> getAssignTestBatchById(int id) {
        return batchRepo.getBatchById(id).getLabTests();
    }

    @Override
    public void editassignTestToBatch(int labTestId, int batchId, LocalDateTime startDate, LocalDateTime endDate, int duration, double percentage, double negativeMarkingFactor, String assignedBy) {
        BatchDto batchDto = batchRepo.getBatchById(batchId);
        if (batchDto.getLabTests() == null) {
            batchDto.setLabTests(new ArrayList<>());
        }
        LabTestDto labTestDto = labTestRepo.getTest(labTestId);
        labTestDto.setStartDate(startDate);
        labTestDto.setEndDate(endDate);
        labTestDto.setDuration(duration);
        labTestDto.setPassPercentage(percentage);
        labTestDto.setNegativeMarkingFactor(negativeMarkingFactor);
        labTestDto.setAssignedBy(assignedBy);
        List<LabTestDto> labTestDtoList = batchDto.getLabTests();
        labTestDtoList.add(labTestDto);
        batchDto.setLabTests(labTestDtoList);
//        batchRepo.updateLabTestByLabTestIdBatchId(labTestDto,batchId);
        batchRepo.updateBatch1(batchDto);
    }

    @Override
    public BatchDto createBatch(BatchDto batchDto, String employeeId) {
        BatchDto batchDto1 = batchRepo.createBatch(batchDto, employeeId);
        Long adminId = batchRepo.getAdminIdViaEmployeeId(employeeId);
        int batchId = batchDto1.getId();
        batchRepo.insertBatchViaNativeSQL(adminId, batchId);
        return batchDto1;
    }

    @Override
    public List<BatchStatusPayload> getActiveAllBatchs() {
        return batchRepo.getActiveAllBatchs();
    }

    @Override
    public List<TraineeDto> getTraineeByBatchId(int batchId) {
        return batchRepo.getTraineeByBatchId(batchId);
    }

    @Override
    public BatchDto createBatchById(BatchDto batchDto, String userName) {
        return batchRepo.createBatchById(batchDto, userName);
    }

    @Override
    public List<BatchInfo> getBatchInfo() {
        return batchRepo.getBatchInfoWithCounts();
    }

    @Override
    public List<LabPayload> getLabUsingBatchId(int batchId) {
        return batchRepo.getLabUsingBatchId(batchId);
    }

    @Override
    public List<LabTestPayload> getLabTestUsingBatchId(int batchId) {
        return batchRepo.getLabTestUsingBatchId(batchId);
    }

    @Override
    public BatchInfo getBatchInfoById(int batchId) {
        return batchRepo.getBatchInfoById(batchId);
    }

    @Override
    public List<BatchIdCode> getAllBatchIdCode() {
        return batchRepo.getAllBatchIdCode();
    }

    @Override
    public List<BatchIdCode> getBatchsIdBatchCode(String userName) {
        return batchRepo.getBatchsIdBatchCode(userName);
    }

    @Override
    public List<BatchStatusPayload> getClosedAllBatchs() {
        return batchRepo.getClosedAllBatchs();
    }

    @Override
    public BatchIdCode getBatchIdCodeById(int batchId) {
        return batchRepo.getBatchIdCodeById(batchId);
    }

    @Override
    public BatchIdCode getBatchIdCodeByTrainee(String employeeId) {
        return batchRepo.getBatchIdCodeByTrainee(employeeId);
    }

    @Override
    public void updateBatch(BatchDto batchDto, String userId) {
        batchRepo.updateBatch(batchDto);
    }

    @Override
    public List<BatchIdCode> getAllBatchIdCodeActive() {
        return batchRepo.getAllBatchIdCodeActive();
    }
}
