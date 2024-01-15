package com.ra.model.dto.request;

import com.ra.model.dto.OrderDetailDTO;
import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private LocalDateTime order_date;
    private Long userId;
    private int status;
}
