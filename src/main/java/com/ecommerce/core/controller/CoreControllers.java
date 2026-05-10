package com.ecommerce.core.controller;

import com.ecommerce.core.model.*;
import com.ecommerce.core.service.CommonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CoreControllers {

    @Autowired
    private CommonServices commonServices;

    // User Endpoints
    @PostMapping("/users")
    public User createUser(@jakarta.validation.Valid @RequestBody User user) { return commonServices.createUser(user); }
    
    @GetMapping("/users")
    public List<User> getUsers() { return commonServices.getAllUsers(); }

    // Category Endpoints
    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) { return commonServices.createCategory(category); }
    
    @GetMapping("/categories")
    public List<Category> getCategories() { return commonServices.getAllCategories(); }

    // Ticket Endpoints
    @PostMapping("/tickets")
    public Ticket createTicket(@RequestBody Ticket ticket) { return commonServices.createTicket(ticket); }
    
    @GetMapping("/tickets")
    public List<Ticket> getTickets() { return commonServices.getAllTickets(); }
}
