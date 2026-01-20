package com.example.tennispartner.repository;

import com.example.tennispartner.model.Invitation;
import com.example.tennispartner.model.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByReceiverId(Long receiverId);
    List<Invitation> findByStatus(InvitationStatus status);
}
