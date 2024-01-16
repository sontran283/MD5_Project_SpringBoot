package com.ra.service;

import com.ra.model.entity.Cart_item;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;

import java.util.List;

public interface CartItemService {
    List<Cart_item> findAll(User user);

    List<Cart_item> getCartItems(User user);
}
