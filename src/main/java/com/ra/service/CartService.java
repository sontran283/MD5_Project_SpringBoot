package com.ra.service;

import com.ra.model.entity.Product;
import com.ra.model.entity.User;

import java.util.List;

public interface CartService {
    void addToCart(User user, Product product);

    void removeFromCart(User user, Product product);

    List<Product> getCartItems(User user);

    void clearCart(User user);

    double cartTotal(User user);
}
