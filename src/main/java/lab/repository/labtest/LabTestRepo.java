package lab.repository.labtest;

import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.entity.evaluation.RecordLabTestAttempt;
import lab.entity.evaluation.TraineeLabTestReports;
import lab.entity.lab.LabTestInfo;
import lab.exceptionHandler.CustomDatabaseException;
import lab.payload.labTest.LabTestDetail;
import lab.payload.labTest.LabTestDetail;
import lab.payload.labTest.LabTestPayload;
import lab.payload.labTest.LabTestQuestion;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.labTest.LabTestQuestion;
import lab.payload.report.LabTestPassPercentage;
import lab.payload.report.LabTestReport;
import lab.payload.report.TraineeMarksPayload;

import java.util.List;

public interface LabTestRepo {
    void addLabTest(LabTestDto labTestDto, String traineeId) throws CustomDatabaseException;

    LabTestDto getLabTest(int labId);

    LabTestDto updateLabTest(LabTestDto labTestDto);

    boolean deleteLabTest(LabTestDto labTestDto);

    List<LabTestDto> getTestsByTraineeUsername(String employeeId);
    LabTestDto getTest(int labId);

    QuestionDto getLabTestQuestion(int id, int questionId);

    void editQuestionInLabTest(QuestionDto questionDto);

    void addLabTestInfo(LabTestInfo labTestInfo);

    LabTestInfo getLabTestInfo(int labTestId, String traineeId, int batchId);

    List<RecordLabTestAttempt> getLabTestReport(int labTestId, String traineeId);

    public LabTestQuestion getLabTestQuestionData(String traineeId);
    LabTestInfo getLabTestData(int batchId, int labId, String traineeId);

    void updateLabTestInfo(LabTestInfo labTestInfo);

    List<LabTestDto> getIncompleteLabTest(String userId);

    List<LabTestDto> getCompleteLabTest(String userId);

    int gettotalQuestion(int currentLabTestId);

    List<RecordLabTestAttempt> getLabTestReports(int labTestId);

    List<LabTestDto> getAllLabTest();
    List<LabTestDto> getUnAttemptedLabTest(String userId);
    List<LabTestDto> getLabTestAccordingToStatus(String userId, String status);

    List<LabTestPayload> getTraineeLabTests(String userId, String status);

    void addLabTestInfoList(List<LabTestInfo> labTestInfoList);

    LabTestDetail getLabTestDetails(int batchId, String traineeId);
    int getNoOfQuestionInLabTests(String traineeId);

    List<LabTestReport> getLabTestInfoById(int labTestId, int batchId);

    List<LabTestInfo> getLabTestInfoById(int id);

    int getQuestionPoint(int labTestId);

    List<QuestionPointsPayLoad> getQuestionListViaLabTestId(int labTestId);

    List<LabTestPayload> getLabTestsByBatch(int batchId);

    List<RecordLabTestAttempt> getReportsOfTraineeByLabTestId(String traineeId, int labTestId);

    LabTestReport getLabTestReportInfoByTraniee(int labTestId, int batchId, String traineeId);

    LabTestDto getLabTestByIdAndBatchId(int labTestId, int batchId, String labTestName);

    List<LabTestInfo> getALlLabTestInfoByTraineeId(String traineeId);

    List<LabTestPassPercentage> getAllLabTestReportInfoByTrainee(int batchId, String traineeId);

    List<TraineeLabTestReports> getReportsOfTraineeByTraineeId(String traineeId);

    List<TraineeMarksPayload> getTraineeMarksByTraineeIdBatchId(int batchId, String traineeId);

    List<LabTestPayload> getLabTestsByBatchWithTotalMarks(int batchId);

    LabTestDto getLabTestWithoutReport(int labTestId);

    LabTestDto getLabTestSummaryById(int labTestId);

    boolean mapQuestionsToLabTest(int labId,List<Integer> questionIds); // added to map question to lab
}
