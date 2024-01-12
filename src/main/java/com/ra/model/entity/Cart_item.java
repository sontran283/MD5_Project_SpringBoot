package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Cart_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private int quantity;
    private float price;
}
