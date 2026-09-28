package com.citygrid.smartlighting.service;

import com.citygrid.smartlighting.entity.Product;
import com.citygrid.smartlighting.exception.ResourceNotFoundException;
import com.citygrid.smartlighting.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
