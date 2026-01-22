package com.example.tennispartner.config;

import com.example.tennispartner.model.User;
import com.example.tennispartner.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String adminEmail = "admin@admin.se";
        String adminPassword = "admin123";
        User admin = userRepository.findByEmail(adminEmail)
            .orElse(User.builder()
                .email(adminEmail)
                .name("Admin")
                .role("SUPERADMIN")
                .build());
        admin.setPassword(encoder.encode(adminPassword));
        admin.setRole("SUPERADMIN");
        userRepository.save(admin);
        System.out.println("\n==============================");
        System.out.println("ADMIN-KONTO TILLGÄNGLIGT!");
        System.out.println("E-post:    " + adminEmail);
        System.out.println("Lösenord:  " + adminPassword);
        System.out.println("Roll:      SUPERADMIN");
        System.out.println("==============================\n");
    }
}
