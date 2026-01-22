package com.example.tennispartner.controller;

import com.example.tennispartner.model.AdminConfig;
import com.example.tennispartner.repository.AdminConfigRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/config")
public class AdminConfigController {
    @Autowired
    private AdminConfigRepository adminConfigRepository;

    @GetMapping
    public ResponseEntity<?> getConfig() {
        Optional<AdminConfig> configOpt = adminConfigRepository.findById(1L);
        if (configOpt.isPresent()) {
            return ResponseEntity.ok(configOpt.get());
        } else {
            // Returnera tomt objekt om inget finns
            return ResponseEntity.ok(new AdminConfig("", ""));
        }
    }

    @PostMapping
    public ResponseEntity<?> saveConfig(@RequestBody AdminConfig cfg) {
        cfg.setId(1L); // Alltid samma id, endast en config
        AdminConfig saved = adminConfigRepository.save(cfg);
        return ResponseEntity.ok(saved);
    }
}
