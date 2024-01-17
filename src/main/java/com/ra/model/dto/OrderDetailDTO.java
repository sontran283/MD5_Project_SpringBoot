package com.ra.model.dto;

import com.ra.model.entity.OrderDetail;
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

    public OrderDetailDTO(OrderDetail orderDetail) {
        this.id = orderDetail.getId();
        this.price = orderDetail.getPrice();
        this.quantity = orderDetail.getQuantity();
        this.productId = orderDetail.getProduct().getId();
        this.ordersId = orderDetail.getOrders().getId();
    }
}
