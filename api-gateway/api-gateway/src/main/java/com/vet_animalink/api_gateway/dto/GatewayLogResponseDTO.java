package com.vet_animalink.api_gateway.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayLogResponseDTO {
    private Long id;
    private String path;
    private String method;
    private Integer status;
    private java.time.LocalDateTime timestamp;
}
