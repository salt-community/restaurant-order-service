package com.example.restaurantorderservice.exception;

import com.example.restaurantorderservice.exception.custom.JsonMapperException;
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

    private void logException(Exception e) {
        System.out.println("e.getClass() = " + e.getClass());
        System.out.println("e.getMessage() = " + e.getMessage());
        System.out.println("e.getCause() = " + e.getCause());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(Exception ex) {
        logException(ex);
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

    @ExceptionHandler(JsonMapperException.class)
    public ResponseEntity<ErrorResponseDto> handleJsonMapperException(JsonMapperException e) {
        logException(e);
        return buildResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
