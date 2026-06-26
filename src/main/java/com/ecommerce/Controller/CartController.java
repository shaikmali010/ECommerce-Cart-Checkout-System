package com.ecommerce.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.DTO.CartRequestDto;
import com.ecommerce.DTO.CartResponseDto;
import com.ecommerce.DTO.UpdateCartRequestDto;
import com.ecommerce.Service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
@Validated
@Tag(
        name = "Cart APIs",
        description = "APIs for managing shopping cart operations"
)
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    @Operation(
            summary = "Add product to cart",
            description = "Adds a product to the user's cart. If the cart does not exist, a new cart is created."
    )
    public ResponseEntity<CartResponseDto> addProductToCart(
            @Valid @RequestBody CartRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addProductToCart(requestDto));
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "Get cart by user id",
            description = "Retrieves the cart and all its items for the given user."
    )
    public ResponseEntity<CartResponseDto> getCartByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PutMapping("/{cartItemId}")
    @Operation(
            summary = "Update cart item quantity",
            description = "Updates the quantity of a product in the cart."
    )
    public ResponseEntity<CartResponseDto> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartRequestDto requestDto) {

        return ResponseEntity.ok(
                cartService.updateCartItem(cartItemId, requestDto.getQuantity()));
    }

    @DeleteMapping("/{cartItemId}")
    @Operation(
            summary = "Remove cart item",
            description = "Removes a specific product from the cart."
    )
    public ResponseEntity<String> removeCartItem(
            @PathVariable Long cartItemId) {

        cartService.removeCartItem(cartItemId);

        return ResponseEntity.ok("Cart item removed successfully.");
    }

    @DeleteMapping("/clear/{userId}")
    @Operation(
            summary = "Clear cart",
            description = "Removes all products from the user's cart."
    )
    public ResponseEntity<String> clearCart(
            @PathVariable Long userId) {

        cartService.clearCart(userId);

        return ResponseEntity.ok("Cart cleared successfully.");
    }
}