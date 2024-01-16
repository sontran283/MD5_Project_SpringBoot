package com.ra.controller.user;

import com.ra.exception.ProductNotFoundException;
import com.ra.exception.UserNotFoundException;
import com.ra.model.dto.request.AddtoCartRequestDTO;
import com.ra.model.entity.Cart;
import com.ra.model.entity.Cart_item;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import com.ra.repository.CartItemRepository;
import com.ra.repository.UserRepository;
import com.ra.security.user_principle.UserDetailService;
import com.ra.service.CartItemService;
import com.ra.service.CartService;
import com.ra.service.OrdersService;
import com.ra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    private CartItemService cartItemService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserRepository userRepository;

    // add to cart
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody AddtoCartRequestDTO addtoCartRequestDTO, Authentication authentication) throws UserNotFoundException, ProductNotFoundException {
        Long userId = userDetailService.getUserIdFromAuthentication(authentication);
        cartService.addToCart(userId, addtoCartRequestDTO);
        return new ResponseEntity<>("Product added to cart successfully", HttpStatus.OK);
    }

    // list cart items
    @GetMapping("/index")
    public ResponseEntity<?> index() {
        List<Cart_item> cartItemList = cartItemService.findAll();
        if (cartItemList != null) {
            return new ResponseEntity<>(cartItemList, HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
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

    // cart total
    @GetMapping("/total")
    public ResponseEntity<Double> getCartTotal(@ModelAttribute User user) {
        Cart cart = cartService.getUserCart(user);
        double cartTotal = cartService.cartTotal(user);
        return new ResponseEntity<>(cartTotal, HttpStatus.OK);
    }

    // check out
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(Authentication authentication) {
        try {
            Long userId = userDetailService.getUserIdFromAuthentication(authentication);
            User user = userRepository.findById(userId).orElse(null);
            ordersService.checkout(user);
            cartService.clearCart();
            return new ResponseEntity<>("Checkout successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error during checkout: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
