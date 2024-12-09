package com.ecommerce.ECommerceAPI.exception;

public class ProductOutOfStockException extends RuntimeException{

    public ProductOutOfStockException (String message){
        super(message);
    }
}
