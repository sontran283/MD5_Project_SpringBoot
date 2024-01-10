package com.ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderRequestDTO {
    private String address;
    private String phone;
    private String note;
    private float total;
    private Long userId;
    private int status;
}
