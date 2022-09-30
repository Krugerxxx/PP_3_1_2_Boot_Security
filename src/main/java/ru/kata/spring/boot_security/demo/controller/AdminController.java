package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String welcome(@AuthenticationPrincipal UserDetails userDetails, Model model, User user) {
        model.addAttribute("roleName", roleService.findAll());
        model.addAttribute("activeUser", userService.findByUsername(userDetails.getUsername()));
        model.addAttribute("userslist", userService.findAll());
        return "users/users";
    }

    @RequestMapping(value = "/new_user", method = RequestMethod.GET)
    public ModelAndView newUserForm(@AuthenticationPrincipal UserDetails userDetails, ModelAndView model, User user) {
        model.addObject("activeUser", userService.findByUsername(userDetails.getUsername()));
        model.addObject("user", user);
        model.addObject("roleName", roleService.findAll());
        model.setViewName("users/new_user");
        return model;
    }

    @RequestMapping(value = "/new_user", method = RequestMethod.POST)
    public ModelAndView newUserSubmit(@ModelAttribute(value = "user") @Valid User user,
                                      BindingResult result,
                                      ModelAndView model,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        model.addObject("activeUser", userService.findByUsername(userDetails.getUsername()));
        model.addObject("roleName", roleService.findAll());
        if (result.hasErrors()) {
            System.out.println("Будем считать, что здесь логи, frontend не справился с проверкой");
            model.setViewName("users/users");
            return model;
        }

        String enterEmail = user.getEmail();
        if (userService.save(user).getEmail() == "") {
            model.addObject("emailExists", "Такой email существует: " + enterEmail);
            model.setViewName("/users/new_user");
            return model;
        }
        model.setViewName("redirect:/admin");
        return model;
    }

    @PostMapping(value = "/edit_user{id}")
    public ModelAndView editUser(@ModelAttribute(value = "user") @Valid User user,
                                 BindingResult result,
                                 ModelAndView model) {
        model.addObject("roleName", roleService.findAll());
        if (result.hasErrors()) {
            System.out.println("Будем считать, что здесь логи, frontend не справился с проверкой");
            model.setViewName("users/users");
            return model;
        }

        //TODO подумать, что с этим можно сделать в последней версии, пока переадресация на страницу пользователя
        String enterEmail = user.getEmail();
        if (userService.save(user).getEmail() == "") {
            model.addObject("emailExists", "Такой email существует: " + enterEmail);
            user.setEmail(userService.findById(user.getId()).getEmail());
            model.addObject("user", user);
            model.setViewName("redirect:/admin");
            return model;
        }
        model.setViewName("redirect:/admin");
        return model;
    }

    @RequestMapping(value = "/delete_user{id}")
    public String deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }


    /*TODO посмотри потребуется ли он в последней версии
    @RequestMapping(value = "/edit_user{id}", method = RequestMethod.GET)
    public ModelAndView editUserForm(@PathVariable(value = "id") Long id, ModelAndView model) {
        model.addObject("user", userService.findById(id));
        model.addObject("roleName", roleService.findAll());
        model.setViewName("users/edit_user");
        return model;
    }*/

}
