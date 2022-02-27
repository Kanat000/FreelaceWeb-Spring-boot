package com.example.crud_project.registration;

import com.example.crud_project.user.User;
import com.example.crud_project.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;

@Controller
@RequestMapping(path = "/api/v1/reg")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping
    public String registerPage(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "Register";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String register(User user, Model model){
        user.setUserRole(UserRole.USER);
       String token = registrationService.register(user);
       registrationService.confirmToken(token);
       return "redirect:/login";
    }

}
