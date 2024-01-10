package com.ra.controller;

import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class ProductController {
    @Autowired
    private ProductService productService;

    // index
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getListProducts() {
        List<ProductResponseDTO> productDTOList = productService.findAll();
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    // add product
    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO newProduct = productService.saveOrUpdate(productRequestDTO);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    // delete product
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        if (productService.findProductById(id) != null) {
            productService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    // edit product
    @GetMapping("/products/{id}")
    public ResponseEntity<?> editProduct(@PathVariable("id") Long id) {
        ProductResponseDTO editedProduct = productService.findById(id);
        if (editedProduct != null) {
            return new ResponseEntity<>(editedProduct, HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    // update product
    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO existingProduct = productService.findById(id);
        if (existingProduct != null) {
            ProductRequestDTO updatedProductRequest = new ProductRequestDTO();
            updatedProductRequest.setName(productRequestDTO.getName());
            updatedProductRequest.setPrice(productRequestDTO.getPrice());
            updatedProductRequest.setFile(productRequestDTO.getFile());
            updatedProductRequest.setCategoryId(productRequestDTO.getCategoryId());

            ProductResponseDTO updatedProduct = productService.saveOrUpdate(updatedProductRequest);

            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    // cập nhật trạng thái sản phẩm
    @PatchMapping("/products/{id}/status")
    public ResponseEntity<?> updateProductStatus(@PathVariable("id") Long id, @RequestParam("status") Boolean status) {
        ProductResponseDTO existingProduct = productService.findById(id);
        if (existingProduct != null) {
            existingProduct.setStatus(status);
            ProductResponseDTO updatedProduct = productService.saveOrUpdate(existingProduct.toProductRequestDTO());
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    // tìm kiếm sản phẩm theo tên với phân trang, sắp xếp
    @GetMapping("/products/search")
    public ResponseEntity<Page<ProductResponseDTO>> searchProducts(
            @RequestParam(name = "search") String search,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order) {
        Pageable pageable = createPageable(page, size, sort, order);
        Page<ProductResponseDTO> productDTOPage = productService.searchByName(pageable, search);
        return new ResponseEntity<>(productDTOPage, HttpStatus.OK);
    }

    // lấy danh sách sản phẩm với phân trang, sắp xếp
    @GetMapping("/products/pagination")
    public ResponseEntity<Page<ProductResponseDTO>> getPaginatedProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order) {
        Pageable pageable = createPageable(page, size, sort, order);
        Page<ProductResponseDTO> productDTOPage = productService.getAll(pageable);
        return new ResponseEntity<>(productDTOPage, HttpStatus.OK);
    }

    private Pageable createPageable(int page, int size, String sort, String order) {
        Sort.Direction sortDirection = order.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, sortDirection, sort);
    }
}
