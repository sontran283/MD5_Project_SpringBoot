package com.ra.service;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.model.entity.Category;
import com.ra.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponseDTO> findAll() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(CategoryResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDTO saveOrUpdate(CategoryRequestDTO categoryDTO) throws CustomException {
        Category category;
        // check da ton tai
        if (categoryRepository.existsByCategoryName(categoryDTO.getCategoryName())) {
            throw new CustomException("categoryName already exists!");
        }
        if (categoryDTO.getId() == null) {
            category = new Category();
            category.setCategoryName(categoryDTO.getCategoryName());
            category.setStatus(categoryDTO.getStatus());
        } else {
            category = categoryRepository.findById(categoryDTO.getId()).orElse(null);
            category.setCategoryName(categoryDTO.getCategoryName());
        }
        categoryRepository.save(category);
        return new CategoryResponseDTO(category);
    }

    @Override
    public CategoryResponseDTO findById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.map(category -> new CategoryResponseDTO(category.getId(), category.getCategoryName(), category.getStatus()))
                .orElse(null);
    }

    @Override
    public Page<CategoryResponseDTO> getAll(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(category -> new CategoryResponseDTO(category.getId(), category.getCategoryName(), category.getStatus()));
    }

    @Override
    public Page<CategoryResponseDTO> searchByName(Pageable pageable, String name) {
        Page<Category> categoryPage = categoryRepository.findAllByCategoryNameContainingIgnoreCase(pageable, name);
        return categoryPage.map(category -> new CategoryResponseDTO(category.getId(), category.getCategoryName(), category.getStatus()));
    }

    @Override
    public void changeStatus(Long id) {
        CategoryResponseDTO categoryResponseDTO = findById(id);
        if (categoryResponseDTO != null) {
            categoryRepository.changeStatus(id);
        }
    }
}
