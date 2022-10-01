package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public HomeController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    //TODO найти как перенаправить в конфиге security
    @GetMapping(value = "/")
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return "redirect:/login";
        } else if (authentication.getAuthorities().stream().anyMatch(n -> n.getAuthority().equals("ADMIN"))) {
            return "redirect:/admin";
        } else {
            return "redirect:/user";
        }
    }


    @GetMapping("/user")
    public String welcome(@AuthenticationPrincipal UserDetails userDetails, Model model, User user) {
        model.addAttribute("roleName", roleService.findAll());
        model.addAttribute("activeUser", userService.findByUsername(userDetails.getUsername()));
        model.addAttribute("userslist", userService.findAll());
        return "users/users";
    }


/*    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            request.getSession().invalidate();
        }
        return "redirect:/login";
    }*/

}