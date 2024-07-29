package lab.controller.trainee;

import lab.dto.evaluation.RecordLabAttemptDto;
import lab.dto.evaluation.RecordLabTestAttemptDto;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.evaluation.RecordLabAttempt;
import lab.entity.evaluation.RecordLabTestAttempt;
import lab.entity.lab.LabInfo;
import lab.entity.lab.LabTestInfo;
import lab.entity.lab.QuestionStatus;
import lab.entity.lab.Status;
import lab.entity.timer.Timer;
import lab.payload.*;
import lab.payload.answer.AnswerLabTestResponse;
import lab.payload.answer.AnswerPayload;
import lab.payload.answer.AnswerResponse;
import lab.payload.answer.AnswerResponseMessage;
import lab.payload.lab.*;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.batch.BatchIdCode;

import lab.payload.labTest.LabTestDetail;
import lab.payload.labTest.LabTestPayload;
import lab.payload.labTest.LabTestQuestion;
import lab.payload.report.*;
import lab.repository.labtest.LabTestRepo;
import lab.service.batches.BatchService;
import lab.service.lab.LabService;
import lab.service.labEngine.LabEngineService;
import lab.service.labtest.LabTestService;
import lab.service.reports.ReportService;
import lab.service.timer.TimerService;
import lab.service.trainee.TraineeService;
import lab.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

import static lab.gobal.ReportPayload.convertToDetailReportPayload;
import static lab.gobal.ReportPayload.convertToLabDetailReportPayload;

@RestController
@RequestMapping(path = "/api/trainee")
@SessionAttributes("username")
public class TraineeRest {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private LabService labService;
    @Autowired
    private LabTestService labTestService;
    @Autowired
    private LabTestRepo labTestRepo;
    @Autowired
    private ReportService reportService;
    @Autowired
    private LabEngineService labEngineService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private TimerService timerService;
    @Autowired
    private UserService userService;

    private String getUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String getUserName(String userId) {
        return userService.getUserNameById(userId);
    }

    @RequestMapping(path = "/getLabQuestions")
    public ResponseEntity<?> labStart(@RequestParam("labId") int labId, Model model) {
        TraineeDto traineeByUserName = traineeService.getTraineeByUserName(getUserId());
        String traineeId = traineeByUserName.getEmployeeId();
//        LabDto labDto = labService.getLabById(labId);
        LabDto labDto = labService.getLabWithoutReport(labId);
        LabInfo labInfo = labService.getLabInfo(labId, traineeId, traineeByUserName.getBatchId());
        List<QuestionDto> questionList = labDto.getQuestions();
        if(!questionList.isEmpty()){
            questionList.forEach(questionDto -> questionDto.setQuestionAnswer(""));
        }
        if (labInfo.getQuestionStatusList().size() == 0) {
            List<QuestionStatus> questionStatusList = labInfo.getQuestionStatusList();
            for (QuestionDto question : questionList) {
                QuestionStatus questionStatus = new QuestionStatus();
                questionStatus.setQuestionId(question.getQuestionId());
                questionStatus.setStatus(Status.UNATTEMPTED);
                questionStatusList.add(questionStatus);
            }
            labInfo.setQuestionStatusList(questionStatusList);
            labService.addLabInfo(labInfo);
        }
        QuestionLabInfo questionLabInfo = new QuestionLabInfo();
        questionLabInfo.setLabDto(labDto);
        questionLabInfo.setLabInfo(labInfo);
        return ResponseEntity.ok().body(questionLabInfo);
    }

