package com.ra.repository;

import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Page<Orders> findOrdersById(Pageable pageable, Integer id);

    List<Orders> findAllByStatus(Integer status);

    List<Orders> findByUser(User user);
}
