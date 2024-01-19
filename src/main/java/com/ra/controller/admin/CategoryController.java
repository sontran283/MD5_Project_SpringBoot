package com.ra.controller.admin;

import com.ra.exception.CategoryException;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
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

@RestController
@CrossOrigin("*")
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    // sort-pagination
    @GetMapping("/category/sort-pagination")
    public ResponseEntity<?> categoryIndex(
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
    public ResponseEntity<?> categorySearch(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "3") int size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @RequestParam(name = "search") String search) throws CategoryException {
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
    public ResponseEntity<CategoryResponseDTO> addCategory(@RequestBody CategoryRequestDTO categoryDTO, Long id) throws CategoryException {
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
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @ModelAttribute CategoryRequestDTO categoryDTO) throws CategoryException {
        CategoryResponseDTO category = categoryService.findById(id);
        if (category == null) {
            return new ResponseEntity<>("Could not find the id of the category that needs repair", HttpStatus.BAD_REQUEST);
        }
        categoryDTO.setId(category.getId());
        CategoryResponseDTO updatedCategory = categoryService.saveOrUpdate(categoryDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    // change status
    @PatchMapping("/category/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) {
        CategoryResponseDTO categoryResponseDTO = categoryService.findById(id);
        if (categoryResponseDTO == null) {
            return new ResponseEntity<>("Could not find the id of the category that needs repair", HttpStatus.BAD_REQUEST);
        }
        categoryService.changeStatus(id);
        return new ResponseEntity<>("Status change successful", HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        CategoryResponseDTO category = categoryService.findById(id);
        if (category != null) {
            // Kiểm tra xem danh mục có sản phẩm hay không
            boolean checkProductsInCategory = categoryService.checkProductsInCategory(id);
            if (checkProductsInCategory) {
                return new ResponseEntity<>("This category contains products that cannot be deleted", HttpStatus.BAD_REQUEST);
            }
            categoryService.delete(id);
            return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}