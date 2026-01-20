package com.example.tennispartner.controller;

import com.example.tennispartner.dto.InvitationRequest;
import com.example.tennispartner.model.Invitation;
import com.example.tennispartner.model.InvitationStatus;
import com.example.tennispartner.service.InvitationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping
    public ResponseEntity<List<Invitation>> list(@RequestParam Optional<Long> receiverId,
                                                 @RequestParam Optional<InvitationStatus> status) {
        return ResponseEntity.ok(invitationService.list(receiverId, status));
    }

    @PostMapping
    public ResponseEntity<Invitation> create(@Valid @RequestBody InvitationRequest req) {
        return ResponseEntity.ok(invitationService.create(req.getSenderId(), req.getReceiverId()));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> accept(@PathVariable Long id) {
        return invitationService.setStatus(id, InvitationStatus.ACCEPTED)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/ignore")
    public ResponseEntity<?> ignore(@PathVariable Long id) {
        return invitationService.setStatus(id, InvitationStatus.IGNORED)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
