

package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserService userService;


    public UserDetailsServiceImp(UserService userService) {
        this.userService = userService;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        UserDetails user = userService.findByUsername(email);
//
//        if (user == null) {
//            throw new UsernameNotFoundException(String.format("User '%s' не найден", email));
//        }
//       return user;
    List<User> userList = userService.getUsers();
    for (User user : userList) {
        if (user.getEmail().equalsIgnoreCase(email)) {
            return user;
        }
    }
    throw new UsernameNotFoundException("user not found");
    }
}
