package com.ecommerce.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;

@Builder
public class CartResponseDto {

	private Long cartId;

    private Long userId;

    private List<CartItemResponseDto> cartItems;

    private BigDecimal grandTotal;

	public CartResponseDto(Long cartId, Long userId, List<CartItemResponseDto> cartItems, BigDecimal grandTotal) {
		
		this.cartId = cartId;
		this.userId = userId;
		this.cartItems = cartItems;
		this.grandTotal = grandTotal;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<CartItemResponseDto> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItemResponseDto> cartItems) {
		this.cartItems = cartItems;
	}

	public BigDecimal getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}
    
    
}
