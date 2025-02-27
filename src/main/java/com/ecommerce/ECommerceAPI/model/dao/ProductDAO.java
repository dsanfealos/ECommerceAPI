package com.ecommerce.ECommerceAPI.model.dao;

import com.ecommerce.ECommerceAPI.model.Product;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ProductDAO extends ListCrudRepository<Product, Long> {
    Optional<Product> findByName (String name);
}
