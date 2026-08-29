package com.aliva.aliva.service;

import com.aliva.aliva.dto.ProductDto;
import com.aliva.aliva.entity.Category;
import com.aliva.aliva.entity.Product;
import com.aliva.aliva.exception.ProductNotFoundException;
import com.aliva.aliva.repository.CategoryRepository;
import com.aliva.aliva.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // Получить товары с пагинацией
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // Получить товар по ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Товар с id " + id + " не найден"
                        )
                );
    }

    // Создать товар
    public Product createProduct(ProductDto dto) {

        Product product = new Product();

        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setColor(dto.getColor());
        product.setSize(dto.getSize());
        product.setStock(dto.getStock());
        product.setImage(dto.getImage());

        if (dto.getCategoryId() != null) {

            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Категория с id " + dto.getCategoryId() + " не найдена"
                            )
                    );

            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    // Обновить товар
    public Product updateProduct(Long id, ProductDto dto) {

        Product product = getProductById(id);

        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setColor(dto.getColor());
        product.setSize(dto.getSize());
        product.setStock(dto.getStock());
        product.setImage(dto.getImage());

        if (dto.getCategoryId() != null) {

            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Категория с id " + dto.getCategoryId() + " не найдена"
                            )
                    );

            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        return productRepository.save(product);
    }

    // Удалить товар
    public void deleteProduct(Long id) {

        Product product = getProductById(id);

        productRepository.delete(product);
    }


    public Page<Product> searchProducts(
            String name,
            Long categoryId,
            String color,
            String size,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    ) {

        return productRepository.filterProducts(
                name,
                categoryId,
                color,
                size,
                minPrice,
                maxPrice,
                pageable
        );
    }
}