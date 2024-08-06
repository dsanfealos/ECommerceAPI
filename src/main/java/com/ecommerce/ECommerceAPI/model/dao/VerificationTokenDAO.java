package com.ecommerce.ECommerceAPI.model.dao;

import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
    void deleteByUser(LocalUser user);
}
