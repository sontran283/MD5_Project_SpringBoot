package com.ra.model.dto.request;

import com.ra.model.dto.OrderDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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