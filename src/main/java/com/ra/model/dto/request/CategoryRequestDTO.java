package com.ra.model.dto.request;

import com.ra.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryRequestDTO {
    private Long Id;
    private String categoryName;
    private Boolean status = true;
}
