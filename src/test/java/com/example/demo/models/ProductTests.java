package com.example.demo.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

class ProductTests {

    @Test
    void testProductConstructor() {
        Company company = new Company("123456789", "Tech Corp", "123 Street", "555-1234");
        Category category1 = new Category("Electronics");
        Category category2 = new Category("Computers");
        Set<Category> categories = Set.of(category1, category2);

        Map<String, BigDecimal> prices = Map.of(
            "USD", new BigDecimal("1200.50"),
            "EUR", new BigDecimal("1100.00"),
            "COP", new BigDecimal("4800000.00")
        );

        Product product = new Product("P001", "Laptop", "High-performance laptop", prices, company, categories);

        assertNotNull(product);
        assertEquals("P001", product.getCode());
        assertEquals("Laptop", product.getName());
        assertEquals("High-performance laptop", product.getDescription());
        assertEquals(company, product.getCompany());
        assertEquals(categories, product.getCategories());
        assertEquals(new BigDecimal("1200.50"), product.getPrices().get("USD"));
    }

    @Test
    void testSetAndGetId() {
        Product product = new Product();
        product.setId(1L);

        assertEquals(1L, product.getId());
    }

    @Test
    void testSetAndGetCode() {
        Product product = new Product();
        product.setCode("P002");

        assertEquals("P002", product.getCode());
    }

    @Test
    void testSetAndGetName() {
        Product product = new Product();
        product.setName("Smartphone");

        assertEquals("Smartphone", product.getName());
    }

    @Test
    void testSetAndGetDescription() {
        Product product = new Product();
        product.setDescription("New generation smartphone");

        assertEquals("New generation smartphone", product.getDescription());
    }

    @Test
    void testSetAndGetPrices() {
        Product product = new Product();
        Map<String, BigDecimal> priceMap = Map.of(
            "USD", new BigDecimal("799.99"),
            "EUR", new BigDecimal("750.00")
        );
        product.setPrices(priceMap);

        assertEquals(new BigDecimal("799.99"), product.getPrices().get("USD"));
        assertEquals(new BigDecimal("750.00"), product.getPrices().get("EUR"));
    }

    @Test
    void testSetAndGetCompany() {
        Product product = new Product();
        Company company = new Company("987654321", "Phone Corp", "456 Avenue", "555-5678");
        product.setCompany(company);

        assertEquals(company, product.getCompany());
    }

    @Test
    void testSetAndGetCategories() {
        Product product = new Product();
        Category category1 = new Category("Mobile");
        Category category2 = new Category("Gadgets");
        Set<Category> categories = Set.of(category1, category2);

        product.setCategories(categories);

        assertEquals(2, product.getCategories().size());
    }
}

