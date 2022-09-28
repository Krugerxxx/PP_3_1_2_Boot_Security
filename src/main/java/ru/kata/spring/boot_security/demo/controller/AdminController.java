package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
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
    public String welcome(Model model) {
        model.addAttribute("userslist", userService.findAll().stream()
                .sorted((n, m) -> (int) (n.getId() - m.getId()))
                .collect(Collectors.toList()));
        return "users/users";
    }

    @RequestMapping(value = "/new_user", method = RequestMethod.GET)
    public ModelAndView newUserForm(ModelAndView model, User user) {
        model.addObject("user", user);
        model.addObject("roleName", roleService.findAll());
        model.setViewName("users/new_user");
        return model;
    }

    @RequestMapping(value = "/new_user", method = RequestMethod.POST)
    public ModelAndView newUserSubmit(@ModelAttribute(value = "user") @Valid User user,
                                      BindingResult result,
                                      ModelAndView model) {
        model.addObject("roleName", roleService.findAll());
        if (result.hasErrors()) {
            model.setViewName("users/new_user");
            return model;
        }
        if (userService.save(user).getEmail() == "") {
            model.addObject("emailExists", "Такой email существует");
            model.setViewName("/users/new_user");
            return model;
        }
        model.setViewName("redirect:/admin");
        return model;
    }

    @RequestMapping(value = "/edit_user{id}", method = RequestMethod.GET)
    public ModelAndView editUserForm(@PathVariable(value = "id") Long id, ModelAndView model) {
        model.addObject("user", userService.findById(id));
        model.addObject("roleName", roleService.findAll());
        model.setViewName("users/edit_user");
        return model;
    }

    @RequestMapping(value = "/edit_user{id}", method = RequestMethod.POST)
    public ModelAndView editUser(@ModelAttribute(value = "user") @Valid User user,
                                 BindingResult result,
                                 ModelAndView model) {
        model.addObject("roleName", roleService.findAll());
        if (result.hasErrors()) {
            model.setViewName("users/edit_user");
            return model;
        }
        if (userService.save(user).getEmail() == "") {
            model.addObject("emailExists", "Такой email существует");
            user.setEmail(userService.findById(user.getId()).getEmail());
            System.out.println(user.getEmail());
            model.addObject("user", user);
            model.setViewName("/users/edit_user");
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

}
