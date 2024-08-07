package com.ecommerce.ECommerceAPI.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoginBody {

    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String password;

    public void setUsername(@NotNull @NotBlank String username) {
        this.username = username;
    }

    public void setPassword(@NotNull @NotBlank String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
