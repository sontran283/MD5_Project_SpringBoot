package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String categoryName;
    @Column(columnDefinition = "boolean default true")
    private Boolean status = true;
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products;
    // mappedBy = "category" ___ chỉ định trường nào trong đối tượng Product sẽ quản lý mối quan hệ
    // cascade = CascadeType.REMOVE ___ nghĩa là khi đối tượng Category bị xóa, tất cả các sản phẩm liên quan cũng sẽ bị xóa.
    // fetch = FetchType.LAZY ___ chỉ định cách dữ liệu liên quan (trong trường hợp này là danh sách các sản phẩm) sẽ được tải vào bộ nhớ (lazy loading, tải khi cần).
    public Category(Long Id, String categoryName, Boolean status) {
        this.Id = Id;
        this.categoryName = categoryName;
        this.status = status;
    }
}
