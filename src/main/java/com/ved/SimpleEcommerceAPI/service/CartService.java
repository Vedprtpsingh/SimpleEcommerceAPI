package com.ved.SimpleEcommerceAPI.service;

import com.ved.SimpleEcommerceAPI.model.*;
import com.ved.SimpleEcommerceAPI.repository.CartItemRepository;
import com.ved.SimpleEcommerceAPI.repository.CartRepository;
import com.ved.SimpleEcommerceAPI.repository.ProductRepository;
import com.ved.SimpleEcommerceAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<Cart> getCartByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return cartRepository.findByUser(user.get());
    }

    public Cart addItemToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            return newCart;
        });

        List<CartItem> items = cart.getItems();
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
            items.add(newItem);
        }

        cart.setItems(items);
        return cartRepository.save(cart);
    }

    public Cart updateItemQuantity(Long userId, Long productId, int quantity) {
        Cart cart = getCartByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<CartItem> items = cart.getItems();

        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
                break;
            }
        }

        cart.setItems(items);
        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<CartItem> items = cart.getItems();

        items.removeIf(item -> {
            if (item.getProduct().getId().equals(productId)) {
                cartItemRepository.delete(item);
                return true;
            }
            return false;
        });

        cart.setItems(items);
        return cartRepository.save(cart);
    }


}
