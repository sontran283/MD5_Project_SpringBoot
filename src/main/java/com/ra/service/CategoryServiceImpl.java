package com.ra.service;

import com.ra.exception.CategoryException;
import com.ra.exception.CustomException;
import com.ra.exception.ProductException;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.model.entity.Category;
import com.ra.repository.CategoryRepository;
import com.ra.repository.ProductRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Category> getAll(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(category -> new Category(category.getId(), category.getCategoryName(), category.getStatus()));
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDTO saveOrUpdate(CategoryRequestDTO categoryDTO) throws CategoryException {
        Category category;
        // check da ton tai
        if (categoryRepository.existsByCategoryName(categoryDTO.getCategoryName())) {
            throw new CategoryException("categoryName already exists!");
        }

        // Kiểm tra trường hợp trống trường dữ liệu
        if (StringUtils.isBlank(categoryDTO.getCategoryName())) {
            throw new CategoryException("CategoryName is required");
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
    public Page<CategoryResponseDTO> searchByName(Pageable pageable, String name) throws CategoryException{
        if (StringUtils.isBlank(name)) {
            throw new CategoryException("Category name cannot be blank");
        }
        Page<Category> categoryPage = categoryRepository.findAllByCategoryNameContainingIgnoreCase(pageable, name);
        if (categoryPage.isEmpty()) {
            throw new CategoryException("No category found with the given name");
        }
        return categoryPage.map(CategoryResponseDTO::new);
    }

    @Override
    public void changeStatus(Long id) {
        CategoryResponseDTO categoryResponseDTO = findById(id);
        if (categoryResponseDTO != null) {
            categoryRepository.changeStatus(id);
        }
    }

    @Override
    public boolean checkProductsInCategory(Long categoryId) {
        return productRepository.existsByCategoryId(categoryId);
    }
}
