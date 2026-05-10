package com.ecommerce.core.service;

import com.ecommerce.core.model.Product;
import com.ecommerce.core.model.Category;
import com.ecommerce.core.repository.ProductRepository;
import com.ecommerce.core.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Join;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts(String keyword, String categoryName, Double minPrice, Double maxPrice, int page, int size) {
        Specification<Product> spec = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.or(
                    cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%")
                )
            );
        }
        
        if (categoryName != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Product, Category> categoryJoin = root.join("categories");
                return cb.equal(categoryJoin.get("name"), categoryName);
            });
        }
        
        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        
        return productRepository.findAll(spec, PageRequest.of(page, size)).getContent();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setCategories(productDetails.getCategories());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setImages(productDetails.getImages());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
