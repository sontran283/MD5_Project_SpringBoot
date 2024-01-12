package com.ra.repository;

import com.ra.model.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> searchProductByName(Pageable pageable, String name);

    @Modifying
    @Query("update Product p set p.status=case when p.status=true then false else true end where p.id=?1")
    void changeStatus(Long id);
}
