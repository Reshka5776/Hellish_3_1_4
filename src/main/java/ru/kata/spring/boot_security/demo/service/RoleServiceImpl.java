package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao;

    @Autowired
    public void RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Transactional
    @Override
    public void save(Role role) {
        roleDao.save(role);
    }

    @Transactional
    @Override
    public void update(Role updatedRole) {
        roleDao.update(updatedRole);
    }

    @Override
    public Role getRoleById(long id) {
        return roleDao.getRoleById(id);
    }

    @Transactional
    @Override
    public void delete(long id) {
        roleDao.removeRole(id);
    }

    @Override
    public Set<Role> getRoles() {
        return roleDao.getRoles();
    }
}
