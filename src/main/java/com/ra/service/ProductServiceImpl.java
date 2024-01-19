package com.ra.service;

import com.ra.exception.CategoryException;
import com.ra.exception.CustomException;
import com.ra.exception.ProductException;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import com.ra.repository.CategoryRepository;
import com.ra.repository.ProductRepository;
import io.micrometer.common.util.StringUtils;
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
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

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
    public ProductResponseDTO saveOrUpdate(ProductRequestDTO productDTO) throws ProductException {
        // check da ton tai
        if (productRepository.existsByName(productDTO.getName())) {
            throw new ProductException("productName already exists!");
        }

        // check trường hợp trống trường dữ liệu
        if (StringUtils.isBlank(productDTO.getName()) || productDTO.getPrice() == null || productDTO.getCategoryId() == null) {
            throw new ProductException("cannot be left blank");
        }

        // check trường hợp có khoảng trắng ở đầu hoặc cuối chuỗi
        if (productDTO.getName().startsWith(" ") || productDTO.getName().endsWith(" ")) {
            throw new ProductException("ProductName cannot have leading or trailing spaces");
        }

        // check trường hợp trống trường file
        MultipartFile file = productDTO.getFile();
        if (file == null || file.isEmpty()) {
            throw new ProductException("Product image is required");
        }

        if (productDTO.getId() == null) {
            Product productNew = new Product();
            productNew.setName(productDTO.getName());
            productNew.setPrice(productDTO.getPrice());

            // Upload file
            if (productDTO.getFile() != null && productDTO.getFile().getSize() > 0) {
                String fileName = uploadService.uploadImage(productDTO.getFile());
                productNew.setImage(fileName);
            }

            // lấy category theo categoryId từ request DTO
            Category category = categoryRepository.findById(productDTO.getCategoryId()).orElse(null);

            // Set category cho product
            productNew.setCategory(category);

            // save
            productRepository.save(productNew);
            return new ProductResponseDTO(productNew);
        } else {
            ProductResponseDTO productResponseDTO = findById(productDTO.getId());
            // check da ton tai
            if (productRepository.existsByName(productDTO.getName())) {
                throw new ProductException("productName already exists!");
            }

            String fileName = null;
            if (productDTO.getFile() != null && !productDTO.getFile().isEmpty()) {
                fileName = uploadService.uploadImage(productDTO.getFile());
            } else {
                fileName = productResponseDTO.getImage();
            }
            Category category = categoryRepository.findById(productDTO.getCategoryId()).orElse(null);
            Product product = productRepository.save(Product.builder()
                    .id(productResponseDTO.getId())
                    .name(productDTO.getName())
                    .price(productDTO.getPrice())
                    .category(category)
                    .status(productDTO.getStatus())
                    .image(fileName)
                    .build());
            return new ProductResponseDTO(product);
        }
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
            throw new CustomException("No product found with the id just entered");
        }
        return product;
    }

    @Override
    public Page<ProductResponseDTO> getAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ProductResponseDTO::new);
    }

    @Override
    public Page<ProductResponseDTO> searchByName(Pageable pageable, String name) throws ProductException {
        if (StringUtils.isBlank(name)) {
            throw new ProductException("Product name cannot be blank");
        }
        Page<Product> productPage = productRepository.searchProductByName(pageable, name);
        if (productPage.isEmpty()) {
            throw new ProductException("No products found with the given name");
        }
        return productPage.map(ProductResponseDTO::new);
    }

    @Override
    public void changeStatus(Long id) {
        ProductResponseDTO productResponseDTO = findById(id);
        productRepository.changeStatus(id);
    }
}