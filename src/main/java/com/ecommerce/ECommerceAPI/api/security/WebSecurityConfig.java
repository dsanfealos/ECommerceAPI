package com.ecommerce.ECommerceAPI.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

/**
 * Configurations of the security on endpoints
 */
@Configuration
public class WebSecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    public WebSecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Filter chain to configure security
     * @param http The security project
     * @return The chain built
     * @throws Exception Throw an error configuring
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/product", "/product/**", "/user/**", "/auth/register", "/auth/login",
                        "/auth/verify", "/error", "/auth/forgot", "/auth/reset",
                        "/websocket", "/websocket/**").permitAll()
                .anyRequest().authenticated());
        return http.build();
    }
}
