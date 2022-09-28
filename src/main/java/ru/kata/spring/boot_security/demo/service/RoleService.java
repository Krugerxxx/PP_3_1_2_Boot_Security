package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Set;

public interface RoleService {
    Role save(Role role);

    public Set<Role> findAll();

    public Role getByName(String name);

}
