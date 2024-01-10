package com.ra.service;

import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import com.ra.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UploadService uploadService;

    @Override
    public List<ProductResponseDTO> findAll() {
        List<Product> productList  = productRepository.findAll();
        return productList.stream().map(ProductResponseDTO::new).toList();
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDTO saveOrUpdate(ProductRequestDTO product) {
        Product productNew = new Product();
        productNew.setName(product.getName());
        productNew.setPrice(product.getPrice());

        // upload file
        String fileName = uploadService.uploadImage(product.getFile());
        productNew.setImage(fileName);

        // Lấy category theo categoryId từ request DTO
        Category category = categoryService.findById(product.getCategoryId());

        // Set category cho product
        productNew.setCategory(category);

        // Lưu sản phẩm
        productRepository.save(productNew);

        // Trả về ProductResponseDTO
        return new ProductResponseDTO(productNew);
    }

    @Override
    public ProductResponseDTO findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(ProductResponseDTO::new).orElse(null);
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Page<ProductResponseDTO> getAll(Pageable pageable) {
        Page<Product> productDTOS = productRepository.findAll(pageable);
        return productDTOS.map(product -> new ProductResponseDTO(product.getId(), product.getName(), product.getPrice(), product.getImage(), product.getCategory().getId(),product.getStatus()));
    }

    @Override
    public Page<ProductResponseDTO> searchByName(Pageable pageable, String name) {
        Page<Product> productPage = productRepository.findAllByProductNameContainingIgnoreCase(pageable, name);
        return productPage.map(product -> new ProductResponseDTO(product));
    }

    @Override
    public ProductResponseDTO updateProductStatus(Long id, Boolean status) {
        ProductResponseDTO existingProduct = findById(id);
        if (existingProduct != null) {
            existingProduct.setStatus(status);
            ProductResponseDTO updatedProduct = saveOrUpdate(existingProduct.toProductRequestDTO());
            return updatedProduct;
        }
        return null;
    }
}
