package com.aliva.aliva.controller;

import com.aliva.aliva.dto.ProductDto;
import com.aliva.aliva.entity.Order;
import com.aliva.aliva.entity.Product;
import com.aliva.aliva.enums.OrderStatus;
import com.aliva.aliva.service.OrderService;
import com.aliva.aliva.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.aliva.aliva.entity.Category;
import com.aliva.aliva.repository.CategoryRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CategoryRepository categoryRepository;

    public AdminController(
            ProductService productService,
            OrderService orderService,
            CategoryRepository categoryRepository
    ) {
        this.productService = productService;
        this.orderService = orderService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public String categories(Model model) {

        model.addAttribute(
                "categories",
                categoryRepository.findAll()
        );

        return "admin/categories";
    }

    @PostMapping("/categories")
    public String createCategory(
            @RequestParam String name
    ) {

        Category category = new Category(name);

        categoryRepository.save(category);

        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(
            @PathVariable Long id
    ) {

        categoryRepository.deleteById(id);

        return "redirect:/admin/categories";
    }
    @GetMapping
    public String admin() {
        return "admin/admin";
    }



    // ================= PRODUCTS =================

    @GetMapping("/products")
    public String products(
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {

        Page<Product> products =
                productService.getAllProducts(
                        PageRequest.of(page, 10)
                );

        model.addAttribute("products", products);

        return "admin/products";
    }
    @GetMapping("/products/new")
    public String newProduct(Model model) {

        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("categories", categoryRepository.findAll());

        return "admin/product-form";
    }

    @PostMapping("/products")
    public String createProduct(
            @ModelAttribute("productDto") ProductDto dto
    ) {

        productService.createProduct(dto);

        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(
            @PathVariable Long id,
            Model model
    ) {

        Product product =
                productService.getProductById(id);

        ProductDto dto = new ProductDto();

        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setColor(product.getColor());
        dto.setSize(product.getSize());
        dto.setStock(product.getStock());
        dto.setImage(product.getImage());

        if (product.getCategory() != null) {
            dto.setCategoryId(
                    product.getCategory().getId()
            );
        }

        model.addAttribute("productDto", dto);
        model.addAttribute("productId", id);


        model.addAttribute(
                "categories",
                categoryRepository.findAll()
        );

        return "admin/product-form";
    }

    @PostMapping("/products/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute("productDto") ProductDto dto
    ) {

        productService.updateProduct(id, dto);

        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id
    ) {

        productService.deleteProduct(id);

        return "redirect:/admin/products";
    }

    // ================= ORDERS =================

    @GetMapping("/orders")
    public String orders(Model model) {

        model.addAttribute(
                "orders",
                orderService.getAllOrders()
        );

        model.addAttribute(
                "statuses",
                OrderStatus.values()
        );

        return "admin/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) {

        orderService.updateStatus(id, status);

        return "redirect:/admin/orders";
    }
}