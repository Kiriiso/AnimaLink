package com.vet.notifications_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {
    private Long id;
    private String recipient;
    private String message;
    private boolean sent;
    private java.time.LocalDateTime sentAt;
}
