package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public String welcome(Model model) {
        model.addAttribute("userslist", userService.listAll());
        return "users/users";
    }

    @RequestMapping(value = "/new_user", method = RequestMethod.GET)
    public String newUserForm(Model model, User user) {
        model.addAttribute("newuser", user);
        return "users/new_user";
    }

    @RequestMapping(value = "/new_user", method = RequestMethod.POST)
    public String newUserSubmit(@ModelAttribute @Validated User user, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/users";
        }
        userService.save(user);
        return "redirect:/users";
    }

    @RequestMapping(value = "/edit_user{id}", method = RequestMethod.GET)
    public String editUserForm(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("edituser", userService.getUser(id));
        return "users/edit_user";
    }

    @RequestMapping(value = "/edit_user{id}", method = RequestMethod.POST)
    public String editUser(@ModelAttribute @Validated User user, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/users";
        }
        userService.update(user);
        return "redirect:/users";
    }

    @RequestMapping(value = "/delete_user{id}")
    public String deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

}
