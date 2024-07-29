package lab.controller.admin;

import lab.dto.admin.BatchDto;
import lab.entity.lab.QuestionStatus;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.labTest.LabTestPayload;
import lab.payload.report.LabTestReport;
import lab.payload.report.LabTestReportByBatch;
import lab.payload.report.TestDetail;
import lab.service.batches.BatchService;
import lab.service.lab.LabService;
import lab.service.labtest.LabTestService;
import lab.service.reports.ReportService;
import lab.service.trainee.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lab.gobal.ReportPayload.generateLabTestReports;

@Controller
@RequestMapping("/admin")
public class TestReport {
    @Autowired
    private LabService labService;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private LabTestService labTestService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private ReportService reportService;

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/testReport")
    public String viewBatchesForTestReport(Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<BatchDto> testBatchList = batchService.getBatchs(getUserName());
        model.addAttribute("testBatchList", testBatchList);
        return "pages/testReport/viewTestBatch";
    }

    @GetMapping("/batchTests")
    public String testAssignedToBatch(@RequestParam("id") int batchId, @RequestParam("batchCode") String batchCode, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabTestPayload> labTestDtoList = labTestService.getLabTestsByBatch(batchId);
        model.addAttribute("tests", labTestDtoList);
        model.addAttribute("batchId", batchId);
        model.addAttribute("batchCode", batchCode);
        return "pages/testReport/testAssigned";
    }

    @GetMapping("/testReportSummary")
    public String testReportSummary(@RequestParam("id") int labTestId, @RequestParam("batchId") int batchId, @RequestParam("batchName") String batchName, @RequestParam("labTestName") String labTestName, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            List<LabTestReport> labTestReports = labTestService.getLabTestInfo(labTestId, batchId);
            List<QuestionPointsPayLoad> questionListViaLabTestId = labTestService.getQuestionListViaLabTestId(labTestId);
            if (questionListViaLabTestId.size() == 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Data not found");
                return "pages/batch/labTestReport";
            }
            double passPercentage = questionListViaLabTestId.get(0).getPassPercentage();
            double totalScore = 0.0;
            for (QuestionPointsPayLoad questionPoint : questionListViaLabTestId) {
                totalScore += questionPoint.getQuestionPoints();
            }
            LabTestReportByBatch labTestReportByBatches = generateLabTestReports(labTestReports, questionListViaLabTestId, batchName, totalScore);
            labTestReportByBatches.setLabTestName(labTestName);
            labTestReportByBatches.setPassPercentage(passPercentage);
            model.addAttribute("labTestReportPayload", labTestReportByBatches);
            model.addAttribute("batchId", batchId);
            model.addAttribute("labTestId", labTestId);
            model.addAttribute("labTestName", labTestName);
            return "pages/testReport/labTestReportSummary";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Data not found");
            return "pages/testReport/labTestReportSummary";
        }
    }
}
