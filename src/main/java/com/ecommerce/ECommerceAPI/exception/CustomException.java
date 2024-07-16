package com.ecommerce.ECommerceAPI.exception;

import org.springframework.http.HttpStatus;

public class CustomException {
    //We will get a create an object to call it at the
    // exception handler, filling the constructor with
    // the desired params
    private final String message;

    private final Throwable throwable;
    private  final HttpStatus httpStatus;
    public CustomException(String message, Throwable throwable, HttpStatus httpStatus) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
