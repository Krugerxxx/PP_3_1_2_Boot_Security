package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {


    //TODO найти как перенаправить в конфиге security
    @GetMapping(value = "/")
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()){
            return "redirect:/login";
        } else if (authentication.getAuthorities().stream().anyMatch(n -> n.getAuthority().equals("ADMIN"))) {
            return "redirect:/admin";
        } else {
            return "redirect:/user";
        }
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