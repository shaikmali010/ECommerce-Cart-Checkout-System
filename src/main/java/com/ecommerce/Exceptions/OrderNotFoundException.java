package com.ecommerce.Exceptions;

public class OrderNotFoundException extends RuntimeException {
	
	public OrderNotFoundException(String message) {
		super(message);
	}

}
