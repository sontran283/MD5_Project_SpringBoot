package com.ra.service;

import com.ra.exception.CustomException;
import com.ra.exception.ProductException;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> findAll();

    void delete(Long id);

    ProductResponseDTO saveOrUpdate(ProductRequestDTO product) throws ProductException;

    ProductResponseDTO findById(Long id);

    Product findProductById(Long id) throws CustomException;

    Page<ProductResponseDTO> getAll(Pageable pageable);

    Page<ProductResponseDTO> searchByName(Pageable pageable, String name);

    void changeStatus(Long id);
}