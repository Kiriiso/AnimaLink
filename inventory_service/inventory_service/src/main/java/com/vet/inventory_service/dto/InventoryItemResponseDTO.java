package com.vet.inventory_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemResponseDTO {
    private Long id;
    private String name;
    private String sku;
    private Integer quantity;
    private java.math.BigDecimal price;
}
