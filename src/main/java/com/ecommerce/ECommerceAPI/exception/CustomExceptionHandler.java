package com.ecommerce.ECommerceAPI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleCloudVendorNotFoundException(UserAlreadyExistsException userAlreadyExistsException){
        //We create a CloudVendorException with its constructor filling the message, cause and http status of the error.
        CustomException userException = new CustomException(
                userAlreadyExistsException.getMessage(),
                userAlreadyExistsException.getCause(),
                HttpStatus.CONFLICT
        );

        //It is returned the object created and the HttpStatus
        return new ResponseEntity<>(userException, HttpStatus.CONFLICT);
    }
}
