package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.exception.ProductException;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.service.CategoryService;
import com.ra.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


@RestController
@RequestMapping("/admin")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    // sort-pagination
    @GetMapping("/products/sort-pagination")
    public ResponseEntity<Page<ProductResponseDTO>> productIndex(
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
        Page<ProductResponseDTO> productResponseDTOS = productService.getAll(pageable);
        return new ResponseEntity<>(productResponseDTOS, HttpStatus.OK);
    }

    // search
    @GetMapping("/products/search")
    public ResponseEntity<Page<ProductResponseDTO>> productSearch(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "3") int size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @RequestParam(name = "search") String search) throws ProductException {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<ProductResponseDTO> productResponseDTOS = productService.searchByName(pageable, search);
        return new ResponseEntity<>(productResponseDTOS, HttpStatus.OK);
    }

    // add
    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@ModelAttribute ProductRequestDTO productRequestDTO) throws ProductException {
        // kiểm tra xem danh mục có tồn tại và đang ở trạng thái true không
        Long categoryId = productRequestDTO.getCategoryId();
        CategoryResponseDTO categoryResponseDTO = categoryService.findById(categoryId);

        if (categoryResponseDTO != null && categoryResponseDTO.getStatus()) {
            ProductResponseDTO newProduct = productService.saveOrUpdate(productRequestDTO);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("The category is invalid or does not exist", HttpStatus.BAD_REQUEST);
        }
    }


    // edit
    @GetMapping("/products/{id}")
    public ResponseEntity<?> editProduct(@PathVariable("id") Long id) {
        ProductResponseDTO editedProduct = productService.findById(id);
        if (editedProduct != null) {
            return new ResponseEntity<>(editedProduct, HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }


    // update
    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @ModelAttribute ProductRequestDTO productRequestDTO) throws ProductException {
        ProductResponseDTO updatedProductRequest = productService.findById(id);
        if (updatedProductRequest == null) {
            return new ResponseEntity<>("Could not find the id of the product that needs repair", HttpStatus.BAD_REQUEST);
        }
        productRequestDTO.setId(updatedProductRequest.getId());
        ProductResponseDTO updatedProduct = productService.saveOrUpdate(productRequestDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    // change status
    @PatchMapping("/products/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) {
        ProductResponseDTO productResponseDTO = productService.findById(id);
        if (productResponseDTO == null) {
            return new ResponseEntity<>("Could not find the id of the product that needs repair", HttpStatus.BAD_REQUEST);
        }
        productService.changeStatus(id);
        return new ResponseEntity<>("Status change successful", HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) throws CustomException {
        if (productService.findProductById(id) != null) {
            productService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}