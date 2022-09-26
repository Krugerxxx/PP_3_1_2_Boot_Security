package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;


import javax.validation.Valid;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping()
    public String welcome(Model model) {
        model.addAttribute("userslist", userService.listAll());
        return "users/users";
    }

    @RequestMapping(value = "/new_user", method = RequestMethod.GET)
    public ModelAndView newUserForm(ModelAndView model, User user) {
        model.addObject("user", user);
        model.addObject("roleName", roleService.listAll());
        model.setViewName("users/new_user");
        return model;
    }

    @RequestMapping(value = "/new_user", method = RequestMethod.POST)
    public ModelAndView newUserSubmit(@ModelAttribute(value = "user") @Valid User user,
                                      BindingResult result,
                                      ModelAndView model) {
        model.addObject("roleName", roleService.listAll());
        if (result.hasErrors()) {
            model.setViewName("/users/new_user");
            return model;
        }
        try {
            userService.save(user);
        } catch (Exception e) {
            model.addObject("emailExists", "Такой email существует");
            model.setViewName("/users/new_user");
            return model;
        }
        model.setViewName("redirect:/users");
        return model;
    }

    @RequestMapping(value = "/edit_user{id}", method = RequestMethod.GET)
    public ModelAndView editUserForm(@PathVariable(value = "id") Long id, ModelAndView model) {
        model.addObject("user", userService.getUser(id));
        model.addObject("roleName", roleService.listAll());
        model.setViewName("users/edit_user");
        return model;
    }

    @RequestMapping(value = "/edit_user{id}", method = RequestMethod.POST)
    public ModelAndView editUser(@ModelAttribute(value = "user") @Valid User user,
                                 BindingResult result,
                                 ModelAndView model) {
        model.addObject("roleName", roleService.listAll());
        if (result.hasErrors()) {
            model.setViewName("users/edit_user");
            return model;
        }
        try {
            userService.update(user);
        } catch (Exception e) {
            model.addObject("emailExists", "Такой email существует");
            model.addObject("user", user);
            model.setViewName("/users/new_user");
            return model;
        }
        model.setViewName("redirect:/users");
        return model;
    }

    @RequestMapping(value = "/delete_user{id}")
    public String deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

}