    @RequestMapping(path = "/getLabTestReport")
    public ResponseEntity<ReportLabTestPayload> getLabTestInfo(@RequestParam("labTestId") int labTestId, Model model) {
        TraineeDto traineeDto = traineeService.getTraineeByUserName(getUserId());
        LabTestInfo labTestInfo = labTestService.getLabTestInfo(labTestId, getUserId(), traineeDto.getBatchId());
        List<RecordLabTestAttempt> recordLabTestAttemptList = labTestService.getLabTestReport(labTestId, getUserId());
        double obtainedPoint = traineeService.getTraineeLabTestObtainedPoint(recordLabTestAttemptList);
        double totalPoint = traineeService.getTraineeLabTestTotalPoints(labTestId);
        ReportLabTestPayload reportLabTestPayload = new ReportLabTestPayload();
        reportLabTestPayload.setRecordLabTestAttemptList(recordLabTestAttemptList);
        reportLabTestPayload.setLabTestInfo(labTestInfo);
        reportLabTestPayload.setObtainedPoint(obtainedPoint);
        reportLabTestPayload.setTotalPoints(totalPoint);
        return ResponseEntity.ok().body(reportLabTestPayload);
    }


    @RequestMapping(path = "/getLabReport")
    public ResponseEntity<LabReportPayload> getLabReport(@RequestParam("labId") int labId, Model model) {
        TraineeDto traineeDto = traineeService.getTraineeByUserName(getUserId());
        LabInfo labInfo = labService.getLabInfo(labId, getUserId(), traineeDto.getBatchId());
        List<RecordLabAttempt> recordLabAttemptList = labService.getLabReport(labId, getUserId());
        double obtainedPoint = traineeService.getTraineeLabObtainedPoint(recordLabAttemptList);
        double totalPoints = traineeService.getTraineeLabTotalMarks(labId);
        LabReportPayload reportPayload = new LabReportPayload();
        reportPayload.setRecordLabAttemptList(recordLabAttemptList);
        reportPayload.setLabInfo(labInfo);
        reportPayload.setObtainedPoint(obtainedPoint);
        reportPayload.setTotalPoints(totalPoints);
        return ResponseEntity.ok().body(reportPayload);
    }

    @RequestMapping(path = "/getLabTestQuestions")
    public ResponseEntity<?> labTestStart(@RequestParam("labTestId") int labTestId, Model model) {
        TraineeDto traineeByUserName = traineeService.getTraineeByUserName(getUserId());
        String traineeId = traineeByUserName.getEmployeeId();
        LabTestInfo labTestInfo = labTestService.getLabTestInfo(labTestId, traineeId, traineeByUserName.getBatchId());
//        LabTestDto labTestDto = labTestService.getLabTestById(labTestId);
        LabTestDto labTestDto = labTestService.getLabTestWithoutReport(labTestId);
        List<QuestionDto> questionList = labTestDto.getQuestions();
        if(!questionList.isEmpty()){
            questionList.forEach(questionDto -> questionDto.setQuestionAnswer(""));
        }
        Timer timer = new Timer();
        if (labTestInfo.getQuestionStatusList().size() == 0) {
            List<QuestionStatus> questionStatusList = labTestInfo.getQuestionStatusList();
            for (QuestionDto question : questionList) {
                QuestionStatus questionStatus = new QuestionStatus();
                questionStatus.setQuestionId(question.getQuestionId());
                questionStatus.setStatus(Status.UNATTEMPTED);
                questionStatusList.add(questionStatus);
            }
            labTestInfo.setQuestionStatusList(questionStatusList);
            labTestService.addLabTestInfo(labTestInfo);
            // set timer in database
            timer.setTimerLeft(labTestDto.getDuration() * 60);
            timer.setBatch(traineeByUserName.getBatchId());
            timer.setTraineeId(traineeId);
            timer.setTestId(labTestId);
            timerService.postTestTimer(timer);
        } else {
            timer = timerService.getTestTimer(traineeByUserName.getBatchId(), labTestId, traineeId);
        }
        QuestionInfo questionInfo = new QuestionInfo();
        questionInfo.setLabTestDto(labTestDto);
        questionInfo.setLabTestInfo(labTestInfo);
        questionInfo.setTimer(timer);
        return ResponseEntity.ok().body(questionInfo);
    }

