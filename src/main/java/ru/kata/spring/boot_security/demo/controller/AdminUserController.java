package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;

import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImp;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping
public class AdminUserController {

    private final UserService userService;
    private final UserDetailsServiceImp userDetailsService;
    private final RoleService roleService;


    public AdminUserController(UserService userService, UserDetailsServiceImp userDetailsService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/admin")
    public String printUsers(ModelMap model, @AuthenticationPrincipal User user) {
        model.addAttribute("initUser", userDetailsService.loadUserByUsername(user.getEmail()));
        model.addAttribute("users", userService.getUsers());
        return "userAndAdmin";
    }



    @GetMapping("/user")
    public String pageUser(@AuthenticationPrincipal User user, ModelMap model) {
        model.addAttribute("userPage", userService.getUserById(user.getId()));
        model.addAttribute("user", userDetailsService.loadUserByUsername(user.getEmail()));
        return "userAndAdmin";
    }


}
