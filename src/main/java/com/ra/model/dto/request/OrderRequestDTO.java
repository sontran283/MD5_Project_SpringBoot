package com.ra.model.dto.request;


import com.ra.model.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderRequestDTO {
    private Long id;
    private String address;
    private String phone;
    private String note;
    private float total;
    private LocalDateTime order_date;
    private Long userId;
    private int status;

    public OrderRequestDTO(Orders orders) {
        this.id = orders.getId();
        this.address = orders.getAddress();
        this.phone = orders.getPhone();
        this.note = orders.getNote();
        this.total = orders.getTotal();
        this.order_date = orders.getOrder_date();
        this.userId = orders.getUser().getId();
        this.status = orders.getStatus();
    }
}
