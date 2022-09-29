package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Component
public class DefaultUser {
    private RoleService roleService;
    private UserService userService;

    @Autowired
    public DefaultUser(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostConstruct
    private void initialize() {
        if (roleService.findAll().stream().noneMatch(n -> n.getName().equals("ADMIN"))) {
            roleService.save(new Role("ADMIN"));
        }
        if (roleService.findAll().stream().noneMatch(n -> n.getName().equals("USER"))) {
            roleService.save(new Role("USER"));
        }
        if (userService.findAll().stream().noneMatch(n -> n.getEmail().equals("admin@mail.ru"))) {
            User user = new User();
            user.setFirstname("admin");
            user.setLastname("admin_last");
            user.setAge(1);
            user.setEmail("admin@mail.ru");
            user.setPassword("admin");
            user.setRoles(roleService.findAll());
            userService.save(user);
        }
        if (userService.findAll().stream().noneMatch(n -> n.getEmail().equals("user@mail.ru"))) {
            User user = new User();
            user.setFirstname("user");
            user.setLastname("user_last");
            user.setAge(1);
            user.setEmail("user@mail.ru");
            user.setPassword("user");
            user.setRoles(Set.of(roleService.getByName("USER")));
            userService.save(user);
        }
    }

}
