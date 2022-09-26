package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    void save(User user);

    void update(User user);

    List<User> listAll();

    User getUser(long id);

    void deleteUser(long id);

    User findByUsername(String name);

}
