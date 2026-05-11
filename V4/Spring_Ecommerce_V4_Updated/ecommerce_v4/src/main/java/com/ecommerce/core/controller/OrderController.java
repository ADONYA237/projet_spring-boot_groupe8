package com.ecommerce.core.controller;

import com.ecommerce.core.dto.CreateOrderRequest;
import com.ecommerce.core.dto.UpdateShippingRequest;
import com.ecommerce.core.model.Order;
import com.ecommerce.core.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(
                request.getUserId(),
                request.getItems(),
                request.getPromoCode(),
                request.getAddress()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Order> pay(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.processPayment(id));
    }

    @PatchMapping("/{id}/shipping")
    public ResponseEntity<Order> updateShipping(
            @PathVariable Long id,
            @Valid @RequestBody UpdateShippingRequest request) {
        return ResponseEntity.ok(orderService.updateShippingStatus(id, request.getStatus()));
    }
}
