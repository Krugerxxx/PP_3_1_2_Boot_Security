package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleRepo;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepo roleRepo;

    @Autowired
    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Role save(Role role) {
        try {
            roleRepo.save(role);
        }catch (Exception e){
            System.out.println("Произошла ошибка при сохранении роли, скорее всего такая роль существует");
        }
        return role;
    }

    @Override
    public Set<Role> findAll() {
        Set<Role> roleSet = new HashSet<>();
        roleRepo.findAll().forEach(n -> roleSet.add(n));
        return roleSet;
    }

    @Override
    public Role getByName(String name) {
        for (Role role : findAll()) {
            if (name.contains(role.getName())){
                return role;
            }
        }
        return null;
    }
}
