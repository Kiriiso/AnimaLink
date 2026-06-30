package com.vet.users_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private java.time.LocalDateTime createdAt;
}
