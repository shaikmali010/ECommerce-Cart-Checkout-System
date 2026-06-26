package com.ecommerce.Exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> UserAlreadyExistsException(UserAlreadyExistsException ex){
		return ResponseEntity.status(HttpStatus.CONFLICT)
				          .body(ex.getMessage());
	}
	
//	-------------------------------------------------------------------------------------------
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> UserNotFoundException(UserNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ex.getMessage());
	}
	
//	--------------------------------------------------------------------------------------------
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> MethodArgumentNotValidException(
			MethodArgumentNotValidException ex){
		Map<String, String> errors = new HashMap<>();
		
		ex.getBindingResult()
		  .getFieldErrors()
		  .forEach(error -> 
		           errors.put(error.getField(), error.getDefaultMessage()));
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				             .body(errors);
		
	}
	
	
//		------------------------------------------------------------------------------------------
		
	@ExceptionHandler(Exception.class)
		public ResponseEntity<String> handleException(Exception ex){
		
		ex.printStackTrace();
		
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					             .body(ex.getClass().getName()+ " : "+ex.getMessage());
		}
	
//	------------------------------------------------------------------------------------------------


	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<String> ProductNotFoundException(ProductNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				 .body(ex.getMessage());
	}
	
//	-------------------------------------------------------------------------------------------------
	
	@ExceptionHandler(ProductAlreadyExistsException.class)
	public ResponseEntity<String> ProductAlreadyExistsException(ProductAlreadyExistsException ex){
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(ex.getMessage());
				
	}
	
//	-------------------------------------------------------------------------------------------------
	
	@ExceptionHandler(CartNotFoundException.class)
	public ResponseEntity<String> CartNotFoundException(CartNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ex.getMessage());		
	}
	
//	-------------------------------------------------------------------------------------------------
	
	@ExceptionHandler(CartItemNotFoundException.class)
	public ResponseEntity<String> CartItemNotFounsException(CartItemNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ex.getMessage());
	}
	
//	--------------------------------------------------------------------------------------------------
	
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<String> InsufficientStockException(InsufficientStockException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ex.getMessage());
	}
	
//	---------------------------------------------------------------------------------------------------
	
	@ExceptionHandler(CouponNotFoundException.class)
	public ResponseEntity<String> couponNotFoundException(
	        CouponNotFoundException ex){

	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(ex.getMessage());
	}
	
//	--------------------------------------------------------------------------------------------------
	
	@ExceptionHandler(CouponAlreadyExistsException.class)
	public ResponseEntity<String> couponAlreadyExistsException(
	        CouponAlreadyExistsException ex){

	    return ResponseEntity.status(HttpStatus.CONFLICT)
	            .body(ex.getMessage());
	}
	
//	---------------------------------------------------------------------------------------------------
	
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<String> OrderNotFoundException(OrderNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ex.getMessage());
	}
	
//	---------------------------------------------------------------------------------------------------
	
	@ExceptionHandler(CouponInactiveException.class)
	public ResponseEntity<String> CouponInactiveException(CouponInactiveException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ex.getMessage());
	}
	
//	----------------------------------------------------------------------------------------------------
	
	@ExceptionHandler(CouponExpiredException.class)
	public ResponseEntity<String> CouponExpiredException(CouponExpiredException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				     .body(ex.getMessage());
	}
	
//	----------------------------------------------------------------------------------------------------
	
	@ExceptionHandler(PaymentFailedException.class)
	public ResponseEntity<String> PaymentFailedException(PaymentFailedException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ex.getMessage());
	}
	
}
