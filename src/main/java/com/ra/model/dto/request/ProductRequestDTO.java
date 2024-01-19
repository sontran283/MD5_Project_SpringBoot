package com.ra.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


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
