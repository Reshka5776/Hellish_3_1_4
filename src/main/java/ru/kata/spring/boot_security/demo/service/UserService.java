package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User getUserById(long id);

    void addUser(User user);

    void removeUser(long id);

    void editUser(User updatedUser);

    void setEncryptedPassword(User user);

    User findByUsername(String username);

    User setRolesToUser(User user);


}
