

package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public UserDetailsServiceImp(UserService userService) {
        this.userService = userService;
    }


    public User findByUsername(String email) {
        return userService.findByUsername(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByUsername(email);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' не найден", email));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                user.getAuthorities());
    }
}
