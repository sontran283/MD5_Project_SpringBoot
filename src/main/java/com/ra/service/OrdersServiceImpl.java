package com.ra.service;

import com.ra.exception.CustomException;
import com.ra.model.dto.OrderDetailDTO;
import com.ra.model.dto.request.OrderRequestDTO;
import com.ra.model.dto.response.OrderResponseDTO;
import com.ra.model.entity.*;
import com.ra.repository.*;
import jakarta.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

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
    public Page<OrderResponseDTO> getAll(Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAll(pageable);
        return ordersPage.map(this::mapToOrderResponseDTO);
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

        Optional<User> userOptional = userRepository.findById(ordersDTO.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            orders.setUser(user);
            orders = ordersRepository.save(orders);
            return mapToOrderResponseDTO(orders);
        } else {
            throw new CustomException("User not found with ID: " + ordersDTO.getUserId());
        }
    }


    @Override
    public Page<OrderResponseDTO> searchOrdersById(Pageable pageable, Integer id) {
        Page<Orders> ordersPage = ordersRepository.findOrdersById(pageable, id);
        return ordersPage.map(this::mapToOrderResponseDTO);
    }

    @Override
    public void checkout(User user) {
        Cart cart = cartRepository.findByUser(user);
        List<Cart_item> cartItemList = cartItemRepository.findAllByCart(cart);
        // Tạo đối tượng Order mới
        Orders orders = new Orders();
        orders.setUser(user);
        orders.setOrder_date(LocalDateTime.now());
        ordersRepository.save(orders);

        // Tạo danh sách OrderItem từ danh sách Cart_item
        for (Cart_item cartItem : cartItemList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getPrice());
            orderDetail.setOrders(orders);
            orderDetailRepository.save(orderDetail);
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

    private OrderResponseDTO mapToOrderResponseDTO(Orders orders) {
        Set<OrderDetailDTO> orderDetailDTOs = (orders.getOrderDetails() != null)
                ? orders.getOrderDetails().stream()
                .map(orderDetail -> OrderDetailDTO.builder()
                        .productId(orderDetail.getId())
                        .quantity(orderDetail.getQuantity())
                        .build())
                .collect(Collectors.toSet())
                : Collections.emptySet();

        return OrderResponseDTO.builder()
                .id(orders.getId())
                .address(orders.getAddress())
                .phone(orders.getPhone())
                .note(orders.getNote())
                .total(orders.getTotal())
                .userId(orders.getUser().getId())
                .orderDetails(orders.getOrderDetails())
                .status(orders.getStatus())
                .build();
    }
}
