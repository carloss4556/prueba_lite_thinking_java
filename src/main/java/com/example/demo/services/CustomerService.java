package com.example.demo.services;

import com.example.demo.models.Customer;
import com.example.demo.models.Role;
import com.example.demo.repositories.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // ✅ Get a customer by ID with proper exception handling
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
    }

    // ✅ Create a new customer (Encrypt password & set default role)
    public Customer createCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + customer.getEmail());
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword())); // Encrypt password
        if (customer.getRole() == null) {
            customer.setRole(Role.USER); // Default role
        }
        return customerRepository.save(customer);
    }

    // ✅ Update an existing customer (Only update non-null values)
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        return customerRepository.findById(id).map(existingCustomer -> {
            if (updatedCustomer.getName() != null && !updatedCustomer.getName().isEmpty()) {
                existingCustomer.setName(updatedCustomer.getName());
            }
            if (updatedCustomer.getEmail() != null && !updatedCustomer.getEmail().isEmpty()) {
                existingCustomer.setEmail(updatedCustomer.getEmail());
            }

            // Only update password if a new one is provided
            if (updatedCustomer.getPassword() != null && !updatedCustomer.getPassword().isEmpty()) {
                existingCustomer.setPassword(passwordEncoder.encode(updatedCustomer.getPassword()));
            }

            // Only update role if it's provided
            if (updatedCustomer.getRole() != null) {
                existingCustomer.setRole(updatedCustomer.getRole());
            }

            return customerRepository.save(existingCustomer);
        }).orElseThrow(() -> new RuntimeException("Customer with ID " + id + " not found"));
    }

    // ✅ Check if a customer exists by ID
    public boolean customerExists(Long id) {
        return customerRepository.existsById(id);
    }

    // ✅ Delete a customer (Only if they exist)
    public void deleteCustomer(Long id) {
        if (!customerExists(id)) {
            throw new RuntimeException("Customer with ID " + id + " does not exist");
        }
        customerRepository.deleteById(id);
    }
}
