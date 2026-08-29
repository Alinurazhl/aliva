package com.aliva.aliva.controller;

import com.aliva.aliva.dto.ProductDto;
import com.aliva.aliva.entity.Product;
import com.aliva.aliva.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    // GET /api/products?page=0&size=10
    // GET /api/products?name=сумка
    // GET /api/products?categoryId=1
    // GET /api/products?color=Black
    // GET /api/products?size=38
    // GET /api/products?minPrice=10000&maxPrice=50000
    @GetMapping
    public Page<Product> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable
    ) {

        return productService.searchProducts(
                name,
                categoryId,
                color,
                size,
                minPrice,
                maxPrice,
                pageable
        );
    }

    // GET /api/products/1
    @GetMapping("/{id}")
    public Product getProduct(
            @PathVariable Long id
    ) {

        return productService.getProductById(id);
    }

    // POST /api/products
    @PostMapping
    public Product createProduct(
            @Valid @RequestBody ProductDto productDto
    ) {

        return productService.createProduct(productDto);
    }

    // PUT /api/products/1
    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto productDto
    ) {

        return productService.updateProduct(
                id,
                productDto
        );
    }

    // DELETE /api/products/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}