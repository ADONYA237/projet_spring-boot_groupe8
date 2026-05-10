package com.ecommerce.core.service;

import com.ecommerce.core.model.*;
import com.ecommerce.core.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommonServices {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private TicketRepository ticketRepository;

    // User Methods
    public User createUser(User user) { return userRepository.save(user); }
    public List<User> getAllUsers() { return userRepository.findAll(); }

    // Category Methods
    public Category createCategory(Category category) { return categoryRepository.save(category); }
    public List<Category> getAllCategories() { return categoryRepository.findAll(); }

    // Ticket Methods
    public Ticket createTicket(Ticket ticket) {
        ticket.setStatus("OPEN");
        ticket.setCreatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }
    public List<Ticket> getAllTickets() { return ticketRepository.findAll(); }
}
