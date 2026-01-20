package com.example.tennispartner.controller;

import com.example.tennispartner.model.User;
import com.example.tennispartner.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> u = userService.findById(id);
        return u.map(user -> {
            user.setPassword(null);
            return ResponseEntity.ok(user);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User update) {
        return userService.findById(id).map(user -> {
            user.setFullName(update.getFullName() == null ? user.getFullName() : update.getFullName());
            // do not allow changing password/email here for simplicity
            user.setRole(update.getRole() == null ? user.getRole() : update.getRole());
            userService.updateProfile(user);
            user.setPassword(null);
            return ResponseEntity.ok(user);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
