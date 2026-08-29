package com.aliva.aliva.controller;

import com.aliva.aliva.entity.Product;
import com.aliva.aliva.repository.CategoryRepository;
import com.aliva.aliva.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    public ProductController(
            ProductService productService,
            CategoryRepository categoryRepository
    ) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String products(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Model model
    ) {

        Pageable pageable = PageRequest.of(page, 8);

        Page<Product> products =
                productService.searchProducts(
                        name,
                        categoryId,
                        color,
                        size,
                        minPrice,
                        maxPrice,
                        pageable
                );

        model.addAttribute("products", products);

        model.addAttribute(
                "categories",
                categoryRepository.findAll()
        );

        model.addAttribute("searchName", name);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedColor", color);
        model.addAttribute("selectedSize", size);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "products";
    }

    @GetMapping("/{id}")
    public String productDetails(
            @PathVariable Long id,
            Model model
    ) {

        Product product = productService.getProductById(id);

        model.addAttribute("product", product);

        return "product-details";
    }
}