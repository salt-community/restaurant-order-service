package com.example.restaurantorderservice.controller;

import com.example.restaurantorderservice.dto.request.OrderRequestDto;
import com.example.restaurantorderservice.dto.response.OrderResponseDto;
import com.example.restaurantorderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(
        summary = "Placing order",
        description = "Should respond OrderId"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            ),
        }
    )
    @PostMapping("/place-order")
    public ResponseEntity<OrderResponseDto> createOrder(
        @RequestBody @Valid OrderRequestDto req) {
        OrderResponseDto response = new OrderResponseDto(orderService.createOrder(req));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}


