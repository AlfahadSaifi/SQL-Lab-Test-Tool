package lab.controller.trainee;

import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.trainee.TraineeDto;
import lab.entity.lab.LabInfo;
import lab.entity.lab.LabStatus;
import lab.entity.lab.LabTestInfo;
import lab.payload.batch.BatchIdCode;
import lab.payload.lab.LabPayload;
import lab.service.batches.BatchService;
import lab.service.lab.LabService;
import lab.service.labtest.LabTestService;
import lab.service.trainee.TraineeService;
import lab.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/trainee")
@SessionAttributes("username")
public class Trainee {
    @Autowired
    private LabService labService;
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

    public String getUser(String userId) {
        String username = userService.getUserNameById(userId);
        return username;
    }

    @RequestMapping("/home")
    public String home(Model model) {
        List<LabPayload> labDtoList = labService.getTraineeLab(getUserId());
        List<LabTestDto> labTestDtoList = labTestService.getTestsByTraineeUsername(getUserId());
        model.addAttribute("labDtoList", labDtoList);
        model.addAttribute("labTestDtoList", labTestDtoList);
        model.addAttribute("username", getUser(getUserId()));
        return "pages/traineeTest/home";
    }

    @RequestMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("username", getUser(getUserId()));
        return "pages/trainee/dashboard";
    }

    @RequestMapping("/labs")
    public String getLabs(Model model) {
        model.addAttribute("username", getUser(getUserId()));
        return "pages/trainee/labs";
    }

    @RequestMapping("/viewLabs")
    public String viewLab(Model model) {
        model.addAttribute("userName", getUser(getUserId()));
        List<LabPayload> labDtoList = labService.getCompleteLab(getUserId());
        model.addAttribute("labList", labDtoList);
        return "pages/trainee/viewLab";
    }

    @RequestMapping("/labTests")
    public String labTests(Model model) {
        model.addAttribute("username", getUser(getUserId()));
        return "pages/trainee/labTests";
    }

    @RequestMapping("/viewLabTest")
    public String viewLabTest(Model model) {
        model.addAttribute("userName", getUser(getUserId()));
        List<LabTestDto> labTestDtoList = labTestService.getCompleteLabTest(getUserId());
        model.addAttribute("labTestList", labTestDtoList);
        return "pages/trainee/viewLabTest";
    }

    @RequestMapping("/labStart")
    public String labStart(@RequestParam("labId") int labId, Model model) {
        TraineeDto trainee = traineeService.getTraineeByUserName(getUserId());
//        LabDto labDto = labService.getLabById(labId);
        LabInfo labInfo = labService.getLabInfo(labId, getUserId(), trainee.getBatchId());
        if (labInfo == null) {
            labInfo = new LabInfo();
        }
        labInfo.setBatch(trainee.getBatchId());
        labInfo.setTraineeId(trainee.getEmployeeId());
        labInfo.setLabStatus(LabStatus.RESUME);
        labInfo.setLabId(labId);
        labService.addLabInfo(labInfo);
        model.addAttribute("labId", labId);
        return "pages/traineeTest/testPortal";
    }

    @RequestMapping("/labTestStart")
    public String labTestStart(@RequestParam("labTestId") int labTestId, Model model) {
        LabTestDto labTestDto = labTestService.getLabTestById(labTestId);
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (labTestDto != null && labTestDto.getEndDate().isBefore(currentDateTime) ) {
            return "pages/trainee/dashboard";
        }
        TraineeDto traineeByUserName = traineeService.getTraineeByUserName(getUserId());
        String traineeId = traineeByUserName.getEmployeeId();
        LabTestInfo labTestInfo = labTestService.getLabTestInfo(labTestId, traineeId, traineeByUserName.getBatchId());
        if (labTestInfo == null) {
            labTestInfo = new LabTestInfo();
        }
        if(labTestInfo.getLabTestStatus()==LabStatus.COMPLETED){
            return "pages/trainee/dashboard";
        }
        labTestInfo.setBatch(traineeByUserName.getBatchId());
        labTestInfo.setTraineeId(traineeByUserName.getEmployeeId());
        labTestInfo.setLabTestStatus(LabStatus.RESUME);
        labTestInfo.setLabTestId(labTestId);
        labTestService.addLabTestInfo(labTestInfo);
        model.addAttribute("labTestDto", labTestDto);
        return "pages/traineeTest/labTestPortal";
    }
    @RequestMapping("/testReport")
    public String testReport(@RequestParam("reportId") int reportId, Model model) {

        return "pages/traineeTest/testReport";
    }

    @RequestMapping("/instruction")
    public String instruction(@RequestParam("labId") int labId, Model model) {
        model.addAttribute("labId", labId);
        return "pages/trainee/instruction";
    }

    @RequestMapping("/instructionLabTest")
    public String instructionLabTest(@RequestParam("labTestId") int labTestId, Model model) {
        model.addAttribute("labTestId", labTestId);
//        LabTestDto labTestDto = labTestService.getLabTestById(labTestId);
        LabTestDto labTestDto = labTestService.getLabTestSummaryById(labTestId);
        model.addAttribute("labTest", labTestDto);
        return "pages/trainee/instructionLabTest";
    }
}
