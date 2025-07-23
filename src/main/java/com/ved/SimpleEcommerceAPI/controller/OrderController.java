package com.ved.SimpleEcommerceAPI.controller;

import com.ved.SimpleEcommerceAPI.model.Order;
import com.ved.SimpleEcommerceAPI.service.OrderService;
import com.ved.SimpleEcommerceAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public String createOrder(Authentication authentication, Model model) {
        String email = authentication.getName();
        Long userId = userService.findByEmail(email)
                .map(user -> user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Order order = orderService.createOrder(userId);
        model.addAttribute("order", order);
        return "order-confirmation";
    }
    
    @PostMapping("/create-api")
    @ResponseBody
    public ResponseEntity<Order> createOrderApi(Authentication authentication) {
        String email = authentication.getName();
        Long userId = userService.findByEmail(email)
                .map(user -> user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Order order = orderService.createOrder(userId);
        return ResponseEntity.ok(order);
    }
}