package com.aliva.aliva.controller;

import com.aliva.aliva.dto.OrderDto;
import com.aliva.aliva.entity.CartItem;
import com.aliva.aliva.entity.User;
import com.aliva.aliva.repository.CartItemRepository;
import com.aliva.aliva.repository.UserRepository;
import com.aliva.aliva.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public OrderController(
            OrderService orderService,
            CartItemRepository cartItemRepository,
            UserRepository userRepository
    ) {
        this.orderService = orderService;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }


    // Страница оформления заказа
    @GetMapping("/checkout")
    public String checkout(
            Authentication authentication,
            Model model
    ) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Пользователь не найден")
                );

        List<CartItem> cartItems =
                cartItemRepository.findByUser(user);

        double totalPrice = cartItems.stream()
                .mapToDouble(item ->
                        item.getProduct().getPrice()
                                * item.getQuantity()
                )
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orderDto", new OrderDto());

        return "checkout";
    }


    // Создание заказа
    @PostMapping("/create")
    public String createOrder(
            @ModelAttribute OrderDto orderDto,
            Authentication authentication
    ) {

        orderService.createOrder(
                authentication.getName(),
                orderDto.getPhone(),
                orderDto.getAddress()
        );

        return "redirect:/orders";
    }


    // Мои заказы
    @GetMapping
    public String orders(
            Authentication authentication,
            Model model
    ) {

        model.addAttribute(
                "orders",
                orderService.getUserOrders(
                        authentication.getName()
                )
        );

        return "orders";
    }
}