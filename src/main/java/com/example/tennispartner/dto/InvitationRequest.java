package com.example.tennispartner.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class InvitationRequest {
    @NotNull
    private Long senderId;

    @NotNull
    private Long receiverId;
}
