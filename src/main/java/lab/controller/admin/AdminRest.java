package lab.controller.admin;
import lab.payload.admin.AdminBatchDetails;
import lab.payload.admin.AdminDashBoardPayLoad;
import lab.payload.admin.AdminLabDetails;
import lab.payload.admin.AdminLabTestDetails;
import lab.payload.batch.BatchStatusPayload;

import lab.payload.lab.LabStatusPayload;
import lab.payload.labTest.LabTestStatusPayload;
import lab.service.admin.AdminService;
import lab.service.batches.BatchService;
import lab.service.lab.LabService;
import lab.service.labtest.LabTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
@RestController
@RequestMapping(path = "/api/admin")
@SessionAttributes("username")
public class AdminRest {
    @Autowired
    private AdminService adminService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private LabService labService;
    @Autowired
    private LabTestService labTestService;
    private String getUserName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    @GetMapping("/adminDashboardDetail")
    public ResponseEntity<?> adminDashboardDetail() {
        AdminDashBoardPayLoad adminDashBoardPayLoad = new AdminDashBoardPayLoad();
        List<Long> adminBatchDetails = batchService.getAdminBatchDetails(getUserName());
        AdminBatchDetails batchDetails=new AdminBatchDetails();
        batchDetails.setNoOfActiveBatch(adminBatchDetails.get(0));
        batchDetails.setNoOfClosedBatch(adminBatchDetails.get(1));
        adminDashBoardPayLoad.setAdminBatchDetails(batchDetails);
        List<Long> adminLabDetails = batchService.getAdminLabDetails(getUserName());
        AdminLabDetails labDetails = new AdminLabDetails();
        labDetails.setNoOfAssignedLab(adminLabDetails.get(1));
        labDetails.setNoOfUnAssignedLab(adminLabDetails.get(0));
        adminDashBoardPayLoad.setAdminLabDetails(labDetails);
        List<Long> adminLabTestDetails = batchService.getAdminLabTestDetails(getUserName());
        AdminLabTestDetails labTestDetails = new AdminLabTestDetails();
        labTestDetails.setNoOfAssignedLabTest(adminLabTestDetails.get(1));
        labTestDetails.setNoOfUnAssignedLabTest(adminLabTestDetails.get(0));
        adminDashBoardPayLoad.setAdminLabTestDetails(labTestDetails);
        return ResponseEntity.ok().body(adminDashBoardPayLoad);
    }
    @GetMapping("/activeBatches")
    public ResponseEntity<?> activeBatches() {
        List<BatchStatusPayload> activeBatchs = batchService.getActiveAllBatchs();
        return ResponseEntity.ok().body(activeBatchs);
    }
    @GetMapping("/closedBatches")
    public ResponseEntity<?> closedBatches() {
        List<BatchStatusPayload> closedBatchs = batchService.getClosedBatchs(getUserName());
        return ResponseEntity.ok().body(closedBatchs);
    }
    @GetMapping("/assignedLabs")
    public ResponseEntity<?> assignedLabs() {
        List<LabStatusPayload> assignedLab = batchService.getAssignedLab(getUserName());
        return ResponseEntity.ok().body(assignedLab);
    }
    @GetMapping("/unAssignedLabs")
    public ResponseEntity<?> unAssignedLabs() {
        List<LabStatusPayload> unAssignedLab = batchService.getUnAssignedLab(getUserName());
        return ResponseEntity.ok().body(unAssignedLab);
    }
    @GetMapping("/assignedLabTests")
    public ResponseEntity<?> assignedLabTests() {
        List<LabTestStatusPayload> assignedLabTest = batchService.getAssignedLabTest(getUserName());
        return ResponseEntity.ok().body(assignedLabTest);
    }
    @GetMapping("/unAssignedLabTests")
    public ResponseEntity<?> unAssignedLabTests() {
        List<LabTestStatusPayload> unAssignedLabTest = batchService.getUnAssignedLabTest(getUserName());
        return ResponseEntity.ok().body(unAssignedLabTest);
    }
    @GetMapping("/getQuestionPoint")
    public ResponseEntity<?> getQuestionPoint(@RequestParam("labId") int labId) {
        int questionPoint = labService.getQuestionPoint(labId);
        return ResponseEntity.ok().body(questionPoint);
    }
    @GetMapping("/getLabTestQuestionPoint")
    public ResponseEntity<?> getLabTestQuestionPoint(@RequestParam("labTestId") int labTestId) {
        int questionPoint = labTestService.getQuestionPoint(labTestId);
        return ResponseEntity.ok().body(questionPoint);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam("password") String newPassword, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, HttpSession session) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return ResponseEntity.badRequest().body("Bad Request");
        }
        try {
            adminService.changePassword(newPassword,getUserName());
            session.invalidate();
            return ResponseEntity.ok("Password changed! You need to login again");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
