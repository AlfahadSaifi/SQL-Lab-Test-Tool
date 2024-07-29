package lab.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class SecurityController {

    @RequestMapping(value = "/login")
    public String login1(Model model, @RequestParam(value = "error",required = false) String error){
        model.addAttribute("error",error);
        return "/pages/home";
    }
    @RequestMapping(value = "/accessDenied")
    public String accessDenied(Model model, @RequestParam(value = "error",required = false) String error){
        model.addAttribute("error",error);
        return "/pages/accessDenied";
    }

    @RequestMapping(value = "/")
    public String loginHomePage(Model model, @RequestParam(value = "error",required = false) String error){
        model.addAttribute("error",error);
        return "/pages/home";
    }
}
