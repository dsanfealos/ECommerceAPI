package com.ecommerce.ECommerceAPI.model.dao;

import com.ecommerce.ECommerceAPI.model.Role;
import org.springframework.data.repository.ListCrudRepository;

public interface RoleDAO extends ListCrudRepository<Role, Long> {

    Role findByName(String role);
}
