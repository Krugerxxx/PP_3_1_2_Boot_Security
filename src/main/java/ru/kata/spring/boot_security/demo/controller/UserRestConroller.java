package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exeption_handling.IncorrectDataException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserRestConroller {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserRestConroller(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Redirect-Ref", "/admin");
        return new ResponseEntity<>(userService.findById(id), httpHeaders, HttpStatus.OK);
    }

    @PostMapping()
    public User addUser(@RequestBody @Valid User user,
                        BindingResult result) {
        if (result.hasErrors()) {
            throw new IncorrectDataException();
        }
        return userService.save(((UserServiceImpl) userService).reSetRoles(user));
    }

    @PatchMapping(value = "/{id}")
    public User editUser(@RequestBody @Valid User user,
                         BindingResult result) {
        if (result.hasErrors()) {
            throw new IncorrectDataException();
        }

        return userService.save(((UserServiceImpl) userService).reSetRoles(user));
    }

    @DeleteMapping(value = "/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return null;
    }

    @GetMapping("/roles")
    public Set<Role> getAllRoles() {
        return roleService.findAll();
    }

}
