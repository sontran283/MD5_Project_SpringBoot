package com.ra.service;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.OrderRequestDTO;
import com.ra.model.dto.response.OrderResponseDTO;
import com.ra.model.entity.*;
import com.ra.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<OrderResponseDTO> findAll() {
        List<Orders> ordersList = ordersRepository.findAll();
        return ordersList.stream().map(OrderResponseDTO::new).toList();
    }

    @Override
    public Page<OrderResponseDTO> getAll(Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAll(pageable);
        return ordersPage.map(OrderResponseDTO::new);
    }


    @Override
    public void delete(Long id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public OrderResponseDTO findById(Long id) {
        Optional<Orders> ordersOptional = ordersRepository.findById(id);
        return ordersOptional.map(OrderResponseDTO::new).orElse(null);
    }

    @Override
    public Orders findOrdersById(Long id) {
        return ordersRepository.findById(id).orElse(null);
    }

    @Override
    public OrderResponseDTO saveOrUpdate(OrderRequestDTO orderRequestDTO) throws CustomException {
        Orders orders = new Orders();
        orders.setId(orderRequestDTO.getId());
        orders.setAddress(orderRequestDTO.getAddress());
        orders.setPhone(orderRequestDTO.getPhone());
        orders.setNote(orderRequestDTO.getNote());
        orders.setTotal(orderRequestDTO.getTotal());
        orders.setStatus(orderRequestDTO.getStatus());
        orders.setOrder_date(orderRequestDTO.getOrder_date());
        orders.setUser(orders.getUser());
        orders.setOrderDetails(orders.getOrderDetails());
        orders = ordersRepository.save(orders);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setStatus(orders.getStatus());
        return orderResponseDTO;
    }


    @Override
    public Page<OrderResponseDTO> searchOrdersById(Pageable pageable, Integer id) {
        Page<Orders> ordersPage = ordersRepository.findOrdersById(pageable, id);
        return ordersPage.map(OrderResponseDTO::new);
    }

    @Transactional
    @Override
    public void checkout(User user) {
        Cart cart = cartRepository.findByUser(user);
        List<Cart_item> cartItemList = cartItemRepository.findAllByCart(cart);
        // tạo đối tượng Order mới
        Orders orders = new Orders();
        orders.setUser(user);
        orders.setOrder_date(LocalDateTime.now());
        ordersRepository.save(orders);

        // tạo danh sách OrderItem từ danh sách Cart_item
        for (Cart_item cartItem : cartItemList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getPrice());
            orderDetail.setOrders(orders);
            orderDetailRepository.save(orderDetail);
            cartItemRepository.deleteByCartItemId(cartItem.getId());
        }

        // cập nhật thông tin đơn hàng từ request
        orders.setAddress(user.getAddress());
        orders.setPhone(user.getPhoneNumber());
        orders.setNote(user.getEmail());

        // tính tổng
        float total = cartItemList.stream().map(cartItem -> cartItem.getPrice() * cartItem.getQuantity()).reduce(Float::sum).orElse(0f);
        orders.setTotal(total);

        // lưu đơn hàng vào cơ sở dữ liệu
        ordersRepository.save(orders);
    }

    @Override
    public void changeStatus(Long id, int status) {
        Orders orders = ordersRepository.findById(id).orElse(null);
        orders.setStatus(status);
        ordersRepository.save(orders);
    }

    @Override
    public List<OrderResponseDTO> getListOrderByStatus(Integer status) {
        List<Orders> ordersList = ordersRepository.findAllByStatus(status);
        return ordersList.stream().map(OrderResponseDTO::new).toList();
    }
}
