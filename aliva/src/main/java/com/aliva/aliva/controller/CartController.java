package com.aliva.aliva.controller;

import com.aliva.aliva.entity.CartItem;
import com.aliva.aliva.entity.Product;
import com.aliva.aliva.entity.User;
import com.aliva.aliva.repository.CartItemRepository;
import com.aliva.aliva.repository.ProductRepository;
import com.aliva.aliva.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartController(
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Добавить товар в корзину
    @PostMapping("/add/{id}")
    public String addToCart(
            @PathVariable Long id,
            Authentication authentication
    ) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Товар не найден")
                );

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Пользователь не найден")
                );

        CartItem cartItem = cartItemRepository
                .findByUserAndProduct(user, product)
                .orElse(null);

        if (cartItem == null) {

            if (product.getStock() <= 0) {
                return "redirect:/products";
            }

            cartItem = new CartItem(
                    user,
                    product,
                    1
            );

        } else {

            if (cartItem.getQuantity() >= product.getStock()) {
                return "redirect:/cart";
            }

            cartItem.setQuantity(
                    cartItem.getQuantity() + 1
            );
        }

        cartItemRepository.save(cartItem);

        return "redirect:/cart";
    }

    // Показать корзину текущего пользователя
    @GetMapping
    public String cart(
            Authentication authentication,
            Model model
    ) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Пользователь не найден")
                );

        model.addAttribute(
                "cartItems",
                cartItemRepository.findByUser(user)
        );

        return "cart";
    }

    // Увеличить количество товара
    // Увеличить количество
    @PostMapping("/increase/{id}")
    public String increaseQuantity(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Пользователь не найден")
                );

        CartItem cartItem = cartItemRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Товар в корзине не найден")
                );

        // Проверяем владельца корзины
        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Нет доступа");
        }

        Product product = cartItem.getProduct();

        // Проверяем наличие товара
        if (cartItem.getQuantity() >= product.getStock()) {
            return "redirect:/cart";
        }

        // Увеличиваем количество
        cartItem.setQuantity(
                cartItem.getQuantity() + 1
        );

        cartItemRepository.save(cartItem);

        return "redirect:/cart";
    }


    // Уменьшить количество
    @PostMapping("/decrease/{id}")
    public String decreaseQuantity(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Пользователь не найден")
                );

        CartItem cartItem = cartItemRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Товар в корзине не найден")
                );

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Нет доступа");
        }

        if (cartItem.getQuantity() > 1) {

            cartItem.setQuantity(
                    cartItem.getQuantity() - 1
            );

            cartItemRepository.save(cartItem);

        } else {

            cartItemRepository.delete(cartItem);
        }

        return "redirect:/cart";
    }


    // Удалить товар
    @PostMapping("/remove/{id}")
    public String removeFromCart(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Пользователь не найден")
                );

        CartItem cartItem = cartItemRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Товар в корзине не найден")
                );

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Нет доступа");
        }

        cartItemRepository.delete(cartItem);

        return "redirect:/cart";
    }
}