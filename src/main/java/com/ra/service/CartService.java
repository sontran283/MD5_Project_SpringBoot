package com.ra.service;

import com.ra.exception.ProductNotFoundException;
import com.ra.exception.UserNotFoundException;
import com.ra.model.dto.request.AddtoCartRequestDTO;
import com.ra.model.entity.Cart;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;

import java.util.List;

public interface CartService {
    Cart getUserCart(User user);
    List<Cart> findAll();
    void addToCart(Long userId, AddtoCartRequestDTO addtoCartRequestDTO) throws UserNotFoundException, ProductNotFoundException;

    void removeFromCart(User user, Product product);

    List<Product> getCartItems(User user);

    void clearCart(User user);

    double cartTotal(User user);
}
