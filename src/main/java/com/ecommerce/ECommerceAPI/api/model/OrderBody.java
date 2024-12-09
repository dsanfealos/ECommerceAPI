package com.ecommerce.ECommerceAPI.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderBody {

    @NotNull
    @NotBlank
    private Long addressId;
    @NotNull
    @NotBlank
    private List<OrderQuantityBody> quantities;

    public @NotNull @NotBlank Long getAddressId() {
        return addressId;
    }

    public void setAddressId(@NotNull @NotBlank Long addressId) {
        this.addressId = addressId;
    }

    public @NotNull @NotBlank List<OrderQuantityBody> getQuantities() {
        return quantities;
    }

    public void setQuantities(@NotNull @NotBlank List<OrderQuantityBody> quantities) {
        this.quantities = quantities;
    }
}
