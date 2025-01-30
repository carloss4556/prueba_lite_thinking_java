package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public HelloController(
            CategoryRepository categoryRepository,
            CompanyRepository companyRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository) {
        this.categoryRepository = categoryRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    // ✅ Basic Hello Endpoint
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    // ✅ Database Summary Endpoint
    @GetMapping("/db-summary")
    public Map<String, Object> getDatabaseSummary() {
        Map<String, Object> summary = new HashMap<>();

        // Get counts of all elements in the DB
        long categoryCount = categoryRepository.count();
        long companyCount = companyRepository.count();
        long customerCount = customerRepository.count();
        long productCount = productRepository.count();
        long orderCount = orderRepository.count();

        summary.put("Total Categories", categoryCount);
        summary.put("Total Companies", companyCount);
        summary.put("Total Customers", customerCount);
        summary.put("Total Products", productCount);
        summary.put("Total Orders", orderCount);

        // Get sample data (first 5 elements from each table)
        summary.put("Sample Categories", categoryRepository.findAll().stream().limit(5).toList());
        summary.put("Sample Companies", companyRepository.findAll().stream().limit(5).toList());
        summary.put("Sample Customers", customerRepository.findAll().stream().limit(5).toList());
        summary.put("Sample Products", productRepository.findAll().stream().limit(5).toList());
        summary.put("Sample Orders", orderRepository.findAll().stream().limit(5).toList());

        return summary;
    }
}