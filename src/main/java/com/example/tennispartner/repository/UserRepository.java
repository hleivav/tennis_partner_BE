package com.example.tennispartner.repository;

import com.example.tennispartner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // Ta bort alla användare som inte är superadmin
    void deleteAllByRoleNot(String role);
}
