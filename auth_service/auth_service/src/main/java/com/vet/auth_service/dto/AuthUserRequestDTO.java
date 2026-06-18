package com.vet.auth_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserRequestDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
}
