package com.ecommerce.Service;

import com.ecommerce.DTO.CartRequestDto;
import com.ecommerce.DTO.CartResponseDto;

public interface CartService {

	CartResponseDto addProductToCart(CartRequestDto requestDto);

	CartResponseDto getCartByUserId(Long userId);
	
	CartResponseDto updateCartItem(Long cartItemId, Integer quantity);
	
	void removeCartItem(Long cartItemId);
	
	void clearCart(Long userId);
	
}
