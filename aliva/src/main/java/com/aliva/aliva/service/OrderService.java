package com.aliva.aliva.service;

import com.aliva.aliva.entity.CartItem;
import com.aliva.aliva.entity.Order;
import com.aliva.aliva.entity.OrderItem;
import com.aliva.aliva.entity.User;
import com.aliva.aliva.enums.OrderStatus;
import com.aliva.aliva.repository.CartItemRepository;
import com.aliva.aliva.repository.OrderRepository;
import com.aliva.aliva.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    public Order createOrder(
            String username,
            String phone,
            String address
    ) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Пользователь не найден")
                );

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Корзина пуста");
        }

        Order order = new Order();

        order.setUser(user);
        order.setPhone(phone);
        order.setAddress(address);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());

        double total = 0;

        for (CartItem cartItem : cartItems) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());

            order.getItems().add(orderItem);

            total += cartItem.getProduct().getPrice()
                    * cartItem.getQuantity();
        }

        order.setTotalPrice(total);

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    public List<Order> getUserOrders(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Пользователь не найден")
                );

        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Заказ не найден")
                );
    }

    public void updateStatus(Long id, OrderStatus status) {

        Order order = getOrderById(id);

        order.setStatus(status);

        orderRepository.save(order);
    }
}