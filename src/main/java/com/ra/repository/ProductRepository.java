package com.ra.repository;

import com.ra.model.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> searchProductByName(Pageable pageable, String name);

    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE LOWER(p.name) = LOWER(:productName) AND p.id <> :productId")
    boolean existsByNameIgnoreCaseAndIdNot(@Param("productName") String productName, @Param("productId") Long productId);

    Boolean existsByName(String productName);

    @Modifying
    @Query("update Product p set p.status=case when p.status=true then false else true end where p.id=?1")
    void changeStatus(Long id);
}
