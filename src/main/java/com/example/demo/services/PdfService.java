package com.example.demo.services;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfService {

    public byte[] generateInventoryPdf(Long companyId) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // ✅ Title
            document.add(new Paragraph("📊 Inventory Report")
                    .setBold()
                    .setFontSize(16));

            document.add(new Paragraph("Company ID: " + companyId)
                    .setFontSize(12));

            document.add(new Paragraph("\n"));

            // ✅ Table Header
            Table table = new Table(new float[]{3, 3, 3, 3});
            table.addHeaderCell("Product");
            table.addHeaderCell("Quantity");
            table.addHeaderCell("Last Updated");
            table.addHeaderCell("Company ID");

            // ✅ Fake Data (Replace with DB Fetching)
            List<String[]> dummyData = List.of(
                    new String[]{"Laptop", "50", "2025-01-30", companyId.toString()},
                    new String[]{"Mouse", "150", "2025-01-29", companyId.toString()},
                    new String[]{"Keyboard", "80", "2025-01-28", companyId.toString()}
            );

            for (String[] row : dummyData) {
                for (String cell : row) {
                    table.addCell(new Cell().add(new Paragraph(cell)));
                }
            }

            document.add(table);
            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
    }
}
