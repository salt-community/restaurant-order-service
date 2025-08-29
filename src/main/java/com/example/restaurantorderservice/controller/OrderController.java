package com.example.restaurantorderservice.controller;

import com.example.restaurantorderservice.dto.request.OrderRequestDto;
import com.example.restaurantorderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
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
        description = "Should respond with 'Hello World!'"
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
    public ResponseEntity<String> createOrder(
        @RequestBody OrderRequestDto req) {
        orderService.createOrder(req);

        return ResponseEntity.ok("OrderPlaced");
    }
}


