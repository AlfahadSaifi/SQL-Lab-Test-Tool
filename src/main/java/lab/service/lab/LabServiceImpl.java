package lab.service.lab;

import lab.dto.admin.AdminDto;
import lab.dto.evaluation.LabReportsDto;
import lab.dto.evaluation.TraineeLabReportsDto;
import lab.dto.lab.LabDto;
import lab.entity.evaluation.RecordLabAttempt;
import lab.entity.lab.LabInfo;
import lab.entity.lab.LabStatus;
import lab.exceptionHandler.CustomDatabaseException;
import lab.payload.lab.LabDetail;
import lab.payload.lab.LabQuestion;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.lab.LabInfoIdBatch;
import lab.payload.lab.LabPayload;
import lab.payload.lab.LabSummary;
import lab.payload.report.LabReport;
import lab.payload.report.TraineeLabMarksPayload;
import lab.repository.admin.AdminRepo;
import lab.repository.lab.LabRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class LabServiceImpl implements LabService{
    @Autowired
    private LabRepo labRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Override
    public LabDto addLab(LabDto labDto, String username) throws CustomDatabaseException {
        AdminDto adminDto = adminRepo.getAdminByUserName(username);
        List<LabDto> labList =  adminDto.getLabs();
        List<LabReportsDto> reportsDtos = new ArrayList<>();
        labDto.setReports(reportsDtos);
        labList.add(labDto);
        adminDto.setLabs(labList);
        try {
            adminDto = adminRepo.updateAdmin(adminDto);
            int i = adminDto.getLabTests().size()-1;
            labDto = adminDto.getLabs().get(Math.max(i, 0));
        } catch (Exception e) {
        }
        return labDto;
    }

    @Override
    public List<LabDto> getLabs(String adminUsername) {
        List<LabDto> labDtoList = adminRepo.getLabDtoList(adminUsername);
        return labDtoList;
    }

    @Override
    public List<LabPayload> getTraineeLab(String userName) {
        return labRepo.getLabsByTraineeUsername(userName);
    }

    @Override
    public LabDto getLabById(int labId) {
        return labRepo.getLab(labId);
    }

    @Override
    public LabReportsDto getReports(int labId, int batchId) {
        LabDto labDto = getLabById(labId);
        List<LabReportsDto> reportsDtoLab = labDto.getReports();
        for (LabReportsDto reports:reportsDtoLab) {
            if(reports.getBatchId()==batchId){
                return reports;
            }
        }
        return new LabReportsDto();
    }

    @Override
    public int getQuestionCount(int labId) {
        LabDto labDto = getLabById(labId);
        return labDto.getQuestions().size();
    }

    @Override
    public List<TraineeLabReportsDto> getLabReports(int labId, int batchId) {
        LabReportsDto reportsDto = getReports(labId,batchId);
        return reportsDto.getTraineeLabReports();
    }

//    @Override
//    public void addReferenceFile(MultipartFile file, int labId) {
//        LabDto labDto = labRepo.getLab(labId);
//        byte[] referenceFileBytes;
//        try {
//            referenceFileBytes = file.getBytes();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        labDto.setReferenceFile(referenceFileBytes);
//        labRepo.updateLab(labDto);
//    }

    @Override
    public LabDetail getLabData(String traineeId, int batchId) {
        return labRepo.getLabData(traineeId,batchId);
    }
    @Override
    public LabQuestion getLabQuestionData(int batchId, String traineeId) {
        //int noOfQuestionInLabs = labRepo.getNoOfQuestionInLabs(traineeId);
        LabQuestion labQuestionData = labRepo.getLabQuestionData(traineeId);
        //int unattemptedLabs=noOfQuestionInLabs-(labQuestionData.getCorrectLabQuestion()+labQuestionData.getIncorrectLabQuestion());
        //labQuestionData.setUnattemptedLabQuestion(unattemptedLabs);
        return labQuestionData;
    }

    @Override
    public List<LabPayload> getIncompleteLab(String userId) {
        return labRepo.getIncompleteLab(userId);
    }

    @Override
    public List<LabPayload> getCompleteLab(String userId) {
        return labRepo.getCompleteLab(userId);
    }

    @Override
    public List<RecordLabAttempt> getLabReport(int labId, String traineeId) {
        return labRepo.getLabReport(labId,traineeId);
    }

    @Override
    public LabInfo getLabInfo(int labId, String traineeId, int batchId) {
        return labRepo.getLabInfo(labId,traineeId, batchId);
    }

    @Override
    public void addLabInfo(LabInfo labInfo) {
        labRepo.addLabInfo(labInfo);
    }

    @Override
    public List<LabDto> getAllLabData() {
        return labRepo.getAllLab();
    }

    @Override
    public int totalQuestion(int labId) {
        return labRepo.totalQuestion(labId);
    }

    @Override
    public List<RecordLabAttempt> getLabReport(int labId) {
        return labRepo.getLabReport(labId);
    }

    @Override
    public void updateLabInfo(int labId, String userId, int batchId) {
        LabInfo labInfo = labRepo.getLabInfo(labId,userId,batchId);
        labInfo.setLabStatus(LabStatus.COMPLETED);
        labRepo.updateLabInfo(labInfo);
    }

    @Override
    public List<LabPayload> getTraineeLabs(String userId, String status) {
        return labRepo.getTraineeLabs(userId,status);
    }

//    @Override
//    public void addReferenceFileRest(byte[] file, int labId) {
//        LabDto lab = labRepo.getLab(labId);
//        lab.setReferenceFile(file);
//        labRepo.updateLab(lab);
//    }

    @Override
    public int getQuestionPoint(int labId) {
        return labRepo.getQuestionPoint(labId);
    }

    @Override
    public void addDefaultLabInfo(int labId, int batchId, List<String> traineeIdList) {
        List<LabInfo> labInfoList = new ArrayList<>();
        for(String traineeId:traineeIdList){
            LabInfo labInfo = new LabInfo();
            labInfo.setLabId(labId);
            labInfo.setBatch(batchId);
            labInfo.setTraineeId(traineeId);
            labInfo.setLabStatus(LabStatus.UNATTEMPTED);
            labInfoList.add(labInfo);
        }
        labRepo.addLabInfoList(labInfoList);
    }

    @Override
    public List<LabInfo> getLabInfo(int labId, int batchId) {
        return labRepo.getLabInfoList(labId,batchId);
    }

    @Override
    public List<LabInfo> getLabInfo(int id) {
        return labRepo.getLabInfoList(id);
    }

    @Override
    public List<LabReport> getLabReportInfo(int labId, int batchId) {
        return labRepo.getLabReportInfo(labId,batchId);
    }

    @Override
    public List<QuestionPointsPayLoad> getQuestionListViaLabId(int labId) {
        return labRepo.getQuestionListViaLabId(labId);
    }

    @Override
    public List<LabInfoIdBatch> getLabInfoIdBatch(int labId) {
        return labRepo.getLabInfoIdBatch(labId);
    }

    @Override
    public List<LabSummary> getLabByIdBatch(int labId, int batchId) {
        return labRepo.getLabByIdBatch(labId,batchId);
    }

    @Override
    public List<LabPayload> getLabByBatch(int batchId) {
        return labRepo.getLabByBatch(batchId);
    }

    @Override
    public LabReport getLabReportInfoByTraniee(int labId, int batchId, String traineeId) {
        return labRepo.getLabReportInfoByTraniee(labId, batchId, traineeId);
    }

    @Override
    public List<RecordLabAttempt> getReportsOfTraineeByLabId(String traineeId, int labId) {
        return labRepo.getReportsOfTraineeByLabId(traineeId,labId);
    }

    @Override
    public LabPayload getLabPayloadById(int labId) {
        return labRepo.getLabPayloadById(labId);
    }

    @Override
    public List<LabInfo> getAllLabInfoByTrainee(String traineeId) {
        return labRepo.getAllLabInfoByTrainee(traineeId);
    }

    @Override
    public List<LabPayload> getLabByBatchWithTotalMarks(int batchId) {
        return labRepo.getLabByBatchWithTotalMarks(batchId);
    }

    @Override
    public List<TraineeLabMarksPayload> getTraineeMarksByTraineeIdBatchId(int batchId, String traineeId) {
        return labRepo.getTraineeMarksByTraineeIdBatchId(batchId,traineeId);
    }

    @Override
    public boolean deleteLab(int labId, String userName) {
        adminRepo.removeLab(labId, userName);
        return true;
    }

    @Override
    public boolean deleteLabTest(int labTestId, String userName) {
        adminRepo.removeLabTest(labTestId, userName);
        return true;
    }

    @Override
    public LabDto getLabWithoutReport(int labId) {
        return labRepo.getLabWithoutReport(labId);
    }

    @Override
    public LabDto addLabToAdmin(LabDto labDto, String userName) {
        return adminRepo.addLabToAdmin(labDto, userName);
    }

}
