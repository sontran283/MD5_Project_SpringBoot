package com.ra.service;

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

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private UserService userService;

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
    public OrderResponseDTO saveOrUpdate(OrderRequestDTO ordersDTO) {
        Orders orders = new Orders();
        orders.setAddress(ordersDTO.getAddress());
        orders.setNote(ordersDTO.getNote());
        orders.setPhone(ordersDTO.getPhone());
        orders.setTotal(ordersDTO.getTotal());
        orders.setStatus(1);
        User user = userService.findById(ordersDTO.getUserId());
        orders.setUser(user);
        orders = ordersRepository.save(orders);
        return mapToOrderResponseDTO(orders);
    }

    @Override
    public Page<OrderResponseDTO> searchOrdersById(Pageable pageable, Integer id) {
        Page<Orders> ordersPage=ordersRepository.findOrdersById(pageable, id);
        return ordersPage.map(orders -> new OrderResponseDTO(orders.getId(),orders.getAddress(),orders.getPhone(),orders.getNote(), orders.getTotal(), orders.getStatus(),orders.getUser().getId(),orders.getOrderDetails()));
    }

    @Override
    public Page<OrderResponseDTO> getAll(Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAll(pageable);
        return ordersPage.map(this::mapToOrderResponseDTO);
    }

    private OrderResponseDTO mapToOrderResponseDTO(Orders orders) {
        return new OrderResponseDTO(
                orders.getId(),
                orders.getAddress(),
                orders.getPhone(),
                orders.getNote(),
                orders.getTotal(),
                orders.getUser().getId(),
//                orders.getOrderDetails(),
                orders.getStatus(),
        );
    }
}
