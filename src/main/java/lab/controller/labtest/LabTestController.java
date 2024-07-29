package lab.controller.labtest;

import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.entity.lab.LabTestInfo;
import lab.payload.batch.BatchIdCode;
import lab.service.batches.BatchService;
import lab.service.labtest.LabTestService;
import lab.service.questionBank.QuestionBankService;
import lab.service.trainee.TraineeService;
import lab.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class LabTestController {
    @Autowired
    private LabTestService labTestService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private UserService userService;



    private String getUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String getUserName(String userId) {
        return userService.getUserNameById(userId);
    }

    @RequestMapping("createLabTest")
    public String getCreateLabTest(Model model) {
        model.addAttribute("labTest", new LabTestDto());
        return "pages/labTest/createTestLab";
    }

    @PostMapping(value = "createLabTest")
    public String postCreateLabTest(@ModelAttribute("labTest") @Valid LabTestDto labTestDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        if (bindingResult.hasErrors()) {
            return "pages/lab/createLab";
        }
        try {
            labTestDto = labTestService.addLabTestToAdmin(labTestDto, getUserId());
            redirectAttributes.addFlashAttribute("successMessage", "Assessment added Successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/createLabTest";
        }
        return "redirect:/admin/viewLabTestQuestions?id=" + labTestDto.getLabTestId();
    }

    @GetMapping("viewLabTest1")
    public String viewLabTest(Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabTestDto> labTestDtoList = labTestService.getLabTests(getUserId());
        model.addAttribute("labTestList", labTestDtoList);
        return "pages/labTest/viewLabTest";
    }

    @GetMapping("viewLabTest")
    public String viewLabTest(@RequestParam("batchId") int batchId, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        BatchIdCode batchIdCode = batchService.getBatchIdCodeById(batchId);
        List<LabTestDto> labTestDtoList = batchService.getAssignTestBatchById(batchId);
        model.addAttribute("labTestList", labTestDtoList);
        model.addAttribute("batchId", batchId);
        model.addAttribute("batchCode", batchIdCode.getBatchCode());
        return "pages/labTest/viewLabTest";
    }

    @GetMapping("deleteLabTest")
    public String deleteLabTest(@RequestParam("id") int testLabId, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            boolean status = labTestService.deleteLabTest(testLabId, getUserId());
            redirectAttributes.addFlashAttribute("successMessage", "Lab deleted successfully !!");
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "lab delete fail !!");
        }
            return "redirect:/admin/viewAssignLabTest";
    }

    @GetMapping("viewLabTestQuestions")
    public String viewLabTestQuestions(@RequestParam("id") int labTestId, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        LabTestDto labTestDto = labTestService.getLabTestById(labTestId);
        if (labTestDto != null) {
            List<QuestionDto> questionDtoList = labTestDto.getQuestions();
            model.addAttribute("questionList", questionDtoList);
        }
        model.addAttribute("labTestId", labTestId);
        if (labTestDto != null) {
            model.addAttribute("labName", labTestDto.getLabTestName());
        }
        return "pages/labTest/viewLabTestQuestions";
    }

    @RequestMapping("/addLabTestQuestion")
    public String addQuestion(Model model, @RequestParam("id") int labTestId, @RequestParam("labTestName") String labTestName, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        QuestionDto questionDto = new QuestionDto();
        questionDto.setQuestionPoints(labTestService.getQuestionPoint(labTestId));
        model.addAttribute("question", questionDto);
        model.addAttribute("labTestId", labTestId);
        model.addAttribute("labTestName", labTestName);
        return "pages/labTest/addLabTestQuestion";
    }

    @PostMapping("/addLabTestQuestionViaForm")
    public String addQuestion(@ModelAttribute("question") QuestionDto questionDto, @RequestParam("id") int labId, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        labTestService.addQuestionInLabTest(questionDto, labId);
        redirectAttributes.addFlashAttribute("successMessage", "Question added successfully");
        return "redirect:/admin/viewLabTestQuestions?id=" + labId;
    }

    @RequestMapping("/editLabTestQuestion")
    public String editLabTestQuestion(@RequestParam("id") int id, @RequestParam("questionId") int questionId, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        // view edit the question
        QuestionDto questionDto = labTestService.getLabTestQuestion(id, questionId);
        model.addAttribute("question", questionDto);
        model.addAttribute("labId", id);
        return "pages/labTest/editLabTestQuestion";
    }

    @PostMapping("/editLabTestQuestionViaForm")
    public String editQuestionViaForm(@ModelAttribute("question") QuestionDto questionDto, @RequestParam("id") int labTestId, @RequestParam("questionId") int questionId, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            labTestService.editQuestionInLabTest(questionDto, labTestId);
            redirectAttributes.addFlashAttribute("successMessage", "Question edited successfully");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to edit question");
        }
        return "redirect:/admin/viewLabTestQuestions?id=" + labTestId;
    }

    @RequestMapping("/deleteLabTestQuestion")
    public String deleteLabTestQuestion(@RequestParam("id") int labTestId, @RequestParam("questionId") int questionId, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        // delete Assessment Question
        try {
            labTestService.deleteLabTestQuestion(labTestId, questionId);
            redirectAttributes.addFlashAttribute("successMessage", "Question deleted successfully");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete question");
        }
        return "redirect:/admin/viewLabTestQuestions?id=" + labTestId;
    }

    @GetMapping("lab/assignTestLab")
    public String openAssignTest(Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        model.addAttribute("tests", labTestService.getLabTests(getUserId()));
        model.addAttribute("batches", batchService.getBatchs(getUserId()));
        return "pages/admin/assignTest";
    }

    @PostMapping("lab/assignTest")
    public String assignTestProcess(@RequestParam("labTestId") int labTestId, @RequestParam("batch") int batchId, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("duration") int duration, @RequestParam("passPercentage") double percentage, @RequestParam("negativeMarkingFactor") double negativeMarkingFactor, @RequestParam("assignedBy") String assignedBy, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime parsedStartDate = LocalDateTime.parse(startDate, formatter);
        LocalDateTime parsedEndDate = LocalDateTime.parse(endDate, formatter);
        List<String> traineeIdList = traineeService.getTraineeIdByBatchId(batchId);
        try {
            batchService.assignTestToBatch(labTestId, batchId, parsedStartDate, parsedEndDate, duration, percentage, negativeMarkingFactor, assignedBy);
            labTestService.addDefaultLabTestInfo(labTestId, batchId, traineeIdList);
            redirectAttributes.addFlashAttribute("successMessage", "Test assigned successfully to respected batch.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            System.out.println("error occurred");
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/lab/assignTestLab2?id=" + labTestId;
    }

    @GetMapping("lab/assignTestLab1")
    public String openAssignTest1(@RequestParam("id") int id, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        LabTestDto labTestDto = labTestService.getLabTestById(id);
        ArrayList<LabTestDto> labTestDtos = new ArrayList<>();
        labTestDtos.add(labTestDto);
        List<LabTestInfo> labInfoList = labTestService.getLabTestinfo(id);
        List<BatchIdCode> allBatches = batchService.getAllBatchIdCodeActive();
        List<BatchIdCode> assignedBatches = new ArrayList<>();
        List<BatchIdCode> unassignedBatches = new ArrayList<>();
        for (BatchIdCode batchDto : allBatches) {
            boolean assigned = labInfoList.stream().anyMatch(labInfo -> labInfo.getBatch() == batchDto.getId());
            if (assigned) {
                assignedBatches.add(batchDto);
            } else {
                unassignedBatches.add(batchDto);
            }
        }
        model.addAttribute("tests", labTestDtos);
        model.addAttribute("labTestName",labTestDto.getLabTestName());
        model.addAttribute("batches", unassignedBatches);
        model.addAttribute("assignedBy", getUserName(getUserId()));
        return "pages/admin/assignTest";
    }


    @GetMapping("lab/assignTestLab2")
    public String openAssignTest2(@RequestParam("id") int id, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabTestInfo> labInfoList = labTestService.getLabTestinfo(id);
        List<BatchIdCode> allBatches = batchService.getAllBatchIdCode();
        List<BatchIdCode> assignedBatches = new ArrayList<>();
        List<BatchIdCode> unassignedBatches = new ArrayList<>();
        for (BatchIdCode batchDto : allBatches) {
            boolean assigned = labInfoList.stream().anyMatch(labInfo -> labInfo.getBatch() == batchDto.getId());
            if (assigned) {
                assignedBatches.add(batchDto);
            } else {
                unassignedBatches.add(batchDto);
            }
        }
        LabTestDto labTestDto = labTestService.getLabTestById(id);
        String status = null;
        if (labTestDto != null && labTestDto.getEndDate() != null) {
            LocalDateTime currentTime = LocalDateTime.now();
            if (currentTime.isAfter(labTestDto.getEndDate())) {
                status = "Closed";
            } else if (currentTime.isEqual(labTestDto.getStartDate()) ||
                    (currentTime.isAfter(labTestDto.getStartDate()) && currentTime.isBefore(labTestDto.getEndDate()))) {
                status = "Inprogress";
            } else if (currentTime.isBefore(labTestDto.getStartDate())) {
                status = "Not Initiated";
            }
        }
        model.addAttribute("labTestId", id);
        System.out.println("Assessment name ----> "+labTestDto.getLabTestName());

        model.addAttribute("labTestName", labTestDto.getLabTestName());
        model.addAttribute("assignedBatches", assignedBatches);
        model.addAttribute("unassignedBatches", unassignedBatches);
        model.addAttribute("status", status);
        return "pages/admin/assignTest1";
    }

    @GetMapping("labTest/viewLabTestReportByBatch")
    public String viewLabTestReportByBatch(@RequestParam("id") int labTestId, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabTestInfo> labInfoList = labTestService.getLabTestinfo(labTestId);
        List<BatchIdCode> allBatches = batchService.getAllBatchIdCode();
        List<BatchIdCode> assignedBatches = new ArrayList<>();
        List<BatchIdCode> unassignedBatches = new ArrayList<>();
        // Check if each lab is assigned to a batch
        for (BatchIdCode batchDto : allBatches) {
            boolean assigned = labInfoList.stream().anyMatch(labInfo -> labInfo.getBatch() == batchDto.getId());
            if (assigned) {
                assignedBatches.add(batchDto);
            } else {
                unassignedBatches.add(batchDto);
            }
        }

        model.addAttribute("labTestId", labTestId);
        model.addAttribute("labTestName", labTestService.getLabTestById(labTestId).getLabTestName());
        model.addAttribute("assignedBatches", assignedBatches);
        model.addAttribute("unassignedBatches", unassignedBatches);
        return "pages/admin/viewLabTestReportByBatch";
    }

    @GetMapping("lab/editAssignLabTest")
    public String editAssignLabTest(@RequestParam("labTestId") int labTestId, @RequestParam("batchId") int batchId, @RequestParam("labTestName") String labTestName, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            BatchIdCode batchIdCodes = batchService.getBatchIdCodeById(batchId);
            LabTestDto labTestById = labTestService.getLabTestByIdAndBatchId(labTestId, batchId, labTestName);
            model.addAttribute("batchId", batchId);
            model.addAttribute("labData", labTestById);
            model.addAttribute("labTestId", labTestId);
            model.addAttribute("labTestName", labTestName);
            model.addAttribute("batchCode", batchIdCodes.getBatchCode());
            redirectAttributes.addFlashAttribute("successMessage", "Edit successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "pages/admin/editAssignLabTest";
    }

    @PostMapping("lab/editAssignTest1")
    public String editLabTestAssign(@RequestParam("labTestId") int labTestId, @RequestParam("batchId") int batchId, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("duration") int duration, @RequestParam("passPercentage") double percentage, @RequestParam("negativeMarkingFactor") double negativeMarkingFactor, @RequestParam("assignedBy") String assignedBy, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime parsedStartDate = LocalDateTime.parse(startDate, formatter);
        LocalDateTime parsedEndDate = LocalDateTime.parse(endDate, formatter);
        try {
            batchService.editassignTestToBatch(labTestId, batchId, parsedStartDate, parsedEndDate, duration, percentage, negativeMarkingFactor, assignedBy);
            redirectAttributes.addFlashAttribute("successMessage", "Test Assign updated successfully.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/viewAssignLabTest";
    }


}
