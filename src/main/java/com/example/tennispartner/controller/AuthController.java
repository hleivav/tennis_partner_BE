package com.example.tennispartner.controller;

import org.springframework.transaction.annotation.Transactional;

import com.example.tennispartner.dto.AuthRequest;
import com.example.tennispartner.dto.AuthResponse;
import com.example.tennispartner.model.User;
import com.example.tennispartner.service.UserService;
import com.example.tennispartner.dto.PasswordResetRequest;
import com.example.tennispartner.dto.PasswordResetConfirmRequest;
import com.example.tennispartner.model.PasswordResetToken;
import com.example.tennispartner.repository.PasswordResetTokenRepository;
import com.example.tennispartner.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // Radera alla användare (för felsökning)
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteAllUsers() {
        userService.deleteAll();
        return ResponseEntity.ok("Alla användare raderade.");
    }

    private final UserService userService;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    public AuthController(UserService userService, PasswordResetTokenRepository tokenRepository, EmailService emailService) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody User user) {
        User saved = userService.register(user);
        return ResponseEntity.ok(new AuthResponse(
            saved.getId(),
            saved.getEmail(),
            saved.getName(),
            saved.getRole(),
            saved.getAvatar(),
            saved.getLevel(),
            saved.getSeekingPartner(),
            saved.getPhone()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest req) {
        System.out.println("[AuthController] /login POST: email=" + req.getEmail() + ", password=" + req.getPassword());
        var opt = userService.login(req.getEmail(), req.getPassword());
        if (opt.isEmpty()) {
            System.out.println("[AuthController] /login: INVALID credentials");
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        var u = opt.get();
        AuthResponse resp = new AuthResponse(
            u.getId(),
            u.getEmail(),
            u.getName(),
            u.getRole(),
            u.getAvatar(),
            u.getLevel(),
            u.getSeekingPartner(),
            u.getPhone()
        );
        System.out.println("[AuthController] /login: response=" + resp);
        return ResponseEntity.ok(resp);
    }

    @Transactional
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetRequest request) {
        System.out.println("forgotPassword endpoint anropad med: " + request.getEmail());
        var userOpt = userService.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        // Remove old tokens for this email
        tokenRepository.deleteByEmail(request.getEmail());
        // Generate token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(2);
        PasswordResetToken prt = new PasswordResetToken(token, request.getEmail(), expiry);
        tokenRepository.save(prt);
        // Skicka e-post
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        String body = "Hej!\n\nKlicka på länken för att återställa ditt lösenord: " + resetLink + "\n\nLänken är giltig i 2 timmar.";
        emailService.sendSimpleMessage(request.getEmail(), "Återställ ditt lösenord", body);
        return ResponseEntity.ok("Återställningslänk skickad om e-post finns registrerad.");
    }

    // Lista alla användare (tillfälligt för felsökning)
    @GetMapping("/users")
    public ResponseEntity<?> listUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Transactional
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetConfirmRequest request) {
        var tokenOpt = tokenRepository.findByToken(request.getToken());
        if (tokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Ogiltig eller utgången länk");
        }
        var prt = tokenOpt.get();
        if (prt.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Länken har gått ut");
        }
        var userOpt = userService.findByEmail(prt.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Användare saknas");
        }
        userService.updatePassword(userOpt.get(), request.getNewPassword());
        tokenRepository.deleteByEmail(prt.getEmail());
        return ResponseEntity.ok("Lösenordet är nu uppdaterat");
    }
}
