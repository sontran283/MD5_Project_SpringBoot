package com.ra.service;

import com.ra.exception.ProductNotFoundException;
import com.ra.exception.UserNotFoundException;
import com.ra.model.dto.request.AddtoCartRequestDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Cart;
import com.ra.model.entity.Cart_item;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import com.ra.repository.CartItemRepository;
import com.ra.repository.CartRepository;
import com.ra.repository.ProductRepository;
import com.ra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart getUserCart(User user) {
        return cartRepository.findByUser(user);
    }

    @Override
    public List<Cart> findAll() {
        List<Cart> cartList = cartRepository.findAll();
        return cartList.stream().map(Cart::new).toList();
    }

    @Override
    public void addToCart(Long userId, AddtoCartRequestDTO addtoCartRequestDTO) throws UserNotFoundException, ProductNotFoundException {
        User user1=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User id not found"));
        Cart cart=cartRepository.findByUser(user1);
        Product product=productRepository.findById(addtoCartRequestDTO.getProductId()).orElseThrow(()->new ProductNotFoundException("product id not found"));
        if (cart==null){
            Cart cart1=new Cart();
            cart1.setUser(user1);
            cartRepository.save(cart1);
        }

        Cart_item cartItem=cartItemRepository.findAllByCartAndProduct(cart,product);
        Cart_item cartItem1=new Cart_item();
        cartItem1.setCart(cart);
        cartItem1.setProduct(product);
        cartItem1.setQuantity(addtoCartRequestDTO.getQuantity());
        cartItem1.setPrice(product.getPrice());
        cartItemRepository.save(cartItem1);
    }

    @Override
    public void removeFromCart(User user, Product product) {
        cartRepository.removeFromCart(user.getId(), product.getId());
    }

    @Override
    public List<Product> getCartItems(User user) {
        return cartRepository.getCartItems(user.getId());
    }

    @Override
    public void clearCart(User user) {
        cartRepository.clearCart(user.getId());
    }

    @Override
    public double cartTotal(User user) {
        List<Product> cartItems = getCartItems(user);
        return cartItems.stream().mapToDouble(Product::getPrice).sum();
    }
}