    @PostMapping(path = "/labTestSubmitQuestion")
    public ResponseEntity<?> labTestSubmitQuestion(@RequestParam("labTestId") int labTestId, @RequestBody AnswerPayload answerPayload, Model model) {
        String query = answerPayload.getQuery().trim();
        int questionId = answerPayload.getQuestionId();
        if (questionId <= 0) return ResponseEntity.ok().body("Invalid id");
        LabTestDto labTestDto = null;
        QuestionDto questionDto = null;
        List<QuestionDto> questionDtoList = null;
        if (labTestId > 0) {
            labTestDto = labTestService.getLabTestById(labTestId);
            questionDtoList = labTestDto.getQuestions();
        }
        RecordLabTestAttemptDto recordLabTestAttemptDto = reportService.fetchRecordLabTestAttempt(getUserId(), labTestId, questionId);

        String output;
        AnswerResponseMessage answerResponseMessage = labEngineService.submitAns(String.valueOf(questionId), query);
        if (answerResponseMessage.isCorrect()) {
            output = "Correct";
        } else if (answerResponseMessage.isIncorrect()) {
            output = "InCorrect";
        } else {
            output = answerResponseMessage.getIsInvalidSyntax();
        }
        List outputList = answerResponseMessage.getOutput();
        if (questionDtoList != null) {
            for (QuestionDto q : questionDtoList) {
                if (q.getQuestionId() == questionId) {
                    questionDto = q;
                    break;
                }
            }
        }
        int inCorrect = recordLabTestAttemptDto.getIncorrectAttempt();
        recordLabTestAttemptDto.setQuestionId(questionId);
        recordLabTestAttemptDto.setTraineeId(getUserId());
        recordLabTestAttemptDto.setLabTestId(labTestId);
        if (!answerResponseMessage.isCorrect()) {
            recordLabTestAttemptDto.setIncorrectAttempt(inCorrect + 1);
        }
        if (recordLabTestAttemptDto.getIncorrectAttempt() <= 2 && output.equals("Correct")) {
            recordLabTestAttemptDto.setTraineeCurrentQuestionPoints(questionDto.getQuestionPoints());
        } else if (!output.equals("Correct")) {
            recordLabTestAttemptDto.setTraineeCurrentQuestionPoints(0);
        } else if (recordLabTestAttemptDto.getIncorrectAttempt() > 2 && output.equals("Correct")) {
            int incorrectAttempt = recordLabTestAttemptDto.getIncorrectAttempt() - 2;
            recordLabTestAttemptDto.setTraineeCurrentQuestionPoints(questionDto.getQuestionPoints());
            while (incorrectAttempt > 0) {
                double traineeCurrentQuestionPoints = recordLabTestAttemptDto.getTraineeCurrentQuestionPoints();
                double negativeMarkingFactor = labTestDto.getNegativeMarkingFactor()/100;
                traineeCurrentQuestionPoints -= (traineeCurrentQuestionPoints * negativeMarkingFactor);
                traineeCurrentQuestionPoints = Math.round(traineeCurrentQuestionPoints * 100.0) / 100.0;
                recordLabTestAttemptDto.setTraineeCurrentQuestionPoints(traineeCurrentQuestionPoints);
                incorrectAttempt--;
            }
        }
        try {
            reportService.saveRecordLabTestAttempt(recordLabTestAttemptDto, query, answerResponseMessage.isCorrect());
        } catch (Exception e) {
            System.out.println(e);
        }
        model.addAttribute("output", output);
        model.addAttribute("outputList", outputList);
        LabTestDto labTestDto1 = labTestService.getLabTestById(labTestId);
        TraineeDto traineeByUserName = traineeService.getTraineeByUserName(getUserId());
        String traineeId = traineeByUserName.getEmployeeId();
        labTestService.getLabTestInfo(labTestDto1.getLabTestId(), traineeId, traineeByUserName.getBatchId());
        AnswerLabTestResponse answerResponse = new AnswerLabTestResponse();
        answerResponse.setOutput(output);
        answerResponse.setOutputList(outputList);
        answerResponse.setRecordLabTestAttemptDto(recordLabTestAttemptDto);
        // LabTestInfo
        LabTestInfo labTestInfo = labTestService.getLabTestInfo(labTestId, traineeId, traineeByUserName.getBatchId());
        List<QuestionStatus> questionStatusList = labTestInfo.getQuestionStatusList();
        boolean flag = false;
        for (QuestionStatus questionStatus : questionStatusList) {
            if (questionStatus.getQuestionId() == questionId) {
                flag = true;
                if (answerResponseMessage.isCorrect()) {
                    questionStatus.setStatus(Status.CORRECT);
                } else {
                    questionStatus.setStatus(Status.INCORRECT);
                }
            }
        }
        if (!flag) {
            QuestionStatus questionStatus = new QuestionStatus();
            questionStatus.setQuestionId(questionId);
            if (answerResponseMessage.isCorrect()) {
                questionStatus.setStatus(Status.CORRECT);
            } else {
                questionStatus.setStatus(Status.INCORRECT);
            }
            questionStatusList.add(questionStatus);
        }
        labTestInfo.setQuestionStatusList(questionStatusList);
        labTestService.addLabTestInfo(labTestInfo);
        answerResponse.setLabTestInfo(labTestInfo);
        return ResponseEntity.ok().body(answerResponse);
    }

