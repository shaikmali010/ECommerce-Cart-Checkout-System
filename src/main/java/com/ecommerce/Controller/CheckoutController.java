package com.ecommerce.Controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.DTO.CheckoutRequestDto;
import com.ecommerce.DTO.OrderResponseDto;
import com.ecommerce.Service.CheckoutService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/checkout")
@Tag(name = "Checkout APIs", 
          description = "Checkout and place order"
          )
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    @Operation(summary = "Checkout Order")
    public ResponseEntity<OrderResponseDto> checkout(
            @Valid @RequestBody CheckoutRequestDto requestDto) {

        return ResponseEntity.ok(checkoutService.checkout(requestDto));
    }
}