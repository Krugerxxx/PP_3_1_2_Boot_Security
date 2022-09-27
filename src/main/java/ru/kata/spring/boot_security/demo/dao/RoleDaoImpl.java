package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.repository.CrudRepository;
import ru.kata.spring.boot_security.demo.models.Role;

public interface RoleDaoImpl extends RoleDao, CrudRepository<Role, Long> {

}
