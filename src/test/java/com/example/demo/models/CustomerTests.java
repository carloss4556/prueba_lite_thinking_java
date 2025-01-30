package com.example.demo.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTests {

    @Test
    void testCustomerConstructor() {
        Customer customer = new Customer("john@example.com", "John Doe", "securepassword", Role.USER);

        assertNotNull(customer);
        assertEquals("John Doe", customer.getName());
        assertEquals("john@example.com", customer.getEmail());
        assertEquals("securepassword", customer.getPassword());
        assertEquals(Role.USER, customer.getRole()); // ✅ Ensure role is correctly assigned
    }

    @Test
    void testSetAndGetId() {
        Customer customer = new Customer();
        customer.setId(1L);

        assertEquals(1L, customer.getId());
    }

    @Test
    void testSetAndGetEmail() {
        Customer customer = new Customer();
        customer.setEmail("jane@example.com");

        assertEquals("jane@example.com", customer.getEmail());
    }

    @Test
    void testSetAndGetName() {
        Customer customer = new Customer();
        customer.setName("Jane Doe");

        assertEquals("Jane Doe", customer.getName());
    }

    @Test
    void testSetAndGetPassword() {
        Customer customer = new Customer();
        customer.setPassword("hashedpassword123");

        assertEquals("hashedpassword123", customer.getPassword());
    }

    @Test
    void testSetAndGetRole() {
        Customer customer = new Customer();
        customer.setRole(Role.ADMIN);

        assertEquals(Role.ADMIN, customer.getRole());
    }
}
