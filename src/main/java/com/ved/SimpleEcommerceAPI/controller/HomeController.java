package com.ved.SimpleEcommerceAPI.controller;

import com.ved.SimpleEcommerceAPI.model.Cart;
import com.ved.SimpleEcommerceAPI.model.Product;
import com.ved.SimpleEcommerceAPI.service.CartService;
import com.ved.SimpleEcommerceAPI.service.ProductService;
import com.ved.SimpleEcommerceAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        List<Product> products = productService.getProducts(0, 20).getContent();
        model.addAttribute("products", products);

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<Long> userIdOpt = userService.findByEmail(email).map(user -> user.getId());
            if (userIdOpt.isPresent()) {
                Optional<Cart> cartOpt = cartService.getCartByUserId(userIdOpt.get());
                model.addAttribute("cart", cartOpt.orElse(new Cart()));
            } else {
                model.addAttribute("cart", new Cart());
            }
        } else {
            model.addAttribute("cart", new Cart());
        }

        return "shop";
    }
}
