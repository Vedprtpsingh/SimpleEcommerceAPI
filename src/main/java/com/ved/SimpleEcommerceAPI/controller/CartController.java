package com.ved.SimpleEcommerceAPI.controller;

import com.ved.SimpleEcommerceAPI.model.Cart;
import com.ved.SimpleEcommerceAPI.service.CartService;
import com.ved.SimpleEcommerceAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        String email = authentication.getName();
        Long userId = userService.findByEmail(email).map(user -> user.getId()).orElse(null);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        return cartService.getCartByUserId(userId)
                .map(cart -> ResponseEntity.ok(cart))
                .orElse(ResponseEntity.ok(new Cart()));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addItemToCart(Authentication authentication,
                                              @RequestParam Long productId,
                                              @RequestParam int quantity) {
        String email = authentication.getName();
        Long userId = userService.findByEmail(email).map(user -> user.getId()).orElse(null);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        Cart cart = cartService.addItemToCart(userId, productId, quantity);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateItemQuantity(Authentication authentication,
                                                   @RequestParam Long productId,
                                                   @RequestParam int quantity) {
        String email = authentication.getName();
        Long userId = userService.findByEmail(email).map(user -> user.getId()).orElse(null);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        Cart cart = cartService.updateItemQuantity(userId, productId, quantity);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeItemFromCart(Authentication authentication,
                                                  @RequestParam Long productId) {
        String email = authentication.getName();
        Long userId = userService.findByEmail(email).map(user -> user.getId()).orElse(null);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        Cart cart = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(cart);
    }
}
