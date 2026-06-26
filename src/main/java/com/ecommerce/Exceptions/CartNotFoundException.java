package com.ecommerce.Exceptions;

public class CartNotFoundException extends RuntimeException {
	
	public CartNotFoundException(String message) {
		super(message);
	}

}
