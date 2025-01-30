package com.example.demo.controllers;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.example.demo.models.Customer;
import com.example.demo.models.Role;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtService jwtService, CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ LOGIN ENDPOINT (Uses JSON request instead of query params)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
        }

        Optional<Customer> customerOpt = customerRepository.findByEmail(email);

        if (customerOpt.isEmpty() || !passwordEncoder.matches(password, customerOpt.get().getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid email or password"));
        }

        Customer customer = customerOpt.get();
        String token = jwtService.generateToken(email, customer.getRole());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "message", "Login successful",
                "role", customer.getRole().name()
        ));
    }

    // ✅ SIGNUP ENDPOINT (User Registration with Default Role)
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody Customer customer) {
        if (customer.getEmail() == null || customer.getPassword() == null || customer.getName() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email, name, and password are required"));
        }

        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is already in use"));
        }

        // ✅ Set default role to USER if role is not provided
        if (customer.getRole() == null) {
            customer.setRole(Role.USER);
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);

        // ✅ Generate token including role
        String token = jwtService.generateToken(customer.getEmail(), customer.getRole());

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully with role: " + customer.getRole().name());
        response.put("token", token);
        response.put("role", customer.getRole().name());

        return ResponseEntity.ok(response);
    }
}
