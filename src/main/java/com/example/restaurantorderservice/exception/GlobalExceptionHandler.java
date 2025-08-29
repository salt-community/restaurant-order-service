package com.example.restaurantorderservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleValidationExceptions(Exception ex) {
        System.out.println("ex.getClass() = " + ex.getClass());
        System.out.println("ex.getMessage() = " + ex.getMessage());
        System.out.println("ex.getCause() = " + ex.getCause());
        return ResponseEntity.internalServerError().build();
    }
}
