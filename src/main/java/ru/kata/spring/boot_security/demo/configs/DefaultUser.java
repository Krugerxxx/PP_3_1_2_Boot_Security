package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.List;

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
        if (roleService.listAll().stream().noneMatch(n -> n.getName().equals("ADMIN"))) {
            roleService.save(new Role("ADMIN"));
        }
        if (roleService.listAll().stream().noneMatch(n -> n.getName().equals("USER"))) {
            roleService.save(new Role("USER"));
        }
        if (userService.listAll().stream().noneMatch(n -> n.getName().equals("user"))) {
            User user = new User();
            user.setName("user");
            user.setAge(18);
            user.setEmail("sss@mail.ru");
            user.setPassword("user");
            user.setRoles(roleService.getByName("ADMIN"));
            user.setRoles(roleService.getByName("USER"));
            userService.save(user);
        }
    }

}
