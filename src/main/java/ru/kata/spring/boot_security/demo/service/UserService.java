package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Set;

public interface UserService extends UserDetailsService {

    User save(User user);

    Set<User> findAll();

    User findById(Long id);

    User findByUsername(String name);

    void deleteById(Long id);

}
