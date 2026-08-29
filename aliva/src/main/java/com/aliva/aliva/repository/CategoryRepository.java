package com.aliva.aliva.repository;

import com.aliva.aliva.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}