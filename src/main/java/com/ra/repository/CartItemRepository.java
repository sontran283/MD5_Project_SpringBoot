package com.ra.repository;

import com.ra.model.entity.Cart;
import com.ra.model.entity.Cart_item;
import com.ra.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<Cart_item,Long> {
    Cart_item findByCart(Cart cart);
    Cart_item findAllByCartAndProduct(Cart cart, Product product);
}