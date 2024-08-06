package com.ecommerce.ECommerceAPI.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ecommerce.ECommerceAPI.model.LocalUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {


    @Value("${jwt.algorithm.key}")
    private String algorithmKey;
    @Value("${jwt.issuer}")
    private String issuer;
    /**
     * How many seconds before thejwt expires
     */
    @Value("${jwt.expiryInSeconds}")
    private int expiryInSeconds;
    private Algorithm algorithm;

    private static final String USERNAME_KEY = "USERNAME";
    private static final String EMAIL_KEY = "EMAIL";

    @PostConstruct
    public void postConstruct(){
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    /**
     * It allows the user to get a jwt, so they can use the app
     * just by login once in a certain amount of time
     * @param user A LocalUser that logged in
     * @return JWT
     */
    public String generateJWT(LocalUser user){
        return JWT.create()
                .withClaim(USERNAME_KEY, user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * expiryInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String generateVerificationJWT(LocalUser user){
        return JWT.create()
                .withClaim(EMAIL_KEY, user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * expiryInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUsername(String token){
        return JWT.decode(token).getClaim(USERNAME_KEY).asString();
    }
}
