package com.example.tennispartner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Long id;
    private String email;
    private String name;
    private String role;
    private String avatar;
    private String level;
    private Boolean seekingPartner;
    private String phone;
}
