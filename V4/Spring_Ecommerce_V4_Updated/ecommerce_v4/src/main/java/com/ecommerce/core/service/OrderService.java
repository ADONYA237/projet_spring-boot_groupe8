package com.ecommerce.core.service;

import com.ecommerce.core.model.*;
import com.ecommerce.core.repository.OrderRepository;
import com.ecommerce.core.repository.ProductRepository;
import com.ecommerce.core.repository.PromotionRepository;
import com.ecommerce.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    public Order createOrder(Long userId, List<OrderItem> items, String promoCode, String address) {
        double total = 0;
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            item.setUnitPrice(product.getPrice());
            total += item.getUnitPrice() * item.getQuantity();

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        if (promoCode != null && !promoCode.isBlank()) {
            Optional<Promotion> promo = promotionRepository.findByCode(promoCode);
            if (promo.isPresent() && promo.get().getIsActive()) {
                total = total * (1 - promo.get().getDiscountPercentage() / 100);
            }
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setItems(items);
        order.setTotalAmount(total);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setShippingAddress(address);

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public Order processPayment(Long orderId) {
        Order order = getOrderById(orderId);

        order.setStatus("PAID");
        Order savedOrder = orderRepository.save(order);

        try {
            User user = userRepository.findById(order.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            byte[] pdf = invoiceService.generateInvoicePdf(savedOrder);
            String htmlBody = buildInvoiceEmailBody(savedOrder);

            emailService.sendInvoiceEmail(
                    user.getEmail(),
                    "Votre Facture - Commande #" + savedOrder.getId(),
                    htmlBody,
                    pdf,
                    "facture_" + savedOrder.getId() + ".pdf"
            );
        } catch (Exception e) {
            System.err.println("Failed to send invoice email: " + e.getMessage());
        }

        return savedOrder;
    }

    public Order updateShippingStatus(Long orderId, String status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    private String buildInvoiceEmailBody(Order order) {
        return "<html><body style='font-family:Arial,sans-serif;'>"
                + "<h2>Merci pour votre achat !</h2>"
                + "<p>Votre commande <strong>#" + order.getId() + "</strong> a bien été payée.</p>"
                + "<p>Montant total : <strong>" + String.format("%.2f", order.getTotalAmount()) + " €</strong></p>"
                + "<p>Veuillez trouver votre facture en pièce jointe.</p>"
                + "<br/><p>L'équipe E-Commerce</p>"
                + "</body></html>";
    }
}
