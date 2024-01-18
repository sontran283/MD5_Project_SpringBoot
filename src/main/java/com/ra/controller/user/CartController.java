package com.ra.controller.user;

import com.ra.exception.ProductNotFoundException;
import com.ra.exception.UserNotFoundException;
import com.ra.model.dto.ICartItem;
import com.ra.model.dto.request.AddtoCartRequestDTO;
import com.ra.model.entity.Cart_item;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import com.ra.repository.CartItemRepository;
import com.ra.repository.UserRepository;
import com.ra.security.user_principle.UserDetailService;
import com.ra.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;


    // add to cart
    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestBody AddtoCartRequestDTO addtoCartRequestDTO, Authentication authentication, User user) {
        try {
            // nếu chưa đăng nhập thì bắt đăng nhập mới addtocart
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity<>("User not logged in. Please log in", HttpStatus.UNAUTHORIZED);
            }

            Long userId = userDetailService.getUserIdFromAuthentication(authentication);
            cartService.addToCart(userId, addtoCartRequestDTO);
            return new ResponseEntity<>("Product added to cart successfully", HttpStatus.OK);

        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("Product not found. Please enter a valid product code.", HttpStatus.NOT_FOUND);
        }
    }


    // index
    @GetMapping("/index")
    public ResponseEntity<?> index(Authentication authentication) {
        try {
            Long userId = userDetailService.getUserIdFromAuthentication(authentication);
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
            List<ICartItem> cartItemList = cartItemRepository.getCartItems1(user.getId());
            if (!cartItemList.isEmpty()) {
                return new ResponseEntity<>(cartItemList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cart is empty", HttpStatus.NOT_FOUND);
            }
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }


    // delete by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long id) {
        Cart_item cartItem = cartItemRepository.findCart_itemById(id);
        if (cartItem != null) {
            cartItemRepository.deleteById(cartItem.getId());
            return new ResponseEntity<>("Product removed from cart successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }


    // clear All
    @DeleteMapping("/clearAll")
    public ResponseEntity<String> clearCart() {
        cartService.clearCart();
        return new ResponseEntity<>("Cart cleared successfully", HttpStatus.OK);
    }


    // check out
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(Authentication authentication) {
        try {
            Long userId = userDetailService.getUserIdFromAuthentication(authentication);
            User user = userRepository.findById(userId).orElse(null);

            // nếu cart rỗng thì không được checkout
            if (!user.getCart().getCartItems().isEmpty() && user != null) {
                Orders orders = ordersService.checkout(user);
                emailService.sendThanks("thanks!", orders);
                return new ResponseEntity<>("Checkout successful", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cart is empty. Cannot proceed with checkout", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error during checkout: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
