package com.ra.service;

import com.ra.model.dto.request.OrderRequestDTO;
import com.ra.model.dto.response.OrderResponseDTO;
import com.ra.model.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdersService {
    List<OrderResponseDTO> findAll();

    void delete(Long id);

    OrderResponseDTO findById(Long id);

    Orders findOrdersById(Long id);

    OrderResponseDTO saveOrUpdate(OrderRequestDTO ordersDTO);

    Page<OrderResponseDTO> searchOrdersById(Pageable pageable, Integer id);

    Page<OrderResponseDTO> getAll(Pageable pageable);
}
