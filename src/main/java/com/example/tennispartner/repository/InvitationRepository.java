package com.example.tennispartner.repository;

import com.example.tennispartner.model.Invitation;
import com.example.tennispartner.model.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByReceiverId(Long receiverId);
    List<Invitation> findByStatus(InvitationStatus status);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM Invitation i WHERE i.sender.email <> :superadminEmail OR i.receiver.email <> :superadminEmail")
    void deleteAllInvolvingNonSuperadmin(@org.springframework.data.repository.query.Param("superadminEmail") String superadminEmail);
}
