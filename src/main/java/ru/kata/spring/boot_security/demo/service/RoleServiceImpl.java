package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    @Qualifier(value = "roleDaoImpl")
    RoleDao roleDao;

    @Override
    public List<Role> listAll() {
        return roleDao.findAll();
    }

    @Override
    public List<Role> getByName(String name) {
        List<Role> roles = new ArrayList<>();
        for (Role role : listAll()) {
            if (name.contains(role.getName()))
                roles.add(role);
        }
        return roles;
    }
}
