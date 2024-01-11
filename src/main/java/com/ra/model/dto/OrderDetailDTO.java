package com.ra.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetailDTO {
    private Long id;
    private float price;
    private int quantity;
    private Long productId;
    private Long ordersId;
}
