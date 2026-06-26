package com.ecommerce.Exceptions;

public class CouponExpiredException extends RuntimeException {
	
	public CouponExpiredException(String message) {
		super(message);
	}

}
