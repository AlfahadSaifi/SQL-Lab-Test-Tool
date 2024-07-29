package lab.service.labtest;

import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.entity.evaluation.RecordLabTestAttempt;
import lab.entity.evaluation.TraineeLabTestReports;
import lab.entity.lab.LabTestInfo;
import lab.exceptionHandler.CustomDatabaseException;
import lab.payload.labTest.LabTestDetail;
import lab.payload.labTest.LabTestPayload;
import lab.payload.labTest.LabTestQuestion;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.report.LabTestPassPercentage;
import lab.payload.report.LabTestReport;
import lab.payload.report.TraineeMarksPayload;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface LabTestService {

    LabTestDto addLabTest(LabTestDto labTestDto, String username) throws CustomDatabaseException;

    List<LabTestDto> getLabTests(String userName);

    boolean addQuestionInLabTest(QuestionDto questionDto, int labId);

    LabTestDto getLabTestById(int labTestId);

    int getPointsPerQuest(int labTestId);

    boolean deleteLabTest(int testLabId, String userName);

    LabTestDto getTestById(int testId);

    QuestionDto getLabTestQuestion(int id, int questionId);

    void editQuestionInLabTest(QuestionDto questionDto, int labId);

    void deleteLabTestQuestion(int labTestId, int questionId);

    List<LabTestDto> getTestsByTraineeUsername(String employeeId);

    void addLabTestInfo(LabTestInfo labTestInfo);

    LabTestInfo  getLabTestInfo(int labTestId, String traineeId, int batchId);

    List<RecordLabTestAttempt> getLabTestReport(int labTestId, String traineeId);

    List<RecordLabTestAttempt>  getLabTestReport(int labTestId);
    LabTestDetail getLabTestDetails(int batchId, String traineeId);
    LabTestQuestion getLabTestQuestionDetails(int batchId, String traineeId);

    void submitLabTest(int labTestId, String traineeId, int batchId);

    List<LabTestDto> getIncompleteLabTest(String userId);

    List<LabTestDto> getCompleteLabTest(String userId);

    int TotalQuestion(int currentLabTestId);

    List<LabTestDto> getAllLabTest();

    List<LabTestPayload> getTraineeLabTests(String userId, String status);

    void addDefaultLabTestInfo(int labTestId, int batchId, List<String> traineeIdList);

//    void addLabTestReferenceFile(MultipartFile file, int labTestId);

    List<LabTestReport> getLabTestInfo(int labTestId, int batchId);

    List<LabTestInfo> getLabTestinfo(int id);

    int getQuestionPoint(int labTestId);

    List<QuestionPointsPayLoad> getQuestionListViaLabTestId(int labTestId);

    List<LabTestPayload> getLabTestsByBatch(int batchId);

    LabTestReport getLabTestReportInfoByTraniee(int labTestId, int batchId, String traineeId);

    List<RecordLabTestAttempt> getReportsOfTraineeByLabTestId(String traineeId, int labTestId);

    LabTestDto getLabTestByIdAndBatchId(int labTestId, int batchId, String labTestName);

    LabTestDto addLabTestToAdmin(LabTestDto labTestDto, String adminId);

    List<LabTestInfo> getALlLabTestInfoByTraineeId(String traineeId);

    List<LabTestPassPercentage> getAllLabTestReportInfoByTrainee(int batchId, String traineeId);


    List<TraineeLabTestReports> getReportsOfTraineeByTraineeId(String traineeId);

    List<TraineeMarksPayload> getTraineeMarksByTraineeIdBatchId(int batchId, String traineeId);

    List<LabTestPayload> getLabTestsByBatchWithTotalMarks(int batchId);

    LabTestDto getLabTestWithoutReport(int labTestId);

    LabTestDto getLabTestSummaryById(int labTestId);

    boolean mapQuestionsToLabTest(int labId,List<Integer> questionIds); // added to map question to lab
    LabTestDto updateLabTest(LabTestDto labTestDto);

}
