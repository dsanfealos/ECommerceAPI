package com.ecommerce.ECommerceAPI.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class OrderBody {

    @NotNull
    @NotBlank
    private Long addressId;
    @NotNull
    @NotBlank
    private Map<Long, Integer> quantities;

    public @NotNull @NotBlank Long getAddressId() {
        return addressId;
    }

    public void setAddressId(@NotNull @NotBlank Long addressId) {
        this.addressId = addressId;
    }

    public @NotNull @NotBlank Map<Long, Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(@NotNull @NotBlank Map<Long, Integer> quantities) {
        this.quantities = quantities;
    }
}
