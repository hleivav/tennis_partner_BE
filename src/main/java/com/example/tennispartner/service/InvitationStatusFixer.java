package com.example.tennispartner.service;

import com.example.tennispartner.model.Invitation;
import com.example.tennispartner.model.InvitationStatus;
import com.example.tennispartner.repository.InvitationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InvitationStatusFixer implements CommandLineRunner {
    private final InvitationRepository invitationRepository;

    public InvitationStatusFixer(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    @Override
    public void run(String... args) {
        invitationRepository.findAll().stream()
            .filter(inv -> inv.getStatus() == null)
            .forEach(inv -> {
                inv.setStatus(InvitationStatus.PENDING);
                invitationRepository.save(inv);
                System.out.println("Updated invitation id=" + inv.getId() + " to PENDING");
            });
    }
}
