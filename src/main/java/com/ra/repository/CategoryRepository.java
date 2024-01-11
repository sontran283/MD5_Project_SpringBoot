package com.ra.repository;

import com.ra.model.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAllByCategoryNameContainingIgnoreCase(Pageable pageable, String name);

    Boolean existsByCategoryName(String categoryName);

    @Modifying
    @Query("update Category c set c.status=case when c.status=true then false else true end where c.Id=?1")
    void changeStatus(Long id);
}
