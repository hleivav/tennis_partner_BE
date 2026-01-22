package com.example.tennispartner.controller;

import com.example.tennispartner.model.User;
import com.example.tennispartner.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
        @DeleteMapping("/all-except-superadmin")
        public ResponseEntity<?> deleteAllExceptSuperadmin() {
            userService.deleteAllExceptSuperadmin();
            return ResponseEntity.ok().build();
        }
    @GetMapping
    public ResponseEntity<?> listUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> u = userService.findById(id);
            return u.map(user -> {
                user.setPassword(null);
                // Returnera endast relevanta profilfält
                return ResponseEntity.ok(new com.example.tennispartner.dto.AuthResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole(),
                    user.getAvatar(),
                    user.getLevel(),
                    user.getSeekingPartner(),
                    user.getPhone()
                ));
            }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User update) {
        return userService.findById(id).map(user -> {
            user.setName(update.getName() == null ? user.getName() : update.getName());
            user.setRole(update.getRole() == null ? user.getRole() : update.getRole());
            user.setEmail(update.getEmail() == null ? user.getEmail() : update.getEmail());
            user.setAvatar(update.getAvatar() == null ? user.getAvatar() : update.getAvatar());
            user.setLevel(update.getLevel() == null ? user.getLevel() : update.getLevel());
            user.setSeekingPartner(update.getSeekingPartner() == null ? user.getSeekingPartner() : update.getSeekingPartner());
            user.setPhone(update.getPhone() == null ? user.getPhone() : update.getPhone());
            if (update.getPassword() != null && !update.getPassword().isEmpty()) {
                userService.updatePassword(user, update.getPassword());
            } else {
                userService.updateProfile(user);
            }
            user.setPassword(null);
                // Returnera endast relevanta profilfält
                return ResponseEntity.ok(new com.example.tennispartner.dto.AuthResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole(),
                    user.getAvatar(),
                    user.getLevel(),
                    user.getSeekingPartner(),
                    user.getPhone()
                ));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
