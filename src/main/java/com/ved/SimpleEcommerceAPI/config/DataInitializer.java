package com.ved.SimpleEcommerceAPI.config;

import com.ved.SimpleEcommerceAPI.model.Role;
import com.ved.SimpleEcommerceAPI.model.User;
import com.ved.SimpleEcommerceAPI.repository.RoleRepository;
import com.ved.SimpleEcommerceAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Create roles if they don't exist
        createRoleIfNotFound("ADMIN");
        createRoleIfNotFound("CUSTOMER");

        // Create admin user if it doesn't exist
        if (userService.findByEmail("admin@example.com").isEmpty()) {
            User adminUser = new User();
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword("admin123"); // In production, use a secure password
            User savedAdmin = userService.saveUser(adminUser, Set.of("ADMIN"));
            System.out.println("Admin user created with email: admin@example.com and password: admin123");
            System.out.println("Admin user ID: " + savedAdmin.getId());
        } else {
            System.out.println("Admin user already exists");
        }
        
        // Create a test customer user
        if (userService.findByEmail("customer@example.com").isEmpty()) {
            User customerUser = new User();
            customerUser.setEmail("customer@example.com");
            customerUser.setPassword("customer123");
            User savedCustomer = userService.saveUser(customerUser, Set.of("CUSTOMER"));
            System.out.println("Test customer created with email: customer@example.com and password: customer123");
            System.out.println("Customer user ID: " + savedCustomer.getId());
        }
    }

    private void createRoleIfNotFound(String name) {
        if (roleRepository.findById(name).isEmpty()) {
            Role role = new Role(name);
            roleRepository.save(role);
        }
    }
}