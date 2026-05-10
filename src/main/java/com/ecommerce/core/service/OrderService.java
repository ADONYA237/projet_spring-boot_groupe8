package com.ecommerce.core.service;

import com.ecommerce.core.model.*;
import com.ecommerce.core.repository.OrderRepository;
import com.ecommerce.core.repository.ProductRepository;
import com.ecommerce.core.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            
            // Update stock
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        // Apply promotion
        if (promoCode != null) {
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

    public Order processPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Simulating payment processing
        order.setStatus("PAID");
        return orderRepository.save(order);
    }

    public Order updateShippingStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
