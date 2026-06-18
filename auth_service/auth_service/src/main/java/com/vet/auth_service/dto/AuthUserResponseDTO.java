package com.vet.auth_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserResponseDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
}
