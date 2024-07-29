package lab.controller.trainee;

import lab.dto.evaluation.RecordLabAttemptDto;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.entity.evaluation.RecordLabAttempt;
import lab.payload.answer.AnswerResponseMessage;
import lab.payload.lab.LabPayload;
import lab.repository.labtest.LabTestRepo;
import lab.service.lab.LabService;
import lab.service.labEngine.LabEngineService;
import lab.service.labtest.LabTestService;
import lab.service.reports.ReportService;
import lab.service.trainee.TraineeService;
import lab.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/trainee")
public class TraineeHome {
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
    private UserService userService;
    private String getUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    public String getUser(String userId){
        return userService.getUserNameById(userId);
    }
    @RequestMapping("/")
    public String lab(Model model, @RequestParam(required = false, value = "id") String labId1, @RequestParam(required = false, value = "qid") String questionId1, @RequestParam(required = false, value = "testId") String testId1, @RequestParam(required = false, value = "testQid") String testQuestionId1, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabPayload> labs = labService.getTraineeLab(getUserId());
        LabDto labDto;
        List<LabTestDto> labTestDtoList = labTestRepo.getTestsByTraineeUsername(getUserId());
        LabTestDto labTestDto = null;
        QuestionDto questionDto = null;
        List<QuestionDto> questionDtoList = null;
        QuestionDto questionDtoTest = null;
        List<QuestionDto> questionDtoListTest = null;
        if (labId1 != null) {
            int labId = Integer.parseInt(labId1);
            labDto = labService.getLabById(labId);
            questionDtoList = labDto.getQuestions();
        }
        if (testId1 != null) {
            int testId = Integer.parseInt(testId1);
            labTestDto = labTestService.getTestById(testId);
            questionDtoListTest = labTestDto.getQuestions();
        }
        if (testQuestionId1 != null) {
            int questionId = Integer.parseInt(testQuestionId1);
            assert questionDtoListTest != null;
            for (QuestionDto q : questionDtoListTest) {
                if (q.getQuestionId() == questionId) {
                    questionDtoTest = q;
                    break;
                }
            }
        }
        if (questionId1 != null) {
            int questionId = Integer.parseInt(questionId1);
            assert questionDtoList != null;
            for (QuestionDto q : questionDtoList) {
                if (q.getQuestionId() == questionId) {
                    questionDto = q;
                    break;
                }
            }
        }
        model.addAttribute("question", questionDto);
        model.addAttribute("labId", labId1);
        model.addAttribute("questionList", questionDtoList);
        model.addAttribute("output", "Output will show here......");
        model.addAttribute("labs", labs);
        model.addAttribute("testQuestion", questionDtoTest);
        model.addAttribute("testId", testId1);
        model.addAttribute("testQuestion", questionDtoTest);
        model.addAttribute("testQuestionList", questionDtoListTest);
        model.addAttribute("tests", labTestDtoList);
        model.addAttribute("test", labTestDto);
        model.addAttribute("username", getUserId());
        return "pages/trainee/home";
    }
    @PostMapping("/")
    public String questionSubmit(Model model, @RequestParam(value = "id") String labId1, @RequestParam(value = "qid") String questionId1, @RequestParam(value = "query") String query) {
        query = query.trim();
        if (questionId1 != null) {
            String output;
            AnswerResponseMessage answerResponseMessage = labEngineService.submitAns(questionId1, query);
            if (answerResponseMessage.isCorrect()) {
                output = "Correct";
            } else if(answerResponseMessage.isIncorrect()){
                output = "Incorrect";
            }
            else {
                output = answerResponseMessage.getIsInvalidSyntax();
            }
            List outputList = answerResponseMessage.getOutput();
            List<LabPayload> labs = labService.getTraineeLab(getUserId());
            LabDto labDto;
            QuestionDto questionDto = null;
            List<QuestionDto> questionDtoList = null;
            int labId = 0;
            if (labId1 != null) {
                labId = Integer.parseInt(labId1);
                labDto = labService.getLabById(labId);
                questionDtoList = labDto.getQuestions();
            }
            int questionId = Integer.parseInt(questionId1);
            assert questionDtoList != null;
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
            recordLabAttemptDto.setIncorrectAttempt(inCorrect + 1);
            reportService.saveRecordAttempt(recordLabAttemptDto, query, answerResponseMessage.isCorrect());
            model.addAttribute("question", questionDto);
            model.addAttribute("output", output);
            model.addAttribute("outputList", outputList);
            model.addAttribute("labId", labId1);
            model.addAttribute("questionList", questionDtoList);
            model.addAttribute("labs", labs);
            model.addAttribute("userId", getUserId());
        }
        return "pages/trainee/home";
    }
    @RequestMapping("/submitLab")
    public String submitLab(@RequestParam(value = "id") String labId1, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        int labId = Integer.parseInt(labId1);
        reportService.submitLab(labId, getUserId());
        return "pages/trainee/instruction";
    }
    @RequestMapping("/viewLabReport")
    public String viewLabReport(@RequestParam("labId") int labId,HttpServletRequest httpServletRequest,Model model) {
        String traineeId=getUserId();
        List<RecordLabAttempt> recordLabAttemptList = labService.getLabReport(labId, traineeId);
        model.addAttribute("recordLabAttemptList",recordLabAttemptList);
        return "pages/traineeTest/viewLabReport";
    }
    @RequestMapping("/viewLabTestReport")
    public String viewLabTestReport(@RequestParam("labTestId") int labTestId,HttpServletRequest httpServletRequest,Model model) {
        model.addAttribute("labTestId`",labTestId);
        return "pages/traineeTest/viewReport";
    }
//    @GetMapping(value = "/download/{id}")
//    public ResponseEntity<?> downloadLabPdf(@PathVariable int id, HttpServletRequest httpServletRequest) {
//        String referer = httpServletRequest.getHeader("Referer");
//        if (referer == null) {
//            return null;
//        }
//        LabDto labDto = labService.getLabById(id);
//        if (labDto != null && labDto.getReferenceFile() != null) {
//            byte[] pdfData = labDto.getReferenceFile();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.parseMediaType("application/pdf"));
//            String filename = labDto.getLabName() + ".pdf";
//            headers.setContentDispositionFormData(filename, filename);
//            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//            ResponseEntity<byte[]> response = new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
//            return response;
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//    @GetMapping(value = "/downloadLabTest/{id}")
//    public ResponseEntity<?> downloadLabTestPdf(@PathVariable int id, HttpServletRequest httpServletRequest) {
//        String referer = httpServletRequest.getHeader("Referer");
//        if (referer == null) {
//            return null;
//        }
//        LabTestDto labTestDto = labTestService.getLabTestById(id);
//        if (labTestDto != null && labTestDto.getReferenceFile() != null) {
//            byte[] pdfData = labTestDto.getReferenceFile();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.parseMediaType("application/pdf"));
//            String filename = labTestDto.getLabTestName() + ".pdf";
//            headers.setContentDispositionFormData(filename, filename);
//            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//            ResponseEntity<byte[]> response = new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
//            return response;
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
}
