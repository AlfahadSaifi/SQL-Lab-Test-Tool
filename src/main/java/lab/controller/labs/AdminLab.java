package lab.controller.labs;

import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.payload.batch.BatchIdCode;
import lab.payload.lab.LabInfoIdBatch;
import lab.payload.lab.LabPayload;
import lab.service.admin.AdminWithTxn;
import lab.service.batches.BatchService;
import lab.service.formatter.Formatter;
import lab.service.lab.LabService;
import lab.service.labtest.LabTestService;
import lab.service.questionBank.QuestionBankService;
import lab.service.trainee.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminLab {
    @Autowired
    private AdminWithTxn adminWithTxn;
    @Autowired
    private LabService labService;
    @Autowired
    private LabTestService labTestService;

    @Autowired
    private TraineeService traineeService;
    @Autowired
    private BatchService batchService;

    @Autowired
    private QuestionBankService questionBankService;


    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping("lab")
    public String createLab(Model model) {
        model.addAttribute("lab", new LabDto());
        return "pages/lab/createLab";
    }

    @PostMapping("lab")
    public String createLab1(@ModelAttribute("lab") @Valid LabDto labDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        if (bindingResult.hasErrors()) {
            return "pages/lab/createLab";
        }
        try {
            labDto = labService.addLabToAdmin(labDto, getUserName());
            redirectAttributes.addFlashAttribute("successMessage", "Lab added Successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        if (labDto == null && labDto.getLabId() == 0) {
            redirectAttributes.addFlashAttribute("successMessage", "Lab added Successfully.");
            return "redirect:/admin/viewAssignLab";
        }
        return "redirect:/admin/viewQuestions?id=" + labDto.getLabId();
    }

    @RequestMapping("viewLabs1")
    public String getLabs(Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            return "redirect:/admin/";
        }
        List<LabDto> list = labService.getLabs(getUserName());
        model.addAttribute("labList", list);
        return "pages/lab/viewLab";
    }

    @RequestMapping("viewLabs")
    public String getLabs(@RequestParam("batchId") int id, Model model, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabDto> list = batchService.getAssignBatchById(id);
        model.addAttribute("labList", list);
        return "pages/lab/viewLab";
    }

    @RequestMapping("/addQuestion")
    public String addQuestion(Model model, @RequestParam("id") int id, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        QuestionDto questionDto = new QuestionDto();
        questionDto.setQuestionPoints(labService.getQuestionPoint(id));
        model.addAttribute("question", questionDto);
        model.addAttribute("labId", id);
        return "pages/question/addQuestion";
    }

    @GetMapping("deleteLab")
    public String deleteLab(@RequestParam("id") int labId, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            boolean status = labService.deleteLab(labId, getUserName());
            redirectAttributes.addFlashAttribute("successMessage", "Lab deleted successfully !!");
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "lab delete fail !!");
        }
        return "redirect:/admin/viewAssignLab";
    }

    @PostMapping("/addQuestionViaForm")
    public String addQuestion(@ModelAttribute("question") QuestionDto questionDto, @RequestParam("id") int labId, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            adminWithTxn.addQuestionInLabTest(questionDto, labId);
            redirectAttributes.addFlashAttribute("successMessage", "Question added successful.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fail to add file.");
        }
        return "redirect:/admin/viewQuestions?id=" + labId;
    }

    @PostMapping("/addQuestionViaFormInLab")
    public String addQuestionViaFormInLab(@ModelAttribute("question") QuestionDto questionDto, @RequestParam("id") int labId, RedirectAttributes redirectAttributes) {
        try {
            adminWithTxn.addQuestion(questionDto, labId);
            redirectAttributes.addFlashAttribute("successMessage", "Question added successful");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Question added fail");
        }
        return "redirect:/admin/viewQuestions?id=" + labId;
    }

    @PostMapping("/addQuestionViaExcel")
    public String addQuestionViaExcel(@RequestParam("file") MultipartFile file, @RequestParam("id") int labId, RedirectAttributes redirectAttributes) {
        try {
            String s = adminWithTxn.addQuestion(file, labId);
            if (s.equals("Successfully")) {
                redirectAttributes.addFlashAttribute("successMessage", "Questions added successful");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Fail to add file." + s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Fail to add file.");
        }
        return "redirect:/admin/viewQuestions?id=" + labId;
    }

    @PostMapping("/addLabTestQuestionViaExcel")
    public String addLabTestQuestionViaExcel(@RequestParam("file") MultipartFile file, @RequestParam("labTestId") int labTestId, RedirectAttributes redirectAttributes) {

        try {
            String s = adminWithTxn.addQuestionInLabTestViaExcel(file, labTestId);
            if (s.equals("Successfully")) {
                redirectAttributes.addFlashAttribute("successMessage", "Questions added successful" + s);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Fail to add file." + s);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fail to add file.");
        }
        return "redirect:/admin/viewLabTestQuestions?id=" + labTestId;
    }

//    @PostMapping("/addReferenceFile")
//    public String addLabReference(@RequestParam("file") MultipartFile file, @RequestParam("id") int labId, RedirectAttributes redirectAttributes) {
//        try {
//            labService.addReferenceFile(file, labId);
//            redirectAttributes.addFlashAttribute("successMessage", "File added successfully.");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Fail to add file.");
//        }
//        return "redirect:/admin/viewQuestions?id=" + labId;
//    }

//    @PostMapping("/addLabTestReferenceFile")
//    public String addLabTestReferenceFile(@RequestParam("file") MultipartFile file, @RequestParam("id") int labTestId, RedirectAttributes redirectAttributes) {
//        try {
//            labTestService.addLabTestReferenceFile(file, labTestId);
//            redirectAttributes.addFlashAttribute("successMessage", "File added successfully.");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Fail to add file.");
//        }
//        return "redirect:/admin/viewLabTestQuestions?id=" + labTestId;
//    }

    @GetMapping("/viewQuestions")
    public String viewQuestions(@RequestParam("id") int labId, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        LabDto labById = labService.getLabById(labId);
        if (labById != null) {
            List<QuestionDto> questionDtoList = labById.getQuestions();
            model.addAttribute("questionList", questionDtoList);
        }
        model.addAttribute("labId", labId);
        return "pages/question/viewQuestions";
    }

    @RequestMapping("/editQuestionViaForm")
    public String editQuestionViaForm(@ModelAttribute("question") QuestionDto questionDto, @RequestParam("id") int labId, @RequestParam("questionId") int questionId, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            questionDto.setQuestionId(questionId);
            adminWithTxn.editQuestion(questionDto, labId, questionId);
            redirectAttributes.addFlashAttribute("successMessage", "Question edit successful");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Question edit fail");
        }
        return "redirect:/admin/viewQuestions?id=" + labId;
    }

    @RequestMapping("/deleteQuestion")
    public String deleteQuestion(@RequestParam("id") int labId, @RequestParam("questionId") int questionId, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            adminWithTxn.deleteQuestion(labId, questionId);
            redirectAttributes.addFlashAttribute("successMessage", "Question delete successful");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fail to add file.");
        }
        return "redirect:/admin/viewQuestions?id=" + labId;
    }

    @RequestMapping("/editQuestion")
    public String editQuestion(@RequestParam("id") int id, @RequestParam("questionId") int questionId, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            QuestionDto questionDto = adminWithTxn.getQuestion(id, questionId);
            model.addAttribute("question", questionDto);
            model.addAttribute("labId", id);
        } catch (Exception ex) {

        }
        return "pages/question/editQuestion";
    }

    @GetMapping("lab/assignLab")
    public String assignLab(Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        model.addAttribute("labs", labService.getLabs(getUserName()));
        model.addAttribute("batches", batchService.getBatchs(getUserName()));
        return "pages/admin/assignLabs";
    }

    @GetMapping("lab/assignLab1")
    public String assignLab2(@RequestParam("id") int id, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabInfoIdBatch> labInfoList = labService.getLabInfoIdBatch(id);
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
        model.addAttribute("labId", id);
        model.addAttribute("labName", labService.getLabById(id).getLabName());
        model.addAttribute("assignedBatches", assignedBatches);
        model.addAttribute("unassignedBatches", unassignedBatches);
        return "pages/admin/assignLabs1";
    }

    @GetMapping("lab/assignLab3")
    public String assignLab1(@RequestParam("id") int labId, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        ArrayList<LabPayload> labDtos = new ArrayList<>();
        LabPayload labPayload = labService.getLabPayloadById(labId);
        labDtos.add(labPayload);
        List<LabInfoIdBatch> labInfoList = labService.getLabInfoIdBatch(labId);
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
        model.addAttribute("labs", labDtos);
        model.addAttribute("batches", unassignedBatches);
        return "pages/admin/assignLabs";
    }

    @PostMapping("lab/assignLab2")
    public String assignLab2(@RequestParam("labId") int labId, @RequestParam("batch") int batchId, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        try {
            List<String> traineeIdList = traineeService.getTraineeIdByBatchId(batchId);
            batchService.assignBatch(labId, batchId);
            labService.addDefaultLabInfo(labId, batchId, traineeIdList);
            redirectAttributes.addFlashAttribute("successMessage", "Assigned Lab successful");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Assigned Lab fail");
        }
        return "redirect:/admin/viewAssignLab";
    }

    @GetMapping("lab/viewReportByBatch")
    public String viewReportByBatch(@RequestParam("id") int labId, Model model, HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        List<LabInfoIdBatch> labInfoList = labService.getLabInfoIdBatch(labId);
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
        String labName = labInfoList.get(0).getLabName();
        model.addAttribute("labId", labId);
        model.addAttribute("labName", labName);
        model.addAttribute("assignedBatches", assignedBatches);
        return "pages/admin/viewReportByBatch";
    }

    @PostMapping("lab/addFromQuestionBank")
    public ModelAndView addingfromQB(@RequestParam("id") int labTestId, RedirectAttributes redirectAttributes){
        ModelAndView mv = new ModelAndView();
        List<QuestionDto> questions = questionBankService.fetch();
        if(questions==null)
        {
           redirectAttributes.addFlashAttribute("errorMessage","Question bank is empty"); // if question bank is empty
           mv.setViewName("redirect:/admin/viewQuestions?id="+ labTestId);
        }
        else {
            mv.addObject("id", labTestId);
            mv.addObject("questions", questions);
            mv.setViewName("pages/question/viewAllQuestions");
        }
        return mv;
    }

    @PostMapping("addSelectedQuestions")
    public String addingSelectedQuestions(@RequestParam("id") int labId, @RequestParam("selectedQuestions") String questionIds, RedirectAttributes redirectAttributes) {
        Set<Integer> questionsIdsInt = Formatter.stringIdsToQuestionsIds(questionIds);

        LabTestDto labTest = labTestService.getLabTestById(labId);
        List<QuestionDto> questionDtoList = labTest.getQuestions();
        for(int id:questionsIdsInt) {
            questionDtoList.add(questionBankService.fetchById(id));
        }
        labTest.setQuestions(questionDtoList);
        labTestService.updateLabTest(labTest);
        return "redirect:/admin/viewLabTestQuestions?id=" + labId;
    }


}