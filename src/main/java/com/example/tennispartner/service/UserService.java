
package com.example.tennispartner.service;

import com.example.tennispartner.model.User;
import com.example.tennispartner.repository.InvitationRepository;
import com.example.tennispartner.repository.MatchRepository;
import com.example.tennispartner.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Service
public class UserService {
    // Radera alla användare
    public void deleteAll() {
        userRepository.deleteAll();
    }

    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final MatchRepository matchRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository,
                       InvitationRepository invitationRepository,
                       MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.matchRepository = matchRepository;
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        if (user.getRole() == null) user.setRole("USER");
        return userRepository.save(user);
    }

    public Optional<User> login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            System.out.println("[UserService] Ingen användare hittad för: " + email);
            return Optional.empty();
        }
        User u = userOpt.get();
        System.out.println("[UserService] Hittad användare: " + u.getEmail() + ", hash: " + u.getPassword());
        boolean match = encoder.matches(rawPassword, u.getPassword());
        System.out.println("[UserService] Lösenord matchar? " + match);
        if (match) return Optional.of(u);
        return Optional.empty();
    }

    public User updateProfile(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    // Hämta alla användare
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    // Ta bort alla användare utom superadmin
    @Transactional
    public void deleteAllExceptSuperadmin() {
        String superadminEmail = "hleiva@hotmail.com";
        // Ta bort beroenden (FK) innan användarna tas bort
        invitationRepository.deleteAllInvolvingNonSuperadmin(superadminEmail);
        matchRepository.deleteAllInvolvingNonSuperadmin(superadminEmail);
        userRepository.deleteAllByEmailNot(superadminEmail);
    }
}
