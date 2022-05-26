package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    void save(Role role);

    void update(Role updatedRole);

    Role getRoleById(long id);

    void delete(long id);

    Set<Role> getRoles();
}

