package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleService {
    public List<Role> listAll();

    Role save(Role role);

    public List<Role> getByName(String name);


}
