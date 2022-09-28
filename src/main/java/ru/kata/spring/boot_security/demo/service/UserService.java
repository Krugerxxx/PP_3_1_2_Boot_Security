package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    User save(User user);

    List<User> findAll();

    User findById(Long id);

    User findByUsername(String name);

    void deleteById(Long id);

}
