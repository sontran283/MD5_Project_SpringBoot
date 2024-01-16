package com.ra.service;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.OrderRequestDTO;
import com.ra.model.dto.response.OrderResponseDTO;
import com.ra.model.entity.Cart_item;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface OrdersService {
    List<OrderResponseDTO> findAll();

    Page<OrderResponseDTO> getAll(Pageable pageable);

    void delete(Long id);

    OrderResponseDTO findById(Long id);

    Orders findOrdersById(Long id);

    OrderResponseDTO saveOrUpdate(OrderRequestDTO ordersDTO) throws CustomException;

    Page<OrderResponseDTO> searchOrdersById(Pageable pageable, Integer id);

    void checkout(User user);
}
