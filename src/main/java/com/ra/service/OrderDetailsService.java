package com.ra.service;


import com.ra.exception.CustomException;
import com.ra.model.dto.OrderDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderDetailsService {
    List<OrderDetailDTO> findAll();
    void delete(Long id);
    OrderDetailDTO findById(Long id);
    OrderDetailDTO saveOrUpdate(OrderDetailDTO orderDetailsDTO) throws CustomException;
    Page<OrderDetailDTO> getAll(Pageable pageable);
    Page<OrderDetailDTO> searchByOrderDetailsId(Pageable pageable,Integer id);
}
