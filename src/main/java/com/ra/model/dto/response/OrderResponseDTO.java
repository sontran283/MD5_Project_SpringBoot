package com.ra.model.dto.response;

import com.ra.model.dto.OrderDetailDTO;
import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponseDTO {
    private Long id;
    private String address;
    private String phone;
    private String note;
    private float total;
    private Date order_date;
    private Long userId;
    private Set<OrderDetail> orderDetails;
    private int status;

    public OrderResponseDTO(Orders orders) {
        this.id = orders.getId();
        this.address = orders.getAddress();
        this.phone = orders.getPhone();
        this.note = orders.getNote();
        this.total = orders.getTotal();
        this.order_date = orders.getOrder_date();
        this.userId = orders.getUser().getId();
        this.orderDetails = orders.getOrderDetails();
        this.status = orders.getStatus();
    }
}
