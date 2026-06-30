package com.vet_animalink.api_gateway.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gateway_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;
    private String method;
    private Integer status;
    private LocalDateTime timestamp;
}
