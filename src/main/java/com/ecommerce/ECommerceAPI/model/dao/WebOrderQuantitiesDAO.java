package com.ecommerce.ECommerceAPI.model.dao;

import com.ecommerce.ECommerceAPI.model.WebOrderQuantities;
import org.springframework.data.repository.ListCrudRepository;

public interface WebOrderQuantitiesDAO extends ListCrudRepository<WebOrderQuantities, Long> {
}
