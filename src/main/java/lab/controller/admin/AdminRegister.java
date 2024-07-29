package lab.controller.admin;

import lab.dto.admin.AdminDto;
import lab.entity.user.Role;
import lab.exceptions.user.UserAlreadyExist;
import lab.service.admin.AdminWithTxn;
import lab.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/nSbt")
@SessionAttributes("username")
public class AdminRegister {
    @Autowired
    private AdminWithTxn adminWithTxn;
    @Autowired
    private UserService userService;

    private String getUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    private String getUserName(String userId){
        return userService.getUserNameById(userId);
    }
    @RequestMapping("/")
    public String registerView(Model model) {
        AdminDto adminDto = new AdminDto();
        model.addAttribute("admin", adminDto);
        model.addAttribute("username", getUserId());
        return "pages/admin/adminRegister";
    }

    @PostMapping("/")
    public String registerView(@ModelAttribute("admin") @Valid AdminDto admin, BindingResult bindingResult, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        if (referer == null) {
            return "pages/badRequest";
        }
        if (bindingResult.hasErrors()) {
            return "pages/admin/adminRegister";
        }
        admin.setRole(Role.ROLE_ADMIN);
        try {
            adminWithTxn.registerAdmin(admin);
            redirectAttributes.addFlashAttribute("successMessage", "Admin Registered Successfully.");
        } catch (UserAlreadyExist e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid data/User already exist");
        }
        return "redirect:/nSbt/";
    }
}
