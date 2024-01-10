package com.ra.model.dto.response;

import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResponseDTO {
    private Long id;
    private String name;
    private Float price;
    private String image;
    private Long categoryId;
    private Boolean status = true;

    public ProductResponseDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.image = product.getImage();
        this.categoryId = product.getCategory().getId();
        this.status = product.getStatus();
    }
    public ProductRequestDTO toProductRequestDTO() {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName(this.getName());
        productRequestDTO.setPrice(this.getPrice());
        productRequestDTO.setFile(null);
        productRequestDTO.setCategoryId(this.getCategoryId());
        return productRequestDTO;
    }
}
