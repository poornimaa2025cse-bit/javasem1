package com.citygrid.smartlighting.repository;

import com.citygrid.smartlighting.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
