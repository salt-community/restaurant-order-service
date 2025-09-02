package com.example.restaurantorderservice.controller;

import com.example.restaurantorderservice.dto.request.OrderRequestDto;
import com.example.restaurantorderservice.dto.response.OrderIdResponseDto;
import com.example.restaurantorderservice.dto.response.OrderResponseDto;
import com.example.restaurantorderservice.model.Order;
import com.example.restaurantorderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(
        summary = "Creating/placing order",
        description = "Should respond with OrderId"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Created"
            ),
            @ApiResponse(
                responseCode = "200",
                description = "Bad Request"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            ),
        }
    )
    @PostMapping("/place-order")
    public ResponseEntity<OrderIdResponseDto> createOrder(
        @RequestBody @Valid OrderRequestDto req) {
        OrderIdResponseDto response = new OrderIdResponseDto(orderService.createOrder(req));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Get order by ID",
        description = "Should respond with a specific order"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Order Not Found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            ),
        }
    )
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(
        @PathVariable UUID orderId) {
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(OrderResponseDto.fromOrder(order));
    }

    @Operation(
        summary = "Remove order by ID",
        description = "Should respond with a specific order"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "No Content - Successfully deleted Order"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Order Not Found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            ),
        }
    )
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Void> removeOrder(
        @PathVariable UUID orderId) {
        orderService.removeOrder(orderId);
        return ResponseEntity.noContent().build();
    }


}


