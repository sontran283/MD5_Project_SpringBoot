package com.ra.service;

import com.ra.exception.CustomException;
import com.ra.model.dto.OrderDetailDTO;
import com.ra.model.entity.OrderDetail;
import com.ra.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ProductService productService;

    @Override
    public List<OrderDetailDTO> findAll() {
        List<OrderDetailDTO> orderDetailsDTOS = new ArrayList<>();
        List<OrderDetail> orderDetailList = orderDetailRepository.findAll();
        for (OrderDetail orderDetail : orderDetailList) {
            OrderDetailDTO dto = new OrderDetailDTO();
            dto.setId(orderDetail.getId());
            dto.setPrice(orderDetail.getPrice());
            dto.setQuantity(orderDetail.getQuantity());
            dto.setOrdersId(orderDetail.getOrders().getId());
            dto.setProductId(orderDetail.getProduct().getId());
            orderDetailsDTOS.add(dto);
        }
        return orderDetailsDTOS;
    }

    @Override
    public void delete(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public OrderDetailDTO findById(Long id) {
        Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findById(id);
        if (orderDetailOptional.isPresent()) {
            OrderDetail orderDetail = orderDetailOptional.get();
            OrderDetailDTO orderDetailsDTO = new OrderDetailDTO();
            orderDetailsDTO.setId(orderDetail.getId());
            orderDetailsDTO.setPrice(orderDetail.getPrice());
            orderDetailsDTO.setQuantity(orderDetail.getQuantity());
            orderDetailsDTO.setOrdersId(orderDetail.getOrders().getId());
            orderDetailsDTO.setProductId(orderDetail.getProduct().getId());
            return orderDetailsDTO;
        }
        return null;
    }

    @Override
    public OrderDetailDTO saveOrUpdate(OrderDetailDTO orderDetailsDTO) throws CustomException {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(orderDetailsDTO.getId());
        orderDetail.setPrice(orderDetailsDTO.getPrice());
        orderDetail.setQuantity(orderDetailsDTO.getQuantity());
        orderDetail.setOrders(ordersService.findOrdersById(orderDetailsDTO.getOrdersId()));
        orderDetail.setProduct(productService.findProductById(orderDetailsDTO.getProductId()));
        orderDetail = orderDetailRepository.save(orderDetail);
        OrderDetailDTO detailsDTO = new OrderDetailDTO();
        detailsDTO.setId(orderDetail.getId());
        detailsDTO.setPrice(orderDetail.getPrice());
        detailsDTO.setQuantity(orderDetail.getQuantity());
        detailsDTO.setOrdersId(orderDetail.getOrders().getId());
        detailsDTO.setProductId(orderDetail.getProduct().getId());
        return detailsDTO;
    }

    @Override
    public Page<OrderDetailDTO> getAll(Pageable pageable) {
        Page<OrderDetail> orderDetailsPage = orderDetailRepository.findAll(pageable);
        return orderDetailsPage.map(orderDetails -> new OrderDetailDTO(orderDetails.getId(), orderDetails.getPrice(), orderDetails.getQuantity(), orderDetails.getProduct().getId(), orderDetails.getOrders().getId()));
    }

    @Override
    public Page<OrderDetailDTO> searchByOrderDetailsId(Pageable pageable, Integer id) {
        Page<OrderDetail> orderDetailsPage = orderDetailRepository.searchOrderDetailsById(pageable, id);
        return orderDetailsPage.map(orderDetails -> new OrderDetailDTO(orderDetails.getId(), orderDetails.getPrice(), orderDetails.getQuantity(), orderDetails.getProduct().getId(), orderDetails.getOrders().getId()));
    }
}
