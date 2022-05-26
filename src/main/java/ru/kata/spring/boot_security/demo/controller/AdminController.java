package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImp;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Secured("ROLE_ADMIN")
@RestController("/api")
public class AdminController {
    private UserDetailsServiceImp userDetailsServiceImp;
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setUserDetailsServiceImp(UserDetailsServiceImp userDetailsServiceImp) {
        this.userDetailsServiceImp = userDetailsServiceImp;
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/admin/loggedUser")
    public ResponseEntity<User> getLoggedUser(Principal principal) {
        User loggedUser = userDetailsServiceImp.findByUsername(principal.getName());
        return ResponseEntity.ok(loggedUser);
    }

    @GetMapping("/admin/roles")
    public ResponseEntity<Set<Role>> getRoleList() {
        Set<Role> listRoles = roleService.getRoles();
        return ResponseEntity.ok(listRoles);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getUserList() {


        List<User> userList = userService.getUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("admin/users/{id}")
    public ResponseEntity<User> getSelectedUser(@PathVariable("id") long id, Principal principal) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/admin/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User updatedUser = userService.setRolesToUser(user);
        userService.addUser(updatedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("admin/users/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user,
                                        @PathVariable("id") long id) {
        User updatedUser = userService.setRolesToUser(user);
        userService.editUser(updatedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("admin/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        userService.removeUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}


