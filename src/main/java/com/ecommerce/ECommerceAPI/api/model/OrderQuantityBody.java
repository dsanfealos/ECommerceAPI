package com.ecommerce.ECommerceAPI.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrderQuantityBody {

    @NotNull
    @NotBlank
    private Long productId;
    @NotNull
    @NotBlank
    private Integer quantity;

    public @NotNull @NotBlank Long getProductId() {
        return productId;
    }

    public void setProductId(@NotNull @NotBlank Long productId) {
        this.productId = productId;
    }

    public @NotNull @NotBlank Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull @NotBlank Integer quantity) {
        this.quantity = quantity;
    }
}
