package com.ra.service;

import com.ra.exception.CustomException;
import com.ra.model.dto.OrderDetailDTO;
import com.ra.model.dto.request.OrderRequestDTO;
import com.ra.model.dto.response.OrderResponseDTO;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import com.ra.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public List<OrderResponseDTO> findAll() {
        List<OrderResponseDTO> ordersDTOList = new ArrayList<>();
        List<Orders> ordersList = ordersRepository.findAll();
        for (Orders or : ordersList) {
            ordersDTOList.add(mapToOrderResponseDTO(or));
        }
        return ordersDTOList;
    }

    @Override
    public void delete(Long id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public OrderResponseDTO findById(Long id) {
        Optional<Orders> ordersOptional = ordersRepository.findById(id);
        return ordersOptional.map(this::mapToOrderResponseDTO).orElse(null);
    }

    @Override
    public Orders findOrdersById(Long id) {
        return ordersRepository.findById(id).orElse(null);
    }

    @Override
    public OrderResponseDTO saveOrUpdate(OrderRequestDTO ordersDTO) throws CustomException {
        Orders orders = new Orders();
        orders.setAddress(ordersDTO.getAddress());
        orders.setNote(ordersDTO.getNote());
        orders.setPhone(ordersDTO.getPhone());
        orders.setTotal(ordersDTO.getTotal());
        orders.setStatus(1);

//        User user = userService.findById(ordersDTO.getUserId());
//        orders.setUser(user);
        orders = ordersRepository.save(orders);
        return mapToOrderResponseDTO(orders);
    }

    @Override
    public Page<OrderResponseDTO> searchOrdersById(Pageable pageable, Integer id) {
        Page<Orders> ordersPage = ordersRepository.findOrdersById(pageable, id);
        return ordersPage.map(this::mapToOrderResponseDTO);
    }

    @Override
    public Page<OrderResponseDTO> getAll(Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAll(pageable);
        return ordersPage.map(this::mapToOrderResponseDTO);
    }

    private OrderResponseDTO mapToOrderResponseDTO(Orders orders) {
        Set<OrderDetailDTO> orderDetailDTOs = orders
                .getOrderDetails()
                .stream()
                .map(orderDetail -> OrderDetailDTO
                        .builder()
                        .build())
                .collect(Collectors.toSet());
        return OrderResponseDTO.builder()
                .id(orders.getId())
                .address(orders.getAddress())
                .phone(orders.getPhone())
                .note(orders.getNote())
                .total(orders.getTotal())
                .userId(orders.getUser().getId())
                .orderDetails(orderDetailDTOs)
                .status(orders.getStatus())
                .build();
    }
}
