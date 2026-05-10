package com.ecommerce.core.controller;

import com.ecommerce.core.model.Order;
import com.ecommerce.core.model.OrderItem;
import com.ecommerce.core.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order createOrder(
            @RequestParam Long userId,
            @RequestBody List<OrderItem> items,
            @RequestParam(required = false) String promoCode,
            @RequestParam String address) {
        return orderService.createOrder(userId, items, promoCode, address);
    }

    @PostMapping("/{id}/pay")
    public Order pay(@PathVariable Long id) {
        return orderService.processPayment(id);
    }

    @PatchMapping("/{id}/shipping")
    public Order updateShipping(@PathVariable Long id, @RequestParam String status) {
        return orderService.updateShippingStatus(id, status);
    }
}
