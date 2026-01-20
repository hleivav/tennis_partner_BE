package com.example.tennispartner.controller;

import com.example.tennispartner.dto.AuthRequest;
import com.example.tennispartner.dto.AuthResponse;
import com.example.tennispartner.model.User;
import com.example.tennispartner.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody User user) {
        User saved = userService.register(user);
        return ResponseEntity.ok(new AuthResponse(saved.getId(), saved.getEmail(), saved.getFullName(), saved.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest req) {
        var opt = userService.login(req.getEmail(), req.getPassword());
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        var u = opt.get();
        return ResponseEntity.ok(new AuthResponse(u.getId(), u.getEmail(), u.getFullName(), u.getRole()));
    }
}
