package com.example.demo.controllers;

import com.example.demo.models.Inventory;
import com.example.demo.services.InventoryService;
import com.example.demo.services.PdfService;

import jakarta.mail.MessagingException;

import com.example.demo.services.EmailService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final PdfService pdfService;
    private final EmailService emailService;

    // ✅ Inject missing services in the constructor
    public InventoryController(InventoryService inventoryService, PdfService pdfService, EmailService emailService) {
        this.inventoryService = inventoryService;
        this.pdfService = pdfService;
        this.emailService = emailService;
    }

    // ✅ Get all inventory items
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    // ✅ Get inventory details for a specific product
    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getInventoryByProductId(@PathVariable Long productId) {
        return inventoryService.getInventoryByProductId(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Get inventory for a specific company
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Inventory>> getInventoryByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(inventoryService.getInventoryByCompanyId(companyId));
    }

    // ✅ Update inventory quantity for a product
    @PutMapping("/{productId}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.updateInventory(productId, quantity));
    }

    // ✅ Generate and Download Inventory PDF
    @GetMapping("/{companyId}/generate/pdf")
    public ResponseEntity<ByteArrayResource> generateInventoryPdf(@PathVariable Long companyId) {
        byte[] pdfBytes = pdfService.generateInventoryPdf(companyId);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    // ✅ Generate and Send Inventory PDF via Email
    @PostMapping("/{companyId}/send/pdf")
    public ResponseEntity<String> sendInventoryPdf(@PathVariable Long companyId, @RequestParam String email) {
        try {
            byte[] pdfBytes = pdfService.generateInventoryPdf(companyId);
            emailService.sendInventoryReport(email, pdfBytes);
            return ResponseEntity.ok("PDF report sent successfully to " + email);
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

}
