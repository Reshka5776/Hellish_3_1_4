package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;

public interface UserDao {

    List<User> getUsers();

    User getUserById(long id);

    void adduser(User user);

    void removeUser(long id);

    void editUser(User updatedUser);

    User getUserByEmail(String email);
}
