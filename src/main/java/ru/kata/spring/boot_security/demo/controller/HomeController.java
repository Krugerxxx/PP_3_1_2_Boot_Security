package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;


@Controller
public class HomeController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public HomeController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

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
    public String welcomeUser() {
        return "index";
    }

}