package lab.controller.admin;

import lab.dto.admin.BatchDto;
import lab.exceptionHandler.CustomDatabaseException;
import lab.payload.batch.BatchInfo;
import lab.payload.batch.BatchReportPayload;
import lab.payload.lab.LabPayload;
import lab.payload.labTest.LabTestPayload;
import lab.service.batches.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BatchController {
    @Autowired
    private BatchService batchService;

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping("batch")
    public String createBatch(Model model) {
        model.addAttribute("batch", new BatchDto());
        return "pages/lab/createBatch";
    }

    @PostMapping(value = "batch")
    public String createBatch1(@ModelAttribute("batch") @Valid BatchDto batchDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pages/lab/createBatch";
        }
        try {
            BatchDto batchDto1 = batchService.createBatch(batchDto, getUserName());
            String message = "Batch added successfully. ";
            if (batchDto1.getEnrollmentDate().after(new Date())) {
                message += "The enrollment date is in the future. So, You can't assign test right now";
            }
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/viewBatches";
    }

    @RequestMapping("viewBatch")
    @ResponseBody
    public List<BatchDto> getBatches() {
        return batchService.getAllBatch();
    }

    @ExceptionHandler({CustomDatabaseException.class, DataIntegrityViolationException.class})
    @ResponseBody
    public String showError() {
        return "Something Went wrong";
    }

    @RequestMapping("batchReport")
    public String viewBatchReport(Model model) {
        List<BatchInfo> batchReports = batchService.getBatchInfo();
        model.addAttribute("batchReports", batchReports);
        return "pages/batch/viewBatchReport";
    }


    @RequestMapping("viewDetailedBatchReports")
    public String viewDetailedBatchReport(@RequestParam("id") int batchId, Model model) {
        List<LabPayload> labList = batchService.getLabUsingBatchId(batchId);
        List<LabTestPayload> labTestList = batchService.getLabTestUsingBatchId(batchId);
        BatchInfo batchInfo = batchService.getBatchInfoById(batchId);
        BatchReportPayload batchReports = new BatchReportPayload();
        batchReports.setBatchCode(batchInfo.getBatchCode());
        batchReports.setTotalLabs(batchInfo.getTotalLabs());
        batchReports.setTotalLabTests(batchInfo.getTotalLabTests());
        batchReports.setTotalTrainee(batchInfo.getTotalTrainees());
        batchReports.setLabPayloadList(labList);
        batchReports.setLabTestPayloadList(labTestList);
        model.addAttribute("batchInfo", batchReports);
        model.addAttribute("batchId", batchId);
        return "pages/batch/DetailedBatchReport";
    }
}