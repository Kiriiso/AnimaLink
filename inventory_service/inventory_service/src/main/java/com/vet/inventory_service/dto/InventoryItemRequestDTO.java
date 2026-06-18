package com.vet.inventory_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemRequestDTO {
    private Long id;
    private String name;
    private String sku;
    private Integer quantity;
    private java.math.BigDecimal price;
}
