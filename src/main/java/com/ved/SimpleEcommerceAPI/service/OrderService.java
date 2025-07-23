package com.ved.SimpleEcommerceAPI.service;

import com.ved.SimpleEcommerceAPI.model.*;
import com.ved.SimpleEcommerceAPI.repository.CartItemRepository;
import com.ved.SimpleEcommerceAPI.repository.CartRepository;
import com.ved.SimpleEcommerceAPI.repository.OrderRepository;
import com.ved.SimpleEcommerceAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    public Order createOrder(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> items = cart.getItems();
        if (items.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalAmount = items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = new Order(userId, user, items, LocalDateTime.now(), totalAmount);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart after order creation
        for (CartItem item : items) {
            cartItemRepository.delete(item);
        }
        cart.setItems(List.of());
        cartRepository.save(cart);

        return savedOrder;
    }
}
