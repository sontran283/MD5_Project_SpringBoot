package com.ra.service;

import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import com.ra.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void addToCart(User user, Product product) {

    }

    @Override
    public void removeFromCart(User user, Product product) {

    }

    @Override
    public List<Product> getCartItems(User user) {
        return null;
    }

    @Override
    public void clearCart(User user) {

    }

    @Override
    public double cartTotal(User user) {
        return 0;
    }
}
