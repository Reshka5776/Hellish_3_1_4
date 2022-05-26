package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleService roleService;
    private final ApplicationContext context;


    public UserServiceImpl(UserDao userDao, RoleService roleService, ApplicationContext context) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.context = context;
    }

    @Override
    public List<User> getUsers() {
        return userDao.getUsers();
    }

    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Transactional
    @Override
    public void addUser(User user) {
        setEncryptedPassword(user);
        userDao.adduser(user);
    }

    @Transactional
    @Override
    public void removeUser(long id) {
        userDao.removeUser(id);
    }

    @Transactional
    @Override
    public void editUser(User updatedUser) {
        setEncryptedPassword(updatedUser);
        userDao.editUser(updatedUser);
    }

    @Override
    public void setEncryptedPassword(User user) {
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Override
    public User findByUsername(String email) {
        return userDao.getUserByEmail(email);
    }
    @Transactional
    @Override
    public User setRolesToUser(User user) {

        Set<Role> setRoles = new HashSet<>();
        Set<Role> usersRole = new HashSet<>(user.getRoles());
        user.setRoles(null);

        for (Role role : roleService.getRoles()) {
            for (Role uRole : usersRole) {
                if (role.getName().equals(uRole.getName())) {
                    setRoles.add(role);
                }
            }
        }
        user.setRoles(setRoles);
        return user;
    }
}
