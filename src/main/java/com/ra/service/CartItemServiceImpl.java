package com.ra.service;

import com.ra.model.entity.Cart_item;
import com.ra.model.entity.User;
import com.ra.repository.CartItemRepository;
import com.ra.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<Cart_item> findAll(User user) {
        return cartItemRepository.getCartItems(user.getId());
    }

    @Override
    public List<Cart_item> getCartItems(User user) {
        return cartItemRepository.getCartItems(user.getId());
    }
}
