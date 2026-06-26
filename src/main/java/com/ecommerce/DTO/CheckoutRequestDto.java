package com.ecommerce.DTO;

import jakarta.persistence.Column;

public class CheckoutRequestDto {

	@Column(nullable = false)
    private Long userId;

	@Column(nullable = false)
    private String couponCode;
    
	@Column(nullable = false)
    private String paymentStatus;
    
    public CheckoutRequestDto() {}

	public CheckoutRequestDto(Long userId, String couponCode, String paymentStatus) {
		
		this.userId = userId;
		this.couponCode = couponCode;
		this.paymentStatus = paymentStatus;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
	
	
	
    
    
}