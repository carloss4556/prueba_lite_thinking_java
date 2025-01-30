package com.example.demo.services;

import com.example.demo.models.Inventory;
import com.example.demo.repositories.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    public List<Inventory> getInventoryByCompanyId(Long companyId) {
        return inventoryRepository.findByCompanyId(companyId);
    }

    public Inventory updateInventory(Long productId, int quantity) {
        return inventoryRepository.findByProductId(productId).map(inventory -> {
            inventory.setQuantity(quantity);
            return inventoryRepository.save(inventory);
        }).orElseThrow(() -> new RuntimeException("Inventory record not found"));
    }
}
