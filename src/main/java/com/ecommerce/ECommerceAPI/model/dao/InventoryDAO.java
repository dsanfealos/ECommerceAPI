package com.ecommerce.ECommerceAPI.model.dao;

import com.ecommerce.ECommerceAPI.model.Inventory;
import com.ecommerce.ECommerceAPI.model.Product;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface InventoryDAO extends ListCrudRepository<Inventory, Long> {
    Optional<Inventory> findByProduct(Product product);
}
