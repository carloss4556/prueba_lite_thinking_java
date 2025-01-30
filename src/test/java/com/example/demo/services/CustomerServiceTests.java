package com.example.demo.services;

import com.example.demo.models.Customer;
import com.example.demo.models.Role;
import com.example.demo.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private CustomerService customerService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        customerService = new CustomerService(customerRepository, passwordEncoder);
    }

    // ✅ Test fetching all customers
    @Test
    void testGetAllCustomers() {
        List<Customer> customers = Arrays.asList(new Customer("john@example.com", "John Doe", "hashedpassword", Role.USER));
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    // ✅ Test fetching a customer by ID
    @Test
    void testGetCustomerById() {
        Customer customer = new Customer("john@example.com", "John Doe", "hashedpassword", Role.USER);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1L);
        assertEquals("John Doe", result.getName());
    }

    // ✅ Test creating a customer with default role
    @Test
    void testCreateCustomer() {
        Customer customer = new Customer("john@example.com", "John Doe", "password123", null);
        when(passwordEncoder.encode("password123")).thenReturn("hashedpassword");
        when(customerRepository.save(any(Customer.class))).thenAnswer(i -> i.getArgument(0));

        Customer savedCustomer = customerService.createCustomer(customer);
        assertNotNull(savedCustomer);
        assertEquals("John Doe", savedCustomer.getName());
        assertEquals("hashedpassword", savedCustomer.getPassword());
        assertEquals(Role.USER, savedCustomer.getRole()); // Default role should be USER
    }

    // ✅ Test creating an ADMIN customer
    @Test
    void testCreateAdminCustomer() {
        Customer admin = new Customer("admin@example.com", "Admin User", "adminpass", Role.ADMIN);
        when(passwordEncoder.encode("adminpass")).thenReturn("hashedadminpass");
        when(customerRepository.save(any(Customer.class))).thenAnswer(i -> i.getArgument(0));

        Customer savedAdmin = customerService.createCustomer(admin);
        assertNotNull(savedAdmin);
        assertEquals("Admin User", savedAdmin.getName());
        assertEquals("hashedadminpass", savedAdmin.getPassword());
        assertEquals(Role.ADMIN, savedAdmin.getRole());
    }

    // ✅ Test deleting a customer
    @Test
    void testDeleteCustomer() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(1L);

        assertDoesNotThrow(() -> customerService.deleteCustomer(1L));
    }

    // ❌ Should throw an error when trying to delete a non-existing customer
    @Test
    void testDeleteNonExistingCustomer() {
        when(customerRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> customerService.deleteCustomer(1L));
        assertEquals("Customer with ID 1 does not exist", exception.getMessage());
    }
}
