package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Category;
import com.ra.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*") // dung de truy cap moi duong dan ca noi bo, lan ngoai bo
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    // sort-pagination
    @GetMapping("/category/sort-pagination")
    public ResponseEntity<Page<Category>> categoryIndex(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "3") int size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<Category> categoryPage = categoryService.getAll(pageable);
        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    // search
    @GetMapping("/category/search")
    public ResponseEntity<Page<CategoryResponseDTO>> categorySearch(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "3") int size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @RequestParam(name = "search") String search) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<CategoryResponseDTO> categoryPage = categoryService.searchByName(pageable, search);
        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    // add
    @PostMapping("/category")
    public ResponseEntity<CategoryResponseDTO> addCategory(@RequestBody CategoryRequestDTO categoryDTO, Long id) throws CustomException {
        CategoryResponseDTO newCategory = categoryService.saveOrUpdate(categoryDTO);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    // edit
    @GetMapping("/category/{id}")
    public ResponseEntity<?> editCategory(@PathVariable("id") Long id) {
        CategoryResponseDTO category = categoryService.findById(id);
        if (category != null) {
            return new ResponseEntity<>(category, HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    // update
    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryRequestDTO categoryDTO) throws CustomException {
        CategoryResponseDTO category = categoryService.findById(id);
        categoryDTO.setId(category.getId());
        CategoryResponseDTO updatedCategory = categoryService.saveOrUpdate(categoryDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    // change status
    @PatchMapping("/category/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) {
        categoryService.changeStatus(id);
        CategoryResponseDTO categoryResponseDTO = categoryService.findById(id);
        return new ResponseEntity<>(categoryResponseDTO, HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        CategoryResponseDTO category = categoryService.findById(id);
        if (category != null) {
            categoryService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}