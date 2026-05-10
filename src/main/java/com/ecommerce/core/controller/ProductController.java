package com.ecommerce.core.controller;

import com.ecommerce.core.model.Comment;
import com.ecommerce.core.model.Product;
import com.ecommerce.core.model.ProductVariant;
import com.ecommerce.core.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getAllProducts(keyword, category, minPrice, maxPrice, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public Product create(@Valid @RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/variants")
    public Product addVariant(@PathVariable Long id, @RequestBody ProductVariant variant) {
        Product product = productService.getProductById(id);
        product.getVariants().add(variant);
        return productService.createProduct(product);
    }

    @PostMapping("/{id}/comments")
    public Product addComment(@PathVariable Long id, @RequestBody Comment comment) {
        Product product = productService.getProductById(id);
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(LocalDateTime.now());
        }
        product.getComments().add(comment);
        return productService.createProduct(product);
    }
}
