package com.ecommerce.Exceptions;

public class PaymentFailedException extends RuntimeException{
	
	public PaymentFailedException(String message) {
		super(message);
	}

}
