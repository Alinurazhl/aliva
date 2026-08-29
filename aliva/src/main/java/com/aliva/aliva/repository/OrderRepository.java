package com.aliva.aliva.repository;

import com.aliva.aliva.entity.Order;
import com.aliva.aliva.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByCreatedAtDesc(User user);
}