package com.ecommerce.ECommerceAPI.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PasswordResetBody {

    @NotBlank
    @NotNull
    private String token;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")
    @Size(min = 6, max = 32)
    private String password;

    public @NotBlank @NotNull String getToken() {
        return token;
    }

    public void setToken(@NotBlank @NotNull String token) {
        this.token = token;
    }

    public @NotNull @NotBlank @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$") @Size(min = 6, max = 32) String getPassword() {
        return password;
    }

    public void setPassword(@NotNull @NotBlank @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$") @Size(min = 6, max = 32) String password) {
        this.password = password;
    }
}
