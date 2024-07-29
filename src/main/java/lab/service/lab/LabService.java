package lab.service.lab;


import lab.dto.evaluation.LabReportsDto;
import lab.dto.evaluation.TraineeLabReportsDto;
import lab.dto.lab.LabDto;
import lab.entity.evaluation.RecordLabAttempt;
import lab.entity.lab.LabInfo;
import lab.exceptionHandler.CustomDatabaseException;
import lab.payload.lab.LabQuestion;
import lab.payload.lab.LabDetail;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.lab.*;
import lab.payload.report.LabReport;
import lab.payload.report.TraineeLabMarksPayload;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LabService {
    LabDto addLab(LabDto labDto, String username) throws CustomDatabaseException;
    List<LabDto> getLabs(String adminUsername);
    List<LabPayload> getTraineeLab(String userName);
    LabDto getLabById(int labId);
    LabReportsDto getReports(int labId, int batchId);

    List<TraineeLabReportsDto> getLabReports(int labId, int batchId);

//    void addReferenceFile(MultipartFile file, int labId);

    int getQuestionCount(int labId);

    LabDetail getLabData(String traineeId, int batchId);
    public LabQuestion getLabQuestionData(int batchId, String traineeId);

    List<LabPayload> getIncompleteLab(String userId);

    List<LabPayload> getCompleteLab(String userId);

    List<RecordLabAttempt> getLabReport(int labId, String traineeId);

    LabInfo getLabInfo(int labId, String traineeId, int batchId);

    void addLabInfo(LabInfo labInfo);

    List<LabDto> getAllLabData();

     int totalQuestion(int labId);

    List<RecordLabAttempt> getLabReport(int labId);

    void updateLabInfo(int labId, String userId, int batchId);

    List<LabPayload> getTraineeLabs(String userId, String status);

//    void addReferenceFileRest(byte[] file, int labId);

    int getQuestionPoint(int labId);

    void addDefaultLabInfo(int labId, int batchId, List<String> traineeIdList);

    List<LabInfo> getLabInfo(int labId, int batchId);

    List<LabInfo> getLabInfo(int id);

    List<LabReport> getLabReportInfo(int labId, int batchId);

    List<QuestionPointsPayLoad> getQuestionListViaLabId(int labId);

    List<LabInfoIdBatch> getLabInfoIdBatch(int labId);

    List<LabSummary> getLabByIdBatch(int labId, int batchId);

    List<LabPayload> getLabByBatch(int batchId);

    LabReport getLabReportInfoByTraniee(int labId, int batchId, String traineeId);

    List<RecordLabAttempt> getReportsOfTraineeByLabId(String traineeId, int labId);

    LabPayload getLabPayloadById(int labId);

    List<LabInfo> getAllLabInfoByTrainee(String traineeId);

    List<LabPayload> getLabByBatchWithTotalMarks(int batchId);

    List<TraineeLabMarksPayload> getTraineeMarksByTraineeIdBatchId(int batchId, String traineeId);

    boolean deleteLab(int labId, String userName);

    boolean deleteLabTest(int labTestId, String userName);

    LabDto getLabWithoutReport(int labId);

    LabDto addLabToAdmin(LabDto labDto, String userName);
}
