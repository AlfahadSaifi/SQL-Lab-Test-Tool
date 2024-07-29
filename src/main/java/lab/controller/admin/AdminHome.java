package lab.controller.admin;
import lab.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/admin")
@SessionAttributes("username")
public class AdminHome {
    @Autowired
    UserService userService;
    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    @RequestMapping("/")
    public String home(Model model){
        String username = userService.getUserNameById(getUserName());
        model.addAttribute("username", username);
        return "pages/admin/admin";
    }
    @RequestMapping("/dashboard")
    public String adminDashboard(Model model){
        String username = userService.getUserNameById(getUserName());
        model.addAttribute("username", username);
        return "pages/admin/adminDashboard";
    }
    @RequestMapping("/viewAssignLab")
    public String viewAssignLab(Model model){
        String username = userService.getUserNameById(getUserName());
        model.addAttribute("username", username);
        return "pages/admin/viewAssignLab";
    }
    @RequestMapping("/viewAssignLabTest")
    public String viewAssignLabTest(Model model){
        String username = userService.getUserNameById(getUserName());
        model.addAttribute("username", username);
        return "pages/admin/viewAssignLabTest";
    }
}