    @PostMapping(path = "/submitLabQuestion")
    public ResponseEntity<?> labSubmitQuestion(@RequestParam("labId") int labId, @RequestBody AnswerPayload answerPayload, Model model) {
        String query = answerPayload.getQuery().trim();
        int questionId = answerPayload.getQuestionId();
        labId = answerPayload.getLabId();
        if (questionId > 0) {
            String output;
            AnswerResponseMessage answerResponseMessage = labEngineService.submitAns(String.valueOf(questionId), query);
            if (answerResponseMessage.isCorrect()) {
                output = "Correct";
            } else if (answerResponseMessage.isIncorrect()) {
                output = "InCorrect";
            } else {
                output = answerResponseMessage.getIsInvalidSyntax();
            }
            List outputList = answerResponseMessage.getOutput();
            LabDto labDto;
            QuestionDto questionDto = null;
            List<QuestionDto> questionDtoList = null;
            labDto = labService.getLabById(labId);
            questionDtoList = labDto.getQuestions();
            for (QuestionDto q : questionDtoList) {
                if (q.getQuestionId() == questionId) {
                    questionDto = q;
                    break;
                }
            }
            RecordLabAttemptDto recordLabAttemptDto = reportService.fetchRecordAttempt(getUserId(), labId, questionId);
            int inCorrect = recordLabAttemptDto.getIncorrectAttempt();
            recordLabAttemptDto.setQuestionId(questionId);
            recordLabAttemptDto.setTraineeId(getUserId());
            recordLabAttemptDto.setLabId(labId);
            if (!answerResponseMessage.isCorrect()) {
                recordLabAttemptDto.setIncorrectAttempt(inCorrect + 1);
            }
            if (recordLabAttemptDto.getIncorrectAttempt() <= 2 && output.equals("Correct")) {
                recordLabAttemptDto.setTraineeCurrentQuestionPoints(questionDto.getQuestionPoints());
            } else if (!output.equals("Correct")) {
                recordLabAttemptDto.setTraineeCurrentQuestionPoints(0);
            } else if (recordLabAttemptDto.getIncorrectAttempt() > 2 && output.equals("Correct")) {
                int incorrectAttempt = recordLabAttemptDto.getIncorrectAttempt() - 2;
                recordLabAttemptDto.setTraineeCurrentQuestionPoints(questionDto.getQuestionPoints());
                while (incorrectAttempt > 0) {
                    double traineeCurrentQuestionPoints = recordLabAttemptDto.getTraineeCurrentQuestionPoints();
                    double negativeMarkingFactor = labDto.getNegativeMarkingFactor()/100;
                    traineeCurrentQuestionPoints -= (traineeCurrentQuestionPoints * negativeMarkingFactor);
                    traineeCurrentQuestionPoints = Math.round(traineeCurrentQuestionPoints * 100.0) / 100.0;
                    recordLabAttemptDto.setTraineeCurrentQuestionPoints(traineeCurrentQuestionPoints);
                    incorrectAttempt--;
                }
            }
            try {
                reportService.saveRecordAttempt(recordLabAttemptDto, query, answerResponseMessage.isCorrect());
            } catch (Exception e) {
                System.out.println(e);
            }
            model.addAttribute("output", output);
            model.addAttribute("outputList", outputList);
            TraineeDto traineeByUserName = traineeService.getTraineeByUserName(getUserId());
            String traineeId = traineeByUserName.getEmployeeId();
            LabInfo labInfo = labService.getLabInfo(labId, traineeId, traineeByUserName.getBatchId());
            List<QuestionStatus> questionStatusList = labInfo.getQuestionStatusList();
            boolean flag = false;
            for (QuestionStatus questionStatus : questionStatusList) {
                if (questionStatus.getQuestionId() == questionId) {
                    flag = true;
                    if (answerResponseMessage.isCorrect()) {
                        questionStatus.setStatus(Status.CORRECT);
                    } else {
                        questionStatus.setStatus(Status.INCORRECT);
                    }
                }
            }
            if (!flag) {
                QuestionStatus questionStatus = new QuestionStatus();
                questionStatus.setQuestionId(questionId);
                if (answerResponseMessage.isCorrect()) {
                    questionStatus.setStatus(Status.CORRECT);
                } else {
                    questionStatus.setStatus(Status.INCORRECT);
                }
                questionStatusList.add(questionStatus);
            }
            labInfo.setQuestionStatusList(questionStatusList);
            labService.addLabInfo(labInfo);
            AnswerResponse answerResponse = new AnswerResponse();
            answerResponse.setOutput(output);
            answerResponse.setOutputList(outputList);
            answerResponse.setRecordLabAttemptDto(recordLabAttemptDto);
            answerResponse.setLabInfo(labInfo);
            return ResponseEntity.ok().body(answerResponse);
        }
        return ResponseEntity.ok().body("Invalid id");
    }

