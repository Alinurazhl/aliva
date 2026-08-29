package com.aliva.aliva.controller;

import com.aliva.aliva.entity.Category;
import com.aliva.aliva.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String categories(Model model) {

        model.addAttribute(
                "categories",
                categoryRepository.findAll()
        );

        return "categories";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {

        model.addAttribute("category", new Category());

        return "category-form";
    }

    @PostMapping
    public String createCategory(
            @ModelAttribute Category category
    ) {

        categoryRepository.save(category);

        return "redirect:/categories";
    }
}