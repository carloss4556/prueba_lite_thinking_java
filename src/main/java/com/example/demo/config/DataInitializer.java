package com.example.demo.config;

import com.example.demo.models.Customer;
import com.example.demo.models.Role;
import com.example.demo.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (customerRepository.findByEmail("admin@example.com").isEmpty()) {
                Customer admin = new Customer();
                admin.setEmail("admin@example.com");
                admin.setName("Admin User");
                admin.setPassword(passwordEncoder.encode("admin123")); // Secure password
                admin.setRole(Role.ADMIN);
                customerRepository.save(admin);
                System.out.println("✅ Admin user created!");
            }

            if (customerRepository.findByEmail("user@example.com").isEmpty()) {
                Customer user = new Customer();
                user.setEmail("user@example.com");
                user.setName("Regular User");
                user.setPassword(passwordEncoder.encode("user123")); // Secure password
                user.setRole(Role.USER);
                customerRepository.save(user);
                System.out.println("✅ Regular user created!");
            }
        };
    }
}

