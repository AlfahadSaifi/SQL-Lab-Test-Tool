package lab.controller.admin;

import lab.dto.lab.LabDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.evaluation.RecordLabAttempt;
import lab.entity.evaluation.RecordLabTestAttempt;
import lab.entity.lab.QuestionStatus;
import lab.entity.lab.Status;
import lab.gobal.LabTestByTraineePayload;
import lab.gobal.MergeLabPayload;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.batch.BatchIdCode;
import lab.payload.lab.LabPayload;
import lab.payload.lab.LabSummary;
import lab.payload.labTest.LabTestPayload;
import lab.payload.report.*;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lab.gobal.ReportPayload.*;
import static lab.gobal.TraineeReport.mergeLabPayloads;
import static lab.gobal.TraineeReport.mergeLabTestPayloads;

@Controller
@RequestMapping("/admin")
@SessionAttributes("batchId")
public class AdminReports {

    @Autowired
    private LabService labService;

    @Autowired
    private LabTestService labTestService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private ReportService reportService;

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/reports")
    public String viewAllReports(Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        int batchCount = reportService.getBatchCount(getUserName());
        model.addAttribute("batchSize", batchCount);
        int labCount = reportService.getLabCount(getUserName());
        model.addAttribute("labSize", labCount);
        int labTestCount = reportService.getLabTestCount(getUserName());
        model.addAttribute("labTestSize", labTestCount);
        int traineeCount = reportService.getTraineeCount();
        model.addAttribute("traineeSize", traineeCount);
        return "pages/reports/AllReports";
    }

    @GetMapping("/viewlabreport")
    public String viewBatchesForTestReport(Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<BatchIdCode> testBatchList = batchService.getAllBatchIdCode();
        model.addAttribute("testLabBatches", testBatchList);
        return "pages/reports/viewLabBatch";
    }

    @GetMapping("/batchLabs")
    public String testAssignedToBatch(Model model, @RequestParam("batchId") int batchId, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabPayload> labList = labService.getLabByBatch(batchId);
        model.addAttribute("labs", labList);
        model.addAttribute("batchId", batchId);
        return "pages/reports/labAssigned";
    }

    @RequestMapping("/viewLabToShowReport")
    public String getReportsLabs(Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabDto> labDtoList = labService.getLabs(getUserName());
        model.addAttribute("labs", labDtoList);
        return "pages/reports/viewLabToShowReport";
    }


    @RequestMapping("/viewReports")
    public String getReports(Model model, @RequestParam("labId") int labId, @RequestParam("batchId") int batchId, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabSummary> labSummaries = labService.getLabByIdBatch(labId, batchId);
        model.addAttribute("labReport", labSummaries);
        model.addAttribute("batchId", batchId);
        return "pages/reports/viewReports";
    }

    @RequestMapping("/viewDetailedLabReports")
    public String getDetailedLabReports(Model model, @RequestParam("traineeId") String traineeId, @RequestParam("labId") int labId, @RequestParam("batchId") int batchId, @RequestParam("labName") String labName, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            LabReport labReports = labService.getLabReportInfoByTraniee(labId, batchId, traineeId);
            List<QuestionPointsPayLoad> questionListViaLabId = labService.getQuestionListViaLabId(labId);
            List<RecordLabAttempt> recordLabAttempts = labService.getReportsOfTraineeByLabId(traineeId, labId);
            BatchIdCode batchIdCodeById = batchService.getBatchIdCodeById(labReports.getLabInfo().getBatch());
            if (batchIdCodeById == null || labReports == null || (recordLabAttempts == null && recordLabAttempts.isEmpty())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Data Not Available");
                return "pages/reports/viewDetailedLabReport";
            }
            String batchCode = "";
            if (batchIdCodeById != null) {
                batchCode = batchIdCodeById.getBatchCode();
            }
            int totalQuestions = questionListViaLabId.size();
            int totalCorrectQuestions = 0;
            int totalIncorrectQuestions = 0;
            int totalSkipQuestions = 0;
            double totalScore = 0.0;
            double gotScore = 0.0;
            for (QuestionPointsPayLoad quesPayload : questionListViaLabId) {
                totalScore += quesPayload.getQuestionPoints();
            }
            for (RecordLabAttempt record : recordLabAttempts) {
                gotScore += record.getTraineeCurrentQuestionPoints();
            }
            for (QuestionStatus questionStatus : labReports.getLabInfo().getQuestionStatusList()) {
                if (questionStatus.getStatus() == Status.CORRECT) {
                    totalCorrectQuestions++;
                } else if (questionStatus.getStatus() == Status.INCORRECT) {
                    totalIncorrectQuestions++;
                }
            }

            totalSkipQuestions = totalQuestions - (totalCorrectQuestions + totalIncorrectQuestions);
            List<DetailReportPayload> detailReportPayloads = convertToDetailLabReportPayload(questionListViaLabId, recordLabAttempts);
            model.addAttribute("traineeId", traineeId);
            model.addAttribute("traineeName", labReports.getTraineeName());
            model.addAttribute("batchCode", batchCode);
            model.addAttribute("labName", labName);
            model.addAttribute("totalScore", totalScore);
            model.addAttribute("gotScore", gotScore);
            model.addAttribute("totalLabQuestions", totalQuestions);
            model.addAttribute("totalSkipQuestions", totalSkipQuestions);
            model.addAttribute("totalCorrectQuestions", totalCorrectQuestions);
            model.addAttribute("totalIncorrectQuestions", totalIncorrectQuestions);
            model.addAttribute("detailReportPayloads", detailReportPayloads);
            return "pages/reports/viewDetailedLabReport";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Data Not Available");
            return "pages/reports/viewDetailedLabReport";
        }
    }

    @RequestMapping("/viewDetailedReports")
    public String getDetailedReports(Model model, @RequestParam("traineeId") String traineeId, @RequestParam("labTestId") int labTestId, @RequestParam("batchId") int batchId, @RequestParam("labTestName") String labTestName, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            LabTestReport labTestReport = labTestService.getLabTestReportInfoByTraniee(labTestId, batchId, traineeId);
            List<QuestionPointsPayLoad> questionListViaLabId = labTestService.getQuestionListViaLabTestId(labTestId);
            List<RecordLabTestAttempt> recordLabTestAttempts = labTestService.getReportsOfTraineeByLabTestId(traineeId, labTestId);

            BatchIdCode batchIdCodeById = batchService.getBatchIdCodeById(labTestReport.getLabTestInfo().getBatch());
            String batchCode = "";
            if (batchIdCodeById != null) {
                batchCode = batchIdCodeById.getBatchCode();
            }
            if (batchIdCodeById == null || labTestReport == null && recordLabTestAttempts == null && recordLabTestAttempts.size() == 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Data Not Available");
                return "pages/reports/viewDetailedLabReport";
            }
            int totalQuestions = questionListViaLabId.size();
            int totalCorrectQuestions = 0;
            int totalIncorrectQuestions = 0;
            int totalSkipQuestions = 0;
            double totalScore = 0.0;
            double gotScore = 0.0;
            for (QuestionPointsPayLoad quesPayload : questionListViaLabId) {
                totalScore += quesPayload.getQuestionPoints();
            }
            for (RecordLabTestAttempt record : recordLabTestAttempts) {
                gotScore += record.getTraineeCurrentQuestionPoints();
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
            model.addAttribute("traineeId", traineeId);
            model.addAttribute("traineeName", labTestReport.getTraineeName());
            model.addAttribute("batchCode", batchCode);
            model.addAttribute("labName", labTestName);
            model.addAttribute("totalScore", totalScore);
            model.addAttribute("gotScore", gotScore);
            model.addAttribute("totalLabQuestions", questionListViaLabId.size());
            model.addAttribute("totalSkipQuestions", totalSkipQuestions);
            model.addAttribute("totalCorrectQuestions", totalCorrectQuestions);
            model.addAttribute("totalIncorrectQuestions", totalIncorrectQuestions);
            model.addAttribute("detailReportPayloads", detailReportPayloads);

            double percentage = (gotScore / totalScore) * 100;
            model.addAttribute("percentage", percentage);
            if (percentage >= labTestReport.getPassPercentage()) {
                model.addAttribute("result", "PASS");
            } else if (percentage < labTestReport.getPassPercentage()) {
                model.addAttribute("result", "FAIL");
            }
            return "pages/reports/viewDetailedReport";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Data Not Available");
            return "pages/reports/viewDetailedReport";
        }
    }

    @RequestMapping("labReport")
    public String viewLabReport(@RequestParam("labId") int labId, @RequestParam("batchId") int batchId, @RequestParam("batchName") String batchName, @RequestParam("labName") String labName, Model model) {
        List<LabReport> labReports = labService.getLabReportInfo(labId, batchId);
        List<QuestionPointsPayLoad> questionListViaLabId = labService.getQuestionListViaLabId(labId);
        double totalScore = 0.0;
        for (QuestionPointsPayLoad questionPoint : questionListViaLabId) {
            totalScore += questionPoint.getQuestionPoints();
        }
        LabReportByBatch labReportByBatches = generateLabReports(labReports, questionListViaLabId, batchName, totalScore);
        labReportByBatches.setLabName(labName);
        model.addAttribute("labReportPayload", labReportByBatches);
        return "pages/batch/labReport";
    }

    @RequestMapping("labTestReport")
    public String viewLabTestReport(@RequestParam("labTestId") int labTestId, @RequestParam("batchId") int batchId, @RequestParam("batchName") String batchName, @RequestParam("labTestName") String labTestName, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabTestReport> labTestReports = labTestService.getLabTestInfo(labTestId, batchId);
        List<QuestionPointsPayLoad> questionListViaLabTestId = labTestService.getQuestionListViaLabTestId(labTestId);
        if (questionListViaLabTestId.size() == 0) return "pages/batch/labTestReport";

        double passPercentage = questionListViaLabTestId.get(0).getPassPercentage();
        double totalScore = 0.0;
        for (QuestionPointsPayLoad questionPoint : questionListViaLabTestId) {
            totalScore += questionPoint.getQuestionPoints();
        }
        LabTestReportByBatch labTestReportByBatches = generateLabTestReports(labTestReports, questionListViaLabTestId, batchName, totalScore);
        labTestReportByBatches.setLabTestName(labTestName);
        labTestReportByBatches.setPassPercentage(passPercentage);
        model.addAttribute("labTestReportPayload", labTestReportByBatches);
        return "pages/batch/labTestReport";
    }

    @GetMapping("/traineeDetailReport")
    public String traineeDetailReport(@RequestParam("traineeId") String traineeId, @RequestParam("batchId") int batchId, Model model, HttpServletRequest httpServletRequest,RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            // completed , passMarks, CORRECT, INCORRECT
            List<LabTestPassPercentage> labTestPassPercentages = labTestService.getAllLabTestReportInfoByTrainee(batchId, traineeId);

            // total obtained marks , labTest id , trainee id
            List<TraineeMarksPayload> traineeMarksPayloads = labTestService.getTraineeMarksByTraineeIdBatchId(batchId, traineeId);

            // All Assessment , total marks
            List<LabTestPayload> labTestPayloads = labTestService.getLabTestsByBatchWithTotalMarks(batchId);
            // All Lab
            List<LabPayload> labPayloads = labService.getLabByBatchWithTotalMarks(batchId);
            //  All Assessment ,  total marks
            List<TraineeLabMarksPayload> traineeLabMarksPayloads = labService.getTraineeMarksByTraineeIdBatchId(batchId, traineeId);
            TraineeDto trainee = traineeService.getTraineeByUserName(traineeId);
            List<MergeLabPayload> mergeLabPayloads = new ArrayList<>();
            if (labPayloads.size() > 0) {
                mergeLabPayloads = mergeLabPayloads(labPayloads, traineeLabMarksPayloads, traineeId);
            }
            List<LabTestByTraineePayload> labTestByTraineePayload = new ArrayList<>();
            if (labTestPassPercentages.size() > 0 && labTestPayloads.size() > 0) {
                labTestByTraineePayload = mergeLabTestPayloads(labTestPassPercentages, labTestPayloads, traineeMarksPayloads, traineeId);
            }
            int totalPass = 0;
            for (LabTestByTraineePayload labTestByTrainee : labTestByTraineePayload) {
                double obtainedPercent = (labTestByTrainee.getObtainedMarks() / labTestByTrainee.getTotalLabTestPoints()) * 100;
                if (obtainedPercent >= labTestByTrainee.getPassPercentage()) {
                    totalPass++;
                }
            }
            BatchIdCode batchIdCode = batchService.getBatchIdCodeById(batchId);
            model.addAttribute("lab", mergeLabPayloads);
            model.addAttribute("labTest", labTestByTraineePayload);
            model.addAttribute("totalLabSize", labPayloads.size());
            model.addAttribute("totalLabTestSize", labTestPayloads.size());
            model.addAttribute("totalPassLabTest", totalPass);
            model.addAttribute("traineeId", traineeId);
            model.addAttribute("batchName", batchIdCode.getBatchCode());
            model.addAttribute("batchId", batchId);
            model.addAttribute("traineeName", trainee.getName());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage","Data Not Available");
        }
        return "pages/trainee/traineeBatchData";
    }

    @GetMapping("/detailedTraineeLabReport")
    public String traineeLabRecords(@RequestParam("id") String traineeId, @RequestParam("id1") int batchId, @RequestParam("id2") String traineeName, @RequestParam("id3") String batchCode, Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabPayload> labByBatch = labService.getLabByBatch(batchId);
        List<TraineeReportPayload> list = new ArrayList<>();
        for (LabPayload labPayload : labByBatch) {
            TraineeReportPayload traineeReportPayload = new TraineeReportPayload();
            try {
                LabReport labReports = labService.getLabReportInfoByTraniee(labPayload.getLabId(), batchId, traineeId);
                List<QuestionPointsPayLoad> questionListViaLabId = labService.getQuestionListViaLabId(labPayload.getLabId());
                double totalScore = 0.0;
                for (QuestionPointsPayLoad questionPoint : questionListViaLabId) {
                    totalScore += questionPoint.getQuestionPoints();
                }
                Map<Integer, Integer> questionPointsMap = questionListViaLabId.stream()
                        .collect(Collectors.toMap(QuestionPointsPayLoad::getQuestionId, QuestionPointsPayLoad::getQuestionPoints));
                double obtainedScore = 0;
                for (QuestionStatus questionStatus : labReports.getLabInfo().getQuestionStatusList()) {
                    if (questionStatus.getStatus() == lab.entity.lab.Status.CORRECT) {
                        obtainedScore += questionPointsMap.getOrDefault(questionStatus.getQuestionId(), 0);
                    }
                }
                traineeReportPayload.setLabReports(labReports);
                traineeReportPayload.setLabName(labPayload.getLabName());
                traineeReportPayload.setObtainedScore(obtainedScore);
                traineeReportPayload.setTotalScore(totalScore);
                list.add(traineeReportPayload);
            } catch (NullPointerException e) {
                e.printStackTrace();
                List<QuestionPointsPayLoad> questionListViaLabId = labService.getQuestionListViaLabId(labPayload.getLabId());
                if (questionListViaLabId != null) {
                    double totalScore = 0.0;
                    for (QuestionPointsPayLoad questionPoint : questionListViaLabId) {
                        totalScore += questionPoint.getQuestionPoints();
                        traineeReportPayload.setTotalScore(totalScore);
                    }
                }
                traineeReportPayload.setLabReports(new LabReport());
                traineeReportPayload.setLabName(labPayload.getLabName());
                traineeReportPayload.setObtainedScore(0.0);
                list.add(traineeReportPayload);
            }
        }
        model.addAttribute("batchId", batchId);
        model.addAttribute("batchName", batchCode);
        model.addAttribute("traineeId", traineeId);
        model.addAttribute("traineeName", traineeName);
        model.addAttribute("TraineeLabReport", list);
        return "pages/trainee/traineeLabRecord";
    }

    @GetMapping("/detailedTraineeLabTestReport")
    public String TraineeLabTestRecords(@RequestParam("id") String traineeId, @RequestParam("id1") int batchId, @RequestParam("id2") String traineeName, @RequestParam("id3") String batchCode, Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabTestPayload> labByBatch = labTestService.getLabTestsByBatch(batchId);
        List<TraineeReportPayload> list = new ArrayList<>();
        for (LabTestPayload labPayload : labByBatch) {
            TraineeReportPayload traineeReportPayload = new TraineeReportPayload();
            try {
                LabTestReport labtestReports = labTestService.getLabTestReportInfoByTraniee(labPayload.getLabTestId(), batchId, traineeId);
                if (labtestReports != null) {
                    List<QuestionPointsPayLoad> questionListViaLabId = labTestService.getQuestionListViaLabTestId(labPayload.getLabTestId());
                    double totalScore = 0.0;
                    if (questionListViaLabId != null) {
                        for (QuestionPointsPayLoad questionPoint : questionListViaLabId) {
                            totalScore += questionPoint.getQuestionPoints();
                        }

                        Map<Integer, Integer> questionPointsMap = questionListViaLabId.stream()
                                .collect(Collectors.toMap(QuestionPointsPayLoad::getQuestionId, QuestionPointsPayLoad::getQuestionPoints));
                        double obtainedScore = 0;
                        for (QuestionStatus questionStatus : labtestReports.getLabTestInfo().getQuestionStatusList())
                            if (questionStatus.getStatus() == lab.entity.lab.Status.CORRECT)
                                obtainedScore += questionPointsMap.getOrDefault(questionStatus.getQuestionId(), 0);

                        traineeReportPayload.setObtainedScore(obtainedScore);
                        traineeReportPayload.setTotalScore(totalScore);

                        double percentage = 0;
                        if (totalScore != 0) {
                            percentage = (obtainedScore / totalScore) * 100;
                        }
                        double passPercentage = labtestReports.getPassPercentage();
                        if (percentage < passPercentage) {
                            traineeReportPayload.setLabTestResult("Fail");
                        } else {
                            traineeReportPayload.setLabTestResult("Pass");
                        }
                        traineeReportPayload.setLabTestReports(labtestReports);
                        traineeReportPayload.setLabTestName(labPayload.getLabTestName());
                        list.add(traineeReportPayload);
                    }
                } else {
                    traineeReportPayload.setLabTestName(labPayload.getLabTestName());
                    List<QuestionPointsPayLoad> questionListViaLabId = labTestService.getQuestionListViaLabTestId(labPayload.getLabTestId());
                    double totalScore = 0;
                    if (questionListViaLabId != null) {
                        for (QuestionPointsPayLoad questionPoint : questionListViaLabId) {
                            totalScore += questionPoint.getQuestionPoints();
                        }
                        Map<Integer, Integer> questionPointsMap = questionListViaLabId.stream()
                                .collect(Collectors.toMap(QuestionPointsPayLoad::getQuestionId, QuestionPointsPayLoad::getQuestionPoints));
                        double obtainedScore = 0;
                        for (QuestionStatus questionStatus : labtestReports.getLabTestInfo().getQuestionStatusList())
                            if (questionStatus.getStatus() == lab.entity.lab.Status.CORRECT)
                                obtainedScore += questionPointsMap.getOrDefault(questionStatus.getQuestionId(), 0);
                        traineeReportPayload.setTotalScore(totalScore);
                    }
                    traineeReportPayload.setLabTestReports(new LabTestReport());
                    traineeReportPayload.setObtainedScore(0.0);
                    traineeReportPayload.setLabTestResult("N/A");
                    list.add(traineeReportPayload);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            model.addAttribute("batchId", batchId);
            model.addAttribute("batchName", batchCode);
            model.addAttribute("traineeId", traineeId);
            model.addAttribute("traineeName", traineeName);
            model.addAttribute("TraineeLabReport", list);
        }
        return "pages/trainee/traineeLabTestRecord";
    }
}