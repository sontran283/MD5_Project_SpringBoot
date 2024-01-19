package com.ra.repository;

import com.ra.model.dto.ICartItem;
import com.ra.model.entity.Cart;
import com.ra.model.entity.Cart_item;
import com.ra.model.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface CartItemRepository extends JpaRepository<Cart_item, Long> {
    @Modifying
    @Query("DELETE FROM Cart_item ci WHERE ci.id = :Id")
    void deleteByCartItemId(@Param("Id") Long Id);

    @Modifying
    @Query("DELETE FROM Cart_item c WHERE c.cart.id = :id")
    void deleteCartItemByCartId(@Param("id") Long id);

    @Query("SELECT ci.product FROM Cart_item ci WHERE ci.cart.user.id = :userId")
    List<Cart_item> getCartItems(@Param("userId") Long userId);

    @Query(value = "select ci.product_id as productId, ci.price, p.image, p.name from " +
            "cart_item ci join product p on p.id = ci.product_id join cart c on c.id = ci.cart_id" +
            " where c.user_id = :userId", nativeQuery = true)
    List<ICartItem> getCartItems1(@Param("userId") Long userId);


    Boolean existsCart_itemByCartAndProduct(Cart cart, Product product);

    Cart_item findByCartAndProduct(Cart cart, Product product);

    Cart_item findCart_itemById(Long id);

    List<Cart_item> findAllByCart(Cart cart);
}
