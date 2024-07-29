package lab.controller.admin;

import lab.dto.admin.BatchDto;
import lab.dto.trainee.TraineeDetailDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.lab.QuestionStatus;
import lab.payload.answer.QuestionPointsPayLoad;
import lab.payload.batch.BatchIdCode;
import lab.payload.lab.LabPayload;
import lab.payload.labTest.LabTestPayload;
import lab.payload.report.LabReport;
import lab.payload.report.LabTestReport;
import lab.payload.report.TraineePayload;
import lab.payload.report.TraineeReportPayload;
import lab.service.admin.AdminService;
import lab.service.admin.AdminWithTxn;
import lab.service.batches.BatchService;
import lab.service.lab.LabService;
import lab.service.labtest.LabTestService;
import lab.service.trainee.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminTrainee {
    @Autowired
    private AdminService adminService;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private LabService labService;
    @Autowired
    private LabTestService labTestService;

    //to save the data with Txn
    @Autowired
    private AdminWithTxn adminWithTxn;
    private int batchId;

    private String getUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping("/registerTrainee")
    public String registerTrainee(Model model, @RequestParam("batchId") int batchId, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setTraineeDetail(new TraineeDetailDto());
        model.addAttribute("trainee", traineeDto);
//        model.addAttribute("traineeDetails", traineeDetailDto);
        model.addAttribute("batchId", batchId);
        return "pages/admin/traineeRegister";
    }

    @GetMapping("/registerTraineeMore")
    public String registerTraineeMore(@RequestParam("id") int batchId, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        TraineeDto traineeDto = new TraineeDto();
        model.addAttribute("trainee", traineeDto);
        model.addAttribute("batchId", batchId);
        return "pages/admin/traineeRegisterMore";
    }

    @GetMapping("/traineeReport")
    public String viewTraineeReport(Model model) {
        List<TraineePayload> allTrainee = traineeService.getAllTrainee();
        model.addAttribute("TraineeList", allTrainee);
        return "pages/trainee/viewBatchTrainee";
    }

    @GetMapping("/batchTrainees")
    public String BatchTraineesRecords(@RequestParam("id") int batchId, @RequestParam("batchCode") String batchCode, @RequestParam("id1") String traineeId, @RequestParam("id2") String traineeName, Model model) {
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
                double percentage = 0;
                if (totalScore != 0) {
                    percentage = (obtainedScore / totalScore) * 100;
                    traineeReportPayload.setPercentage(percentage);
                }

                traineeReportPayload.setPercentage(percentage);
                traineeReportPayload.setLabReports(labReports);
                traineeReportPayload.setLabName(labPayload.getLabName());
                traineeReportPayload.setObtainedScore(obtainedScore);
                traineeReportPayload.setTotalScore(totalScore);
                list.add(traineeReportPayload);
            } catch (NullPointerException e) {
                e.printStackTrace();
                List<QuestionPointsPayLoad> questionListViaLabId = labService.getQuestionListViaLabId(labPayload.getLabId());
                double totalScore = 0.0;
                if (questionListViaLabId != null) {
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
        //LabTest:
        List<LabTestPayload> labByBatch1 = labTestService.getLabTestsByBatch(batchId);
        List<TraineeReportPayload> list1 = new ArrayList<>();
        for (LabTestPayload labPayload : labByBatch1) {
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
                            traineeReportPayload.setPercentage(percentage);
                        }

                        double passPercentage = labtestReports.getPassPercentage();
                        if (percentage < passPercentage) {
                            traineeReportPayload.setLabTestResult("Fail");
                        } else {
                            traineeReportPayload.setLabTestResult("Pass");
                        }

                        traineeReportPayload.setLabTestReports(labtestReports);
                        traineeReportPayload.setLabTestName(labPayload.getLabTestName());

                        list1.add(traineeReportPayload);
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
                    list1.add(traineeReportPayload);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        long pass = list1.stream().filter(x -> x.getLabTestResult().equals("Pass")).count();
        model.addAttribute("LabSize", list.size());
        model.addAttribute("labTestSize", list1.size());
        model.addAttribute("totalPassesLabTest", pass);
        model.addAttribute("batchId", batchId);
        model.addAttribute("batchName", batchCode);
        model.addAttribute("traineeId", traineeId);
        model.addAttribute("traineeName", traineeName);
        model.addAttribute("TraineeLabReport", list);
        model.addAttribute("TraineeLabTestReport", list1);
        return "pages/trainee/traineeBatchData";
    }


    @GetMapping("/editTraineeDetail")
    public String editTraineeMoreDetail(@RequestParam("id") String employeeId, @RequestParam("batchId") int batchId, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        TraineeDetailDto traineeDetailDto = adminService.getTranieeDetailByEmpId(employeeId);
        model.addAttribute("batchId", batchId);
        if (traineeDetailDto != null) {
            model.addAttribute("traineeDetail", traineeDetailDto);
            model.addAttribute("employeeId", employeeId);
            return "pages/admin/editTraineeDetail";
        }
        model.addAttribute("traineeDetail", new TraineeDetailDto());
        model.addAttribute("employeeId", employeeId);
        redirectAttributes.addFlashAttribute("errorMessage", "Data not found.");
        return "pages/admin/editTraineeDetail";
    }

    @PostMapping("/editTraineeDetail")
    public String postEditTraineeMoreDetail(@ModelAttribute TraineeDetailDto traineeDetailDto, @RequestParam("batchId") int batchId, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        traineeDetailDto = adminService.editTranieeDetailById(traineeDetailDto);
        model.addAttribute("traineeDetail", traineeDetailDto);
        model.addAttribute("employeeId", traineeDetailDto.getEmployeeId());
        redirectAttributes.addFlashAttribute("successMessage", "Trainee edit successfully.");
        return "redirect:/admin/viewTrainee?id=" + batchId;
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("employeeId") String employeeId, @RequestParam("password") String newPassword, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        BatchIdCode batchIdCode = batchService.getBatchIdCodeByTrainee(employeeId);
        int batchId = batchIdCode.getId();
        try {
            adminService.changePassword(newPassword, employeeId);
            redirectAttributes.addFlashAttribute("successMessage", "Change password successful");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to Change password");
        }
        return "redirect:/admin/editTraineeDetail?id=" + employeeId + "&batchId=" + batchId;
    }

    @PostMapping("/registerTraineeViaExcel")
    public String registerTrainee1(@RequestParam("file") MultipartFile file, @RequestParam("id") int batchId, RedirectAttributes redirectAttributes) throws IOException {
        try {
            String s = adminService.registerTraineeViaExcel(file, batchId);
            if (s.equals("Successfully")) {
                redirectAttributes.addFlashAttribute("successMessage", "Trainee added successfully." + s);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid data in excel " + s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid/Employee already exist check you excel file.");
        }
        return "redirect:/admin/viewTrainee?id=" + batchId;
    }

    @PostMapping("/registerTraineeViaForm")
    public String registerStudent2(@ModelAttribute("trainee") @Valid TraineeDto traineeDto, BindingResult bindingResult, @RequestParam("batchId") int batchId, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid field.");
            return "redirect:/admin/viewTrainee?id=" + batchId;
        }
        try {
            TraineeDto trainee = adminService.createTraineeInUser(traineeDto, batchId, getUserId());
            model.addAttribute("trainee", traineeDto);
            redirectAttributes.addFlashAttribute("successMessage", "Trainee added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Trainee already registered ");
        }
        return "redirect:/admin/viewTrainee?id=" + batchId;
    }
    // test

    @PostMapping("/addMoreTraineeDetails")
    public String addTraineeMoreDetails(@ModelAttribute("traineeDetails") @Valid TraineeDetailDto traineeDetails, BindingResult bindingResult, @RequestParam("id") int batchId, @RequestParam("employeeId") String employeeId, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        TraineeDto traineeByEmployeeId = null;
        try {
            traineeByEmployeeId = traineeService.getTraineeByEmployeeId(employeeId);
            traineeByEmployeeId.setTraineeDetail(traineeDetails);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to fetch trainee");
            return "redirect:/admin/viewTrainee?id=" + batchId;
        }
        try {
            adminWithTxn.registerTrainee(traineeByEmployeeId, batchId);
            redirectAttributes.addFlashAttribute("successMessage", "Trainee added successfully." + traineeByEmployeeId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Trainee already registered " + e.getMessage() + traineeByEmployeeId);
        }
        return "redirect:/admin/viewTrainee?id=" + batchId;
    }

    @GetMapping("/viewBatch")
    public String viewBatch(Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<BatchDto> batchList = batchService.getBatchs(getUserId());
        model.addAttribute("batchList", batchList);
        return "pages/batch/viewBatch";
    }

    @GetMapping("/viewBatches")
    public String viewBatches(Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<BatchDto> batchList = batchService.getBatchs(getUserId());
        model.addAttribute("batchList", batchList);
        return "pages/batch/viewBatches";
    }

    @GetMapping("/viewTrainee")
    public String viewTrainee(Model model, @RequestParam("id") int batchId) {
        List<TraineeDto> traineeDtoList = batchService.getTraineeByBatchId(batchId);
        model.addAttribute("trainees", traineeDtoList);
        model.addAttribute("batchId", batchId);
        return "pages/trainee/trainee";
    }

    @GetMapping("/viewOldTrainee")
    public String viewTrainee1(Model model, @RequestParam("id") int batchId) {

        List<TraineeDto> traineeDtoList = batchService.getTraineeByBatchId(batchId);
        model.addAttribute("trainees", traineeDtoList);
        model.addAttribute("batchId", batchId);
        return "pages/trainee/viewOldTrainee";
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }
}