package com.ved.SimpleEcommerceAPI.controller;

import com.ved.SimpleEcommerceAPI.model.User;
import com.ved.SimpleEcommerceAPI.security.JwtUtil;
import com.ved.SimpleEcommerceAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        try {
            String email = user.get("email");
            String password = user.get("password");
            
            System.out.println("Login attempt with email: " + email);
            
            // Authenticate the user
            org.springframework.security.core.Authentication authentication = 
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            
            System.out.println("Authentication successful for: " + email);
            
            // Generate token with roles
            String token = jwtUtil.generateToken(email, authentication.getAuthorities());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> userMap) {
        try {
            String email = (String) userMap.get("email");
            String password = (String) userMap.get("password");
            Set<String> roles = Set.copyOf((java.util.List<String>) userMap.get("roles"));
            
            System.out.println("Registering user with email: " + email + ", roles: " + roles);
            
            // Check if user already exists
            if (userService.findByEmail(email).isPresent()) {
                System.out.println("User already exists with email: " + email);
                return ResponseEntity.badRequest().body("User already exists with this email");
            }
            
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            
            User savedUser = userService.saveUser(user, roles);
            System.out.println("User registered successfully: " + savedUser.getEmail());
            
            // Return success with minimal user info (don't expose password hash)
            return ResponseEntity.ok(Map.of(
                "id", savedUser.getId(),
                "email", savedUser.getEmail(),
                "roles", savedUser.getRoles()
            ));
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/check-user")
    public ResponseEntity<?> checkUser(@RequestParam String email) {
        boolean exists = userService.findByEmail(email).isPresent();
        return ResponseEntity.ok(Map.of("exists", exists));
    }
    
    @PostMapping("/test-auth")
    public ResponseEntity<?> testAuth(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            
            System.out.println("Test auth for email: " + email);
            
            // Check if user exists
            var userOpt = userService.findByEmail(email);
            if (userOpt.isEmpty()) {
                System.out.println("User not found: " + email);
                return ResponseEntity.status(404).body("User not found");
            }
            
            User user = userOpt.get();
            System.out.println("User found with ID: " + user.getId());
            
            // Try to authenticate
            try {
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Authentication successful"
                ));
            } catch (AuthenticationException e) {
                System.out.println("Authentication failed: " + e.getMessage());
                return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Test auth error: " + e.getMessage());
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
