package com.example.tennispartner.service;

import com.example.tennispartner.model.Invitation;
import com.example.tennispartner.model.InvitationStatus;
import com.example.tennispartner.model.User;
import com.example.tennispartner.repository.InvitationRepository;
import com.example.tennispartner.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;

    public InvitationService(InvitationRepository invitationRepository, UserRepository userRepository) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
    }

    public Invitation create(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();
        Invitation inv = Invitation.builder().sender(sender).receiver(receiver).build();
        return invitationRepository.save(inv);
    }

    public List<Invitation> list(Optional<Long> receiverId, Optional<InvitationStatus> status) {
        if (receiverId.isPresent()) return invitationRepository.findByReceiverId(receiverId.get());
        if (status.isPresent()) return invitationRepository.findByStatus(status.get());
        return invitationRepository.findAll();
    }

    public Optional<Invitation> setStatus(Long id, InvitationStatus status) {
        return invitationRepository.findById(id).map(inv -> {
            inv.setStatus(status);
            return invitationRepository.save(inv);
        });
    }
}
