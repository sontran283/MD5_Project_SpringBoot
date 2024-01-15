package com.ra.repository;

import com.ra.model.entity.Cart;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);

    @Modifying
    @Query("UPDATE Cart_item ci SET ci.cart = :cart, ci.product = :product WHERE ci.cart.user.id = :userId AND ci.product.id = :productId")
    void addToCart(@Param("userId") Long userId, @Param("productId") Long productId, @Param("cart") Cart cart, @Param("product") Product product);

    @Modifying
    @Query("DELETE FROM Cart_item ci WHERE ci.cart.user.id = :userId AND ci.product.id = :productId")
    void removeFromCart(@Param("userId") Long userId, @Param("productId") Long productId);

    @Modifying
    @Query("SELECT ci.product FROM Cart_item ci WHERE ci.cart.user.id = :userId")
    List<Product> getCartItems(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Cart_item ci WHERE ci.cart.user.id = :userId")
    void clearCart(@Param("userId") Long userId);
}
