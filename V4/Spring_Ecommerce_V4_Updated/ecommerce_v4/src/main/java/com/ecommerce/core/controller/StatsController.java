package com.ecommerce.core.controller;

import com.ecommerce.core.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class StatsController {

    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private TicketRepository ticketRepository;
    @Autowired private PromotionRepository promotionRepository;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", productRepository.count());
        stats.put("totalOrders", orderRepository.count());
        stats.put("totalUsers", userRepository.count());
        stats.put("totalCategories", categoryRepository.count());
        stats.put("totalTickets", ticketRepository.count());
        stats.put("openTickets", ticketRepository.findAll().stream()
                .filter(t -> "OPEN".equals(t.getStatus())).count());
        stats.put("totalPromotions", promotionRepository.count());

        double totalRevenue = orderRepository.findAll().stream()
                .filter(o -> "PAID".equals(o.getStatus()) || "SHIPPED".equals(o.getStatus()) || "DELIVERED".equals(o.getStatus()))
                .mapToDouble(o -> o.getTotalAmount())
                .sum();
        stats.put("totalRevenue", totalRevenue);

        Map<String, Long> ordersByStatus = new HashMap<>();
        orderRepository.findAll().forEach(o -> {
            ordersByStatus.merge(o.getStatus(), 1L, Long::sum);
        });
        stats.put("ordersByStatus", ordersByStatus);

        return stats;
    }

    @GetMapping("/resources")
    public Map<String, Object> getResources() {
        Map<String, Object> resources = new HashMap<>();

        resources.put("endpoints", List.of(
            // Products
            "GET    /api/v1/products",
            "POST   /api/v1/products",
            "GET    /api/v1/products/{id}",
            "PUT    /api/v1/products/{id}",
            "DELETE /api/v1/products/{id}",
            "POST   /api/v1/products/{id}/variants",
            "POST   /api/v1/products/{id}/comments",
            // Categories
            "GET    /api/v1/categories",
            "POST   /api/v1/categories",
            "GET    /api/v1/categories/{id}",
            // Users
            "GET    /api/v1/users",
            "POST   /api/v1/users",
            "GET    /api/v1/users/{id}",
            // Orders
            "GET    /api/v1/orders",
            "POST   /api/v1/orders",
            "GET    /api/v1/orders/{id}",
            "POST   /api/v1/orders/{id}/pay",
            "PATCH  /api/v1/orders/{id}/shipping",
            // Tickets
            "GET    /api/v1/tickets",
            "POST   /api/v1/tickets",
            "GET    /api/v1/tickets/{id}",
            "PATCH  /api/v1/tickets/{id}/close",
            // Promotions
            "GET    /api/v1/promotions",
            "POST   /api/v1/promotions",
            // System
            "GET    /api/v1/stats",
            "GET    /api/v1/resources"
        ));

        resources.put("models", List.of(
            "Product", "ProductVariant", "Comment",
            "Category",
            "User",
            "Order", "OrderItem",
            "Ticket",
            "Promotion"
        ));

        resources.put("version", "4.0.0");

        return resources;
    }
}
