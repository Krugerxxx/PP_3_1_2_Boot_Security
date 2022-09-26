package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.repository.CrudRepository;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleDaoImpl extends RoleDao, CrudRepository<Role, Long>  {


}
