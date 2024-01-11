package com.ra.service;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import com.ra.repository.CategoryRepository;
import com.ra.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UploadService uploadService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryService categoryService;

    @Override
    public List<ProductResponseDTO> findAll() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(ProductResponseDTO::new).toList();
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDTO saveOrUpdate(ProductRequestDTO productDTO) throws CustomException {
        // check da ton tai
        if (categoryRepository.existsByCategoryName(productDTO.getName())) {
            throw new CustomException("productName already exists!");
        }
        if (productDTO.getId() == null) {
            Product productNew = new Product();
            productNew.setName(productDTO.getName());
            productNew.setPrice(productDTO.getPrice());

            // Upload file
            if (productDTO.getFile() != null && !productDTO.getFile().isEmpty()) {
                String fileName = uploadService.uploadImage(productDTO.getFile());
                productNew.setImage(fileName);
            }

            // Lấy category theo categoryId từ request DTO
            Category category = categoryRepository.findById(productDTO.getCategoryId()).orElse(null);

            // Set category cho product
            productNew.setCategory(category);
        } else {
            ProductResponseDTO productResponseDTO = findById(productDTO.getId());
            productDTO.setId(productResponseDTO.getId());
        }

        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElse(null);
        // Lưu sản phẩm
        Product productNew = productRepository.save(Product.builder()
                .price(productDTO.getPrice())
                .category(category)
                .image(String.valueOf(productDTO.getFile()))
                .name(productDTO.getName())
                .id(productDTO.getId())
                .status(productDTO.getStatus())
                .build());
        // Trả về ProductResponseDTO
        return new ProductResponseDTO(productNew);
    }

    @Override
    public ProductResponseDTO findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(ProductResponseDTO::new).orElse(null);
    }

    @Override
    public Product findProductById(Long id) throws CustomException {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new CustomException("khong tim thay product voi id vua nhap");
        }
        return product;
    }

    @Override
    public Page<ProductResponseDTO> getAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> new ProductResponseDTO(product));
    }

    @Override
    public Page<ProductResponseDTO> searchByName(Pageable pageable, String name) {
        Page<Product> productPage = productRepository.findAllBy(pageable, name);
        return productPage.map(product -> new ProductResponseDTO(product));
    }

    @Override
    public void changeStatus(Long id) {
        ProductResponseDTO productResponseDTO = findById(id);
        productRepository.changeStatus(id);
    }
}