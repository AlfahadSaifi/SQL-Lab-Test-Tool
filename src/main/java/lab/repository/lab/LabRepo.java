package lab.repository.lab;

import lab.dto.evaluation.LabReportsDto;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.entity.evaluation.RecordLabAttempt;
import lab.entity.lab.Lab;
import lab.entity.lab.LabInfo;
import lab.payload.lab.LabDetail;
import lab.payload.lab.LabQuestion;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.lab.*;
import lab.payload.report.LabReport;
import lab.payload.report.TraineeLabMarksPayload;

import java.util.List;

public interface LabRepo {

    boolean addNewLab(LabDto labDto);

    LabDto getLab(int labId);

    LabTestDto getLabTest(int labId);

    Lab getLabById(int labId);

    void updateLab(LabDto labDto);

    void submitLabReport(LabDto labDto);

    List<LabPayload> getLabsByTraineeUsername(String traineeUserName);

    LabReportsDto getLabReportByBatchId(int batchId);

    QuestionDto getQuestion(int labId, int questionId);

    boolean updateQuestion(QuestionDto questionDto);

    boolean deleteQuestion(QuestionDto questionDto, int labId);

    void removeQuestion(QuestionDto questionDto);

    void updateLabTest(LabTestDto labTestDto);

    LabQuestion getLabQuestionData(String traineeId);

    LabDetail getLabData(String traineeId, int batchId) ;

    List<LabPayload> getIncompleteLab(String userId);

    List<LabPayload> getCompleteLab(String userId);

    List<RecordLabAttempt> getLabReport(int labId, String traineeId);

    void addLabInfo(LabInfo labInfo);

    LabInfo getLabInfo(int labId, String traineeId, int batchId);

    List<LabDto> getAllLab();

    int totalQuestion(int labId);

    List<RecordLabAttempt> getLabReport(int labId);

    void updateLabInfo(LabInfo labInfo);
    List<LabPayload> getUnAttemptedLab(String userId);
    List<LabPayload> getTraineeLabs(String userId, String status);


    int getQuestionPoint(int labId);

    void addLabInfoList(List<LabInfo> labInfoList);

    int getNoOfQuestionInLabs(String traineeId);

    List<LabInfo> getLabInfoList(int labId, int batchId);

    List<LabInfo> getLabInfoList(int id);

    List<LabReport> getLabReportInfo(int labId, int batchId);

    List<QuestionPointsPayLoad> getQuestionListViaLabId(int labId);

    List<LabInfoIdBatch> getLabInfoIdBatch(int labId);

    List<LabSummary> getLabByIdBatch(int labId, int batchId);

    List<LabPayload> getLabByBatch(int batchId);

    LabReport getLabReportInfoByTraniee(int labId, int batchId, String traineeId);

    List<RecordLabAttempt> getReportsOfTraineeByLabId(String traineeId, int labId);

    LabPayload getLabPayloadById(int labId);

    LabDto createLab(LabDto labDto);

    void addLabByAdminIdAndLab(long adminId, int labId);

    void removeQuestionById(int labId, int questionId);

    boolean removeQuestionFromJoinTable(int labId, int questionId);

    void removeQuestionFromTableById(int questionId);

    void updateQuestionByHql(QuestionDto questionDto);

    List<LabInfo> getAllLabInfoByTrainee(String traineeId);

    List<LabPayload> getLabByBatchWithTotalMarks(int batchId);

    List<TraineeLabMarksPayload> getTraineeMarksByTraineeIdBatchId(int batchId, String traineeId);

    LabDto getLabWithoutReport(int labId);
}
