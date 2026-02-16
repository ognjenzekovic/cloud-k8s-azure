package com.cloud.invoice.service;

import com.cloud.invoice.dto.OrderDto;
import com.cloud.invoice.dto.OrderItemDto;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Service
public class InvoiceGeneratorServiceImpl implements InvoiceGeneratorService {
    @Override
    public byte[] generateInvoice(OrderDto order) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("FAKTURA")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Broj porudzbine: #" + order.getId())
                    .setFontSize(12));
            document.add(new Paragraph("Narucilac: " + order.getCustomerName())
                    .setFontSize(12));
            document.add(new Paragraph("ID narucioca: " + order.getCustomerId())
                    .setFontSize(12));

            document.add(new Paragraph("\n"));

            Table table = new Table(UnitValue.createPercentArray(new float[]{4, 1, 2, 2}))
                    .useAllAvailableWidth();

            table.addHeaderCell(new Cell().add(new Paragraph("Proizvod").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Kolicina").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Cena").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Ukupno").setBold()));

            for (OrderItemDto item : order.getItems()) {
                BigDecimal itemTotal = item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));

                table.addCell(new Cell().add(new Paragraph(item.getProductName())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity()))));
                table.addCell(new Cell().add(new Paragraph(item.getUnitPrice() + " RSD")));
                table.addCell(new Cell().add(new Paragraph(itemTotal + " RSD")));
            }

            document.add(table);

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("UKUPNO: " + order.getTotalPrice() + " RSD")
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
