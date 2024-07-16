package com.ecommerce.ECommerceAPI.model.dao;

import com.ecommerce.ECommerceAPI.model.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductDAO extends ListCrudRepository<Product, Long> {
}
