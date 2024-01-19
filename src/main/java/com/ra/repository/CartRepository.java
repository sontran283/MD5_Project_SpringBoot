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
    @Query("SELECT ci.product FROM Cart_item ci WHERE ci.cart.user.id = :userId")
    List<Product> getCartItems(@Param("userId") Long userId);
}
