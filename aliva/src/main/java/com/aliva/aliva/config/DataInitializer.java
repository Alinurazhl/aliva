package com.aliva.aliva.config;

import com.aliva.aliva.entity.Category;
import com.aliva.aliva.entity.Product;
import com.aliva.aliva.repository.CategoryRepository;
import com.aliva.aliva.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            CategoryRepository categoryRepository,
            ProductRepository productRepository) {

        return args -> {

            if (categoryRepository.count() == 0) {

                Category bags = new Category("Сумки");
                Category shoes = new Category("Обувь");

                categoryRepository.save(bags);
                categoryRepository.save(shoes);

                Product bag = new Product();
                bag.setName("Classic Black Bag");
                bag.setDescription("Элегантная черная сумка ALIVA");
                bag.setPrice(45000.0);
                bag.setImage("https://images.unsplash.com/photo-1584917865442-de89df76afd3");
                bag.setBrand("ALIVA");
                bag.setColor("Black");
                bag.setSize("Medium");
                bag.setStock(10);
                bag.setCategory(bags);

                Product shoe = new Product();
                shoe.setName("Elegant Black Shoes");
                shoe.setDescription("Классические черные туфли");
                shoe.setPrice(55000.0);
                shoe.setImage("https://images.unsplash.com/photo-1543163521-1bf539c55dd2");
                shoe.setBrand("ALIVA");
                shoe.setColor("Black");
                shoe.setSize("38");
                shoe.setStock(7);
                shoe.setCategory(shoes);

                productRepository.save(bag);
                productRepository.save(shoe);
            }
        };
    }
}