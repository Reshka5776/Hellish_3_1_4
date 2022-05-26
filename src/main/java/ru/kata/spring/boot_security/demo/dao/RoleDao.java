package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {
    void save(Role role);

    void update(Role updatedRole);

    Role getRoleById(long id);

    void removeRole(long id);

    Set<Role> getRoles();
}
