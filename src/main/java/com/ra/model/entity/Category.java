package com.ra.model.entity;

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
    // mappedBy = "category" ___ chỉ định trường nào trong đối tượng Product sẽ quản lý mối quan hệ
    // cascade = CascadeType.REMOVE ___ nghĩa là khi đối tượng Category bị xóa, tất cả các sản phẩm liên quan cũng sẽ bị xóa.
    // fetch = FetchType.LAZY ___ chỉ định cách dữ liệu liên quan (trong trường hợp này là danh sách các sản phẩm) sẽ được tải vào bộ nhớ (lazy loading, tải khi cần).
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Product> products;
    public Category(Long id, String categoryName, Boolean status) {
    }
}
