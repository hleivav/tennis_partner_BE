package com.example.tennispartner.model;

import jakarta.persistence.*;

@Entity
public class AdminConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tournamentName;

    @Column(nullable = false)
    private String deadline;

    public AdminConfig() {}

    public AdminConfig(String tournamentName, String deadline) {
        this.tournamentName = tournamentName;
        this.deadline = deadline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
