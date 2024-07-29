package lab.service.labtest;

import lab.dto.admin.AdminDto;
import lab.dto.evaluation.LabTestReportsDto;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.entity.admin.Admin;
import lab.entity.evaluation.RecordLabTestAttempt;
import lab.entity.evaluation.TraineeLabTestReports;
import lab.entity.lab.LabStatus;
import lab.entity.lab.LabTestInfo;
import lab.exceptionHandler.CustomDatabaseException;
import lab.payload.labTest.LabTestDetail;
import lab.payload.labTest.LabTestPayload;
import lab.payload.labTest.LabTestQuestion;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.report.LabTestPassPercentage;
import lab.payload.report.LabTestReport;
import lab.payload.report.TraineeMarksPayload;
import lab.repository.admin.AdminRepo;
import lab.repository.labtest.LabTestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class LabTestServiceImpl implements LabTestService {

    @Autowired
    private LabTestRepo labTestRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Override
    public LabTestDto addLabTest(LabTestDto labTestDto, String userId) throws CustomDatabaseException {
        AdminDto adminDto = adminRepo.getAdminByUserName(userId);
        List<LabTestDto> labList = adminDto.getLabTests();
        List<LabTestReportsDto> reportsDtos = new ArrayList<>();
        labTestDto.setLabTestReports(reportsDtos);
        labList.add(labTestDto);
        adminDto.setLabTests(labList);
        try {
            AdminDto adminDto1 = adminRepo.updateAdmin(adminDto);
            int i = adminDto1.getLabTests().size() - 1;
            labTestDto = adminDto1.getLabTests().get(Math.max(i,0));
        } catch (CustomDatabaseException e) {
            throw new CustomDatabaseException("Lab Already Exist.", e);
        }
        return labTestDto;
    }


    @Override
    public List<LabTestDto> getLabTests(String userName) {
        return adminRepo.getAdminByUserName(userName).getLabTests();
    }

    private String querySuffixChecker(String query){
        query = query.trim();
        if(query.endsWith(";")){
            query = query.substring(0, query.length()-1);
        }
        return query;
    }

    @Override
    public boolean addQuestionInLabTest(QuestionDto questionDto, int labId) {
        LabTestDto labTestDto = labTestRepo.getLabTest(labId);
        questionDto.setQuestionAnswer(querySuffixChecker(questionDto.getQuestionAnswer())); // removing ';' from query
        List<QuestionDto> questionList = labTestDto.getQuestions();
        if (questionList == null) {
            List<QuestionDto> questionListNew = new ArrayList<>();
            questionListNew.add(questionDto);
            labTestDto.setQuestions(questionListNew);
        } else {
            questionList.add(questionDto);
            labTestDto.setQuestions(questionList);
        }
        labTestRepo.updateLabTest(labTestDto);
        return true;
    }

    @Override
    public LabTestDto getLabTestById(int labId) {
        LabTestDto labTest = labTestRepo.getLabTest(labId);
        return labTest;
    }

    @Override
    public int getPointsPerQuest(int labTestId) {
       return labTestRepo.getQuestionPoint(labTestId);
    }

    @Override
    public boolean deleteLabTest(int testLabId, String userName) {
        adminRepo.removeLabTest(testLabId, userName);
        return true;
    }

    //(-sks) service method that will return test of the provided id
    @Override
    public LabTestDto getTestById(int testId) {
        return labTestRepo.getTest(testId);
    }

    @Override
    public QuestionDto getLabTestQuestion(int id, int questionId) {
        QuestionDto questionDto = labTestRepo.getLabTestQuestion(id, questionId);
        return questionDto;
    }

    @Override
    public void editQuestionInLabTest(QuestionDto questionDto, int labId) {
        labTestRepo.editQuestionInLabTest(questionDto);
    }

    @Override
    public void deleteLabTestQuestion(int labTestId, int questionId) {
        LabTestDto labTestDto = labTestRepo.getTest(labTestId);
        List<QuestionDto> questionDtos = labTestDto.getQuestions();
        Iterator<QuestionDto> iterator = questionDtos.iterator();
        while (iterator.hasNext()) {
            QuestionDto questionDto = iterator.next();
            if (questionId == questionDto.getQuestionId()) {
                iterator.remove();
            }
        }
        labTestRepo.updateLabTest(labTestDto);
    }

    @Override
    public List<LabTestDto> getTestsByTraineeUsername(String employeeId) {
        return labTestRepo.getTestsByTraineeUsername(employeeId);
    }

    @Override
    public void addLabTestInfo(LabTestInfo labTestInfo) {
        labTestRepo.addLabTestInfo(labTestInfo);
    }

    @Override
    public LabTestInfo getLabTestInfo(int labTestId, String traineeId, int batchId) {
        return labTestRepo.getLabTestInfo(labTestId, traineeId, batchId);
    }

    @Override
    public List<RecordLabTestAttempt> getLabTestReport(int labTestId, String traineeId) {
        return labTestRepo.getLabTestReport(labTestId, traineeId);
    }

    @Override
    public List<RecordLabTestAttempt> getLabTestReport(int labTestId) {
        return labTestRepo.getLabTestReports(labTestId);
    }

    @Override
    public LabTestDetail getLabTestDetails(int batchId, String traineeId) {
        LabTestDetail labTestDetails = labTestRepo.getLabTestDetails(batchId, traineeId);
        List<LabTestPayload> unattempted = labTestRepo.getTraineeLabTests(traineeId, "unattempted");
        labTestDetails.setUnattemptedLabTest(unattempted.size());
        return labTestDetails;
    }

    @Override
    public LabTestQuestion getLabTestQuestionDetails(int batchId, String traineeId) {
        //int noOfQuestionInLabTests = labTestRepo.getNoOfQuestionInLabTests(traineeId);
        LabTestQuestion labTestQuestionData = labTestRepo.getLabTestQuestionData(traineeId);
        //int unattemptedLabs = noOfQuestionInLabTests - (labTestQuestionData.getCorrectLabTestQuestion() + labTestQuestionData.getIncorrectLabTestQuestion());
        //labTestQuestionData.setUnattemptedLabTestQuestion(unattemptedLabs);
        return labTestQuestionData;
    }
    @Override
    public void submitLabTest(int labTestId, String traineeId, int batchId) {
        LabTestInfo labTestInfo = labTestRepo.getLabTestInfo(labTestId, traineeId, batchId);
        labTestInfo.setLabTestStatus(LabStatus.COMPLETED);
        labTestRepo.updateLabTestInfo(labTestInfo);
    }

    @Override
    public List<LabTestDto> getIncompleteLabTest(String userId) {
        return labTestRepo.getIncompleteLabTest(userId);
    }

    @Override
    public List<LabTestDto> getCompleteLabTest(String userId) {
        return labTestRepo.getCompleteLabTest(userId);
    }

    @Override
    public int TotalQuestion(int currentLabTestId) {
        return labTestRepo.gettotalQuestion(currentLabTestId);
    }

    @Override
    public List<LabTestDto> getAllLabTest() {
        return labTestRepo.getAllLabTest();
    }

    @Override
    public List<LabTestPayload> getTraineeLabTests(String userId, String status) {
        return labTestRepo.getTraineeLabTests(userId, status);
    }

    @Override
    public void addDefaultLabTestInfo(int labTestId, int batchId, List<String> traineeIdList) {
        List<LabTestInfo> labTestInfoList = new ArrayList<>();
        for (String traineeId : traineeIdList) {
            LabTestInfo labTestInfo = new LabTestInfo();
            labTestInfo.setLabTestId(labTestId);
            labTestInfo.setBatch(batchId);
            labTestInfo.setTraineeId(traineeId);
            labTestInfo.setLabTestStatus(LabStatus.UNATTEMPTED);
            labTestInfoList.add(labTestInfo);
        }
        labTestRepo.addLabTestInfoList(labTestInfoList);
    }

//    @Override
//    public void addLabTestReferenceFile(MultipartFile file, int labTestId) {
//        LabTestDto labTestDto = labTestRepo.getLabTest(labTestId);
//        byte[] referenceFileBytes;
//        try {
//            referenceFileBytes = file.getBytes();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        labTestDto.setReferenceFile(referenceFileBytes);
//        labTestRepo.updateLabTest(labTestDto);
//    }

    @Override
    public List<LabTestReport> getLabTestInfo(int labTestId, int batchId) {
        return labTestRepo.getLabTestInfoById(labTestId, batchId);
    }

    @Override
    public List<LabTestInfo> getLabTestinfo(int id) {
        return labTestRepo.getLabTestInfoById(id);
    }

    @Override
    public int getQuestionPoint(int labTestId) {
        int i = labTestRepo.getQuestionPoint(labTestId);
        System.out.println(i);
        return i;
    }

    @Override
    public List<QuestionPointsPayLoad> getQuestionListViaLabTestId(int labTestId) {
        return labTestRepo.getQuestionListViaLabTestId(labTestId);
    }

    @Override
    public List<RecordLabTestAttempt> getReportsOfTraineeByLabTestId(String traineeId, int labTestId) {
        return labTestRepo.getReportsOfTraineeByLabTestId(traineeId,labTestId);
    }

    @Override
    public LabTestDto getLabTestByIdAndBatchId(int labTestId, int batchId, String labTestName) {
        return labTestRepo.getLabTestByIdAndBatchId(labTestId,batchId,labTestName);
    }

    @Override
    public LabTestDto addLabTestToAdmin(LabTestDto labTestDto, String adminId) {
        return adminRepo.addLabTestInAdmin(labTestDto, adminId);
    }

    @Override
    public List<LabTestInfo> getALlLabTestInfoByTraineeId(String traineeId) {
        return labTestRepo.getALlLabTestInfoByTraineeId(traineeId);
    }

    @Override
    public List<LabTestPassPercentage> getAllLabTestReportInfoByTrainee(int batchId, String traineeId) {
        return labTestRepo.getAllLabTestReportInfoByTrainee(batchId,traineeId);
    }

    @Override
    public List<TraineeLabTestReports> getReportsOfTraineeByTraineeId(String traineeId) {
        return labTestRepo.getReportsOfTraineeByTraineeId(traineeId);
    }

    @Override
    public List<TraineeMarksPayload> getTraineeMarksByTraineeIdBatchId(int batchId, String traineeId) {
        return labTestRepo.getTraineeMarksByTraineeIdBatchId(batchId,traineeId);
    }

    @Override
    public List<LabTestPayload> getLabTestsByBatchWithTotalMarks(int batchId) {
        return labTestRepo.getLabTestsByBatchWithTotalMarks(batchId);
    }

    @Override
    public LabTestDto getLabTestWithoutReport(int labTestId) {
        return labTestRepo.getLabTestWithoutReport(labTestId);
    }

    @Override
    public LabTestDto getLabTestSummaryById(int labTestId) {
        return labTestRepo.getLabTestSummaryById(labTestId);
    }

    @Override
    public boolean mapQuestionsToLabTest(int labId, List<Integer> questionIds) {
        return labTestRepo.mapQuestionsToLabTest(labId,questionIds);
    }


    @Override
    public List<LabTestPayload> getLabTestsByBatch(int batchId) {
        return labTestRepo.getLabTestsByBatch(batchId);
    }

    @Override
    public LabTestReport getLabTestReportInfoByTraniee(int labTestId, int batchId, String traineeId) {
        return labTestRepo.getLabTestReportInfoByTraniee(labTestId,batchId, traineeId);
    }

    @Override
    public LabTestDto updateLabTest(LabTestDto labTestDto) {
        return labTestRepo.updateLabTest(labTestDto);
    }
}
