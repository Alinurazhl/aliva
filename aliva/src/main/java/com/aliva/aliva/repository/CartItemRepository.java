package com.aliva.aliva.repository;

import com.aliva.aliva.entity.CartItem;
import com.aliva.aliva.entity.Product;
import com.aliva.aliva.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    List<CartItem> findByUser(User user);
}