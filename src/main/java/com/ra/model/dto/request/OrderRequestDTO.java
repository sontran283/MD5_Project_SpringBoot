package com.ra.model.dto.request;


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
    private String address;
    private String phone;
    private String note;
    private float total;
    private LocalDateTime order_date;
    private Long userId;
    private int status;
}