    @PostMapping("/submitYourTest")
    public ResponseEntity<?> submitYouLab(@RequestParam(value = "id") int labId) {
        TraineeDto traineeByUserName = traineeService.getTraineeByUserName(getUserId());
        reportService.submitLab(labId, getUserId());
        labService.updateLabInfo(labId, getUserId(), traineeByUserName.getBatchId());
        SubmitLabPayload submitPayload = new SubmitLabPayload();
        submitPayload.setStatus(200);
        submitPayload.setLabId(labId);
        submitPayload.setTraineeId(getUserId());
        submitPayload.setMessage("Your Test is SuccessFully Submitted!!!");
        return ResponseEntity.ok().body(submitPayload);
    }

    @PostMapping("/submitYourLabTest")
    public ResponseEntity<?> submitYourLabTest(@RequestParam(value = "id") int labTestId) {
        TraineeDto traineeByUserName = traineeService.getTraineeByUserName(getUserId());
        reportService.submitLabTest(labTestId, getUserId());
        // Assessment Info
        labTestService.submitLabTest(labTestId, getUserId(), traineeByUserName.getBatchId());
        SubmitPayload submitPayload = new SubmitPayload();
        submitPayload.setStatus(200);
        submitPayload.setLabTestId(labTestId);
        submitPayload.setTraineeId(getUserId());
        submitPayload.setMessage("Your Test is SuccessFully Submitted!!!");
        return ResponseEntity.ok().body(submitPayload);
    }

    @GetMapping("/dashboardDetail")
    public ResponseEntity<?> dashboardDetail() {
        String traineeId = getUserId();
        int batchId = traineeService.getBatchIdByUserName(traineeId);
        DashboardPayload dashboardPayload = new DashboardPayload();
        try {
            LabDetail labDetail = labService.getLabData(traineeId, batchId);
            dashboardPayload.setLabDetail(labDetail);
            LabQuestion labQuestion = labService.getLabQuestionData(batchId, traineeId);
            dashboardPayload.setLabQuestion(labQuestion);
            LabTestDetail labTestDetail = labTestService.getLabTestDetails(batchId, traineeId);
            dashboardPayload.setLabTestDetail(labTestDetail);
            LabTestQuestion labTestQuestion = labTestService.getLabTestQuestionDetails(batchId, traineeId);
            dashboardPayload.setLabTestQuestion(labTestQuestion);
        } catch (Exception ex) {
            return ResponseEntity.ok().body(dashboardPayload);
        }
        return ResponseEntity.ok().body(dashboardPayload);
    }

