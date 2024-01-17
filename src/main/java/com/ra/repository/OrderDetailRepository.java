package com.ra.repository;

import com.ra.model.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    Page<OrderDetail> searchOrderDetailsById(Pageable pageable, Integer id);

    List<OrderDetail> findAllByOrders_Id(Long id);
}
