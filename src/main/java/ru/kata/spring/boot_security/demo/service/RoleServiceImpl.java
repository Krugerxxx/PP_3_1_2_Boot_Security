package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(@Qualifier(value = "roleDaoImpl") RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> listAll() {
        return roleDao.findAll();
    }

    @Override
    public Role save(Role role) {
        roleDao.save(role);
        return role;
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