    @GetMapping("/viewLabData")
    public ResponseEntity<?> completeLab(@RequestParam String status) {
        List<LabPayload> labPayloadList = labService.getTraineeLabs(getUserId(), status);
        return ResponseEntity.ok().body(labPayloadList);
    }

    @GetMapping("/viewLabTestData")
    public ResponseEntity<?> completedLabTest(@RequestParam String status) {
        List<LabTestPayload> labTestPayloadList = labTestService.getTraineeLabTests(getUserId(), status);
        return ResponseEntity.ok().body(labTestPayloadList);
    }
    @GetMapping("getLabTestDetailReport")
    public ResponseEntity<?> getLabTestDetailReport(@RequestParam int labTestId) throws RuntimeException{
        LocalDateTime endDate = labTestService.getLabTestById(labTestId).getEndDate();
        if(LocalDateTime.now().isBefore(endDate))
           return ResponseEntity.status(500).body("Detailed Report will be generated after "+ endDate);
        String traineeId = getUserId();
        String traineeName= getUserName(getUserId());
        BatchIdCode batchIdCode = batchService.getBatchIdCodeByTrainee(traineeId);
        int batchId =0;
        if (batchIdCode!=null)
            batchId = batchIdCode.getId();
        LabTestReport labTestReport = labTestService.getLabTestReportInfoByTraniee(labTestId, batchId, traineeId);
        List<QuestionPointsPayLoad> questionListViaLabId = labTestService.getQuestionListViaLabTestId(labTestId);
        List<RecordLabTestAttempt> recordLabTestAttempts = labTestService.getReportsOfTraineeByLabTestId(traineeId, labTestId);

        if (labTestReport == null && recordLabTestAttempts == null && recordLabTestAttempts.size() == 0) {
            LabTestReportDetailPayload payload = new LabTestReportDetailPayload();

            return ResponseEntity.ok().body(payload);
        }
        BatchIdCode batchIdCodeById = batchService.getBatchIdCodeById(labTestReport.getLabTestInfo().getBatch());
        String batchCode="";
        if(batchIdCodeById!=null){
            batchCode=batchIdCodeById.getBatchCode();
        }
        int totalQuestions = questionListViaLabId.size();
        int totalCorrectQuestions = 0;
        int totalIncorrectQuestions = 0;
        int totalSkipQuestions = 0;
        double totalScore=0.0;
        double gotScore=0.0;
        for (QuestionPointsPayLoad quesPayload:questionListViaLabId) {
            totalScore += quesPayload.getQuestionPoints();
        }
        for(RecordLabTestAttempt record:recordLabTestAttempts){
            gotScore+=record.getTraineeCurrentQuestionPoints();
        }
        for (QuestionStatus questionStatus : labTestReport.getLabTestInfo().getQuestionStatusList()) {
            if (questionStatus.getStatus() == Status.CORRECT) {
                totalCorrectQuestions++;
            } else if (questionStatus.getStatus() == Status.INCORRECT) {
                totalIncorrectQuestions++;
            }
        }
        totalSkipQuestions = totalQuestions - (totalCorrectQuestions + totalIncorrectQuestions);
        List<DetailLabTestReport> detailReportPayloads = convertToDetailReportPayload(questionListViaLabId, recordLabTestAttempts);

        double percentage = (gotScore / totalScore) * 100;

        LabTestReportDetailPayload payload = new LabTestReportDetailPayload();
        payload.setTraineeId(traineeId);
        payload.setTraineeName(labTestReport.getTraineeName());
        payload.setBatchCode(batchCode);
//        payload.setLabName(labTestName);
        payload.setTotalScore(totalScore);
        payload.setGotScore(gotScore);
        payload.setTotalLabQuestions(questionListViaLabId.size());
        payload.setTotalSkipQuestions(totalSkipQuestions);
        payload.setTotalCorrectQuestions(totalCorrectQuestions);
        payload.setTotalIncorrectQuestions(totalIncorrectQuestions);
        payload.setPercentage(percentage);
        payload.setResult(percentage >= labTestReport.getPassPercentage() ? "PASS" : "FAIL");
        payload.setDetailReportPayloads(detailReportPayloads);
        return ResponseEntity.ok().body(payload);
    }
  @GetMapping("getLabDetailReport")
    public ResponseEntity<?> getLabDetailReport(@RequestParam int labId) {
        String traineeId = getUserId();
        String traineeName= getUserName(getUserId());
        BatchIdCode batchIdCode = batchService.getBatchIdCodeByTrainee(traineeId);
        int batchId =0;
        if (batchIdCode!=null)
            batchId = batchIdCode.getId();
        LabReport labReport = labService.getLabReportInfoByTraniee(labId, batchId, traineeId);
        List<QuestionPointsPayLoad> questionListViaLabId = labService.getQuestionListViaLabId(labId);
        List<RecordLabAttempt> recordLabAttempts = labService.getReportsOfTraineeByLabId(traineeId, labId);
        if (labReport == null && recordLabAttempts == null && recordLabAttempts.size() == 0) {
            LabTestReportDetailPayload payload = new LabTestReportDetailPayload();
            return ResponseEntity.ok().body(payload);
        }
        BatchIdCode batchIdCodeById = batchService.getBatchIdCodeById(labReport.getLabInfo().getBatch());
        String batchCode="";
        if(batchIdCodeById!=null){
            batchCode=batchIdCodeById.getBatchCode();
        }
        int totalQuestions = questionListViaLabId.size();
        int totalCorrectQuestions = 0;
        int totalIncorrectQuestions = 0;
        int totalSkipQuestions = 0;
        double totalScore=0.0;
        double gotScore=0.0;
        for (QuestionPointsPayLoad quesPayload:questionListViaLabId) {
            totalScore += quesPayload.getQuestionPoints();
        }
        for(RecordLabAttempt record:recordLabAttempts){
            gotScore+=record.getTraineeCurrentQuestionPoints();
        }
        for (QuestionStatus questionStatus : labReport.getLabInfo().getQuestionStatusList()) {
            if (questionStatus.getStatus() == Status.CORRECT) {
                totalCorrectQuestions++;
            } else if (questionStatus.getStatus() == Status.INCORRECT) {
                totalIncorrectQuestions++;
            }
        }
        totalSkipQuestions = totalQuestions - (totalCorrectQuestions + totalIncorrectQuestions);
        List<DetailLabReport> detailReportPayloads = convertToLabDetailReportPayload(questionListViaLabId, recordLabAttempts);

        double percentage = (gotScore / totalScore) * 100;

        LabReportDetailPayload payload = new LabReportDetailPayload();
        payload.setTraineeId(traineeId);
        payload.setTraineeName(labReport.getTraineeName());
        payload.setBatchCode(batchCode);
//        payload.setLabName(labTestName);
        payload.setTotalScore(totalScore);
        payload.setGotScore(gotScore);
        payload.setTotalLabQuestions(questionListViaLabId.size());
        payload.setTotalSkipQuestions(totalSkipQuestions);
        payload.setTotalCorrectQuestions(totalCorrectQuestions);
        payload.setTotalIncorrectQuestions(totalIncorrectQuestions);
        payload.setPercentage(percentage);
        payload.setDetailReportPayloads(detailReportPayloads);
        return ResponseEntity.ok().body(payload);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam("password") String newPassword, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, HttpSession session) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return ResponseEntity.badRequest().body("Bad Request");
        }
        try {
            traineeService.changePassword(newPassword,getUserId());
            session.invalidate();
            return ResponseEntity.ok("Password changed! You need to login again");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
