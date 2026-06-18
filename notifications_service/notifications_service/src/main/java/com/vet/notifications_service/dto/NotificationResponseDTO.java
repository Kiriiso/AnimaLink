package com.vet.notifications_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private String recipient;
    private String message;
    private boolean sent;
    private java.time.LocalDateTime sentAt;
}
