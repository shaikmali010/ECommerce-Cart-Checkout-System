package com.ecommerce.Controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.DTO.OrderResponseDto;
import com.ecommerce.Service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/orders")
@Validated
@Tag(
        name = "Order APIs",
        description = "APIs for viewing order details"
)
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{orderId}")
    @Operation(
            summary = "Get Order By Id",
            description = "Fetch order details using order id."
    )
    public ResponseEntity<OrderResponseDto> getOrderById(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderById(orderId));
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get Orders By User",
            description = "Fetch all orders placed by a user."
    )
    public ResponseEntity<Page<OrderResponseDto>> getOrdersByUserId(

            @PathVariable Long userId,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "orderDate") String sortBy,

            @RequestParam(defaultValue = "desc") String sortDir) {

        return ResponseEntity.ok(
                orderService.getOrdersByUserId(
                        userId,
                        page,
                        size,
                        sortBy,
                        sortDir));
    }

    @GetMapping
    @Operation(
            summary = "Get All Orders",
            description = "Retrieve all orders with paging and sorting."
    )
    public ResponseEntity<Page<OrderResponseDto>> getAllOrders(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "orderDate") String sortBy,

            @RequestParam(defaultValue = "desc") String sortDir) {

        return ResponseEntity.ok(
                orderService.getAllOrders(
                        page,
                        size,
                        sortBy,
                        sortDir));
    }
}