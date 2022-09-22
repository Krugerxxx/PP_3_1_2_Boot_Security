package ru.kata.spring.boot_security.demo.dao;


import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserDao {
    User save(User user);

    void update(User user);

    List<User> listAll();

    User getUser(long id);

    void deleteUser(long id);
}
