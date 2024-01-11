package com.ra.model.dto.request;

import com.ra.model.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductRequestDTO {
    private Long id;
    private String name;
    private Float price;
    private MultipartFile file;
    private Long categoryId;
    private Boolean status = true;
}
