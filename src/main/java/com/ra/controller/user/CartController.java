package com.ra.controller.user;

import com.ra.exception.ProductNotFoundException;
import com.ra.exception.UserNotFoundException;
import com.ra.model.dto.request.AddtoCartRequestDTO;
import com.ra.model.entity.Cart;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import com.ra.security.user_principle.UserDetailService;
import com.ra.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserDetailService userDetailService;

    // add to cart
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody AddtoCartRequestDTO addtoCartRequestDTO, Authentication authentication) throws UserNotFoundException, ProductNotFoundException {
        Long userId=userDetailService.getUserIdFromAuthentication(authentication);
        cartService.addToCart(userId,addtoCartRequestDTO);
        return new ResponseEntity<>("Product added to cart successfully", HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestBody Product product, @RequestParam User user) {
        Cart cart = cartService.getUserCart(user);
        cartService.removeFromCart(user, product);
        return new ResponseEntity<>("Product removed from cart successfully", HttpStatus.OK);
    }

    // list cart items
    @GetMapping("/index")
    public ResponseEntity<List<Product>> getCartItems(@RequestParam User user) {
        Cart cart = cartService.getUserCart(user);
        List<Product> cartItems = cartService.getCartItems(user);
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    // delete all
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam User user) {
        Cart cart = cartService.getUserCart(user);
        cartService.clearCart(user);
        return new ResponseEntity<>("Cart cleared successfully", HttpStatus.OK);
    }

    // cart total
    @GetMapping("/total")
    public ResponseEntity<Double> getCartTotal(@RequestParam User user) {
        Cart cart = cartService.getUserCart(user);
        double cartTotal = cartService.cartTotal(user);
        return new ResponseEntity<>(cartTotal, HttpStatus.OK);
    }
}
