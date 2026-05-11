package com.ecommerce.core.service;

import com.ecommerce.core.model.Order;
import com.ecommerce.core.model.OrderItem;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class InvoiceService {

    public byte[] generateInvoicePdf(Order order) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();
        
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("FACTURE - COMMANDE #" + order.getId(), fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Date: " + order.getCreatedAt()));
        document.add(new Paragraph("Adresse de livraison: " + order.getShippingAddress()));
        document.add(new Paragraph("Statut: " + order.getStatus()));
        document.add(new Paragraph("\n"));

        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        document.add(new Paragraph("Détails des articles:", fontHeader));
        
        for (OrderItem item : order.getItems()) {
            document.add(new Paragraph("- Produit ID: " + item.getProductId() + " | Quantité: " + item.getQuantity() + " | Prix unitaire: " + item.getUnitPrice() + " €"));
        }

        document.add(new Paragraph("\n"));
        Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        document.add(new Paragraph("TOTAL: " + order.getTotalAmount() + " €", fontTotal));

        document.close();
        return out.toByteArray();
    }
}
