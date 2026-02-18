package com.example.tennispartner.repository;

import com.example.tennispartner.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM Match m WHERE m.player1.email <> :superadminEmail OR m.player2 IS NULL OR m.player2.email <> :superadminEmail")
    void deleteAllInvolvingNonSuperadmin(@org.springframework.data.repository.query.Param("superadminEmail") String superadminEmail);
}
