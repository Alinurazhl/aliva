package com.aliva.aliva.repository;

import com.aliva.aliva.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM Product p
        WHERE (:name IS NULL OR :name = '' 
               OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:categoryId IS NULL OR p.category.id = :categoryId)
        AND (:color IS NULL OR :color = ''
             OR LOWER(p.color) LIKE LOWER(CONCAT('%', :color, '%')))
        AND (:size IS NULL OR :size = ''
             OR LOWER(p.size) LIKE LOWER(CONCAT('%', :size, '%')))
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        """)
    Page<Product> filterProducts(
            @Param("name") String name,
            @Param("categoryId") Long categoryId,
            @Param("color") String color,
            @Param("size") String size,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );
}