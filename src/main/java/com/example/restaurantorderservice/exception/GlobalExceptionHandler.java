package com.example.restaurantorderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponseDto> buildResponse(String message, HttpStatus status) {
        ErrorResponseDto error = new ErrorResponseDto(message, status.value(), LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(Exception ex) {
        System.out.println("ex.getClass() = " + ex.getClass());
        System.out.println("ex.getMessage() = " + ex.getMessage());
        System.out.println("ex.getCause() = " + ex.getCause());
        return buildResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(Exception e) {
        return buildResponse("Bad request", HttpStatus.BAD_REQUEST);
    }
}
