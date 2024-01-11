package com.ra.service;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDTO> findAll();

    void delete(Long id);

    CategoryResponseDTO saveOrUpdate(CategoryRequestDTO category) throws CustomException;

    CategoryResponseDTO findById(Long id) ;

    Page<CategoryResponseDTO> getAll(Pageable pageable);

    Page<CategoryResponseDTO> searchByName(Pageable pageable, String name);

    void changeStatus(Long id);
}
