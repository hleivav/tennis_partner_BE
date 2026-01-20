package com.example.tennispartner.config;

import com.example.tennispartner.model.User;
import com.example.tennispartner.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .email("admin@example.com")
                    .password("admin") // För utveckling – byt i prod
                    .fullName("Super Admin")
                    .role("SUPERADMIN")
                    .build();

            User user = User.builder()
                    .email("user@example.com")
                    .password("password")
                    .fullName("Test User")
                    .role("USER")
                    .build();

            userRepository.save(admin);
            userRepository.save(user);
        }
    }
}
