package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Cart_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private int quantity;
    private float price;

    public Cart_item(Cart_item cartItem) {
        this.id = cartItem.getId();
        this.cart = cartItem.getCart();
        this.product = cartItem.getProduct();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getPrice();
    }
}
