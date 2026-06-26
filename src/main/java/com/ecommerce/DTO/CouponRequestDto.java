package com.ecommerce.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CouponRequestDto {

	    @NotBlank(message = "Coupon code is required")
	    private String couponCode;

	    @NotNull(message = "Discount percentage is required")
	    @DecimalMin(value = "1.0", message = "Discount must be greater than 0")
	    @DecimalMax(value = "100.0", message = "Discount cannot exceed 100")
	    private BigDecimal discountPercentage;

	    @NotNull(message = "Expiry date is required")
	    private LocalDate expiryDate;

	    @NotNull(message = "Coupon status is required")
	    private Boolean active;
	    
	    public CouponRequestDto() {}

		public CouponRequestDto(String couponCode,
				BigDecimal discountPercentage,
				LocalDate expiryDate,
				Boolean active) {
		
			this.couponCode = couponCode;
			this.discountPercentage = discountPercentage;
			this.expiryDate = expiryDate;
			this.active = active;
		}

		public String getCouponCode() {
			return couponCode;
		}

		public void setCouponCode(String couponCode) {
			this.couponCode = couponCode;
		}

		public BigDecimal getDiscountPercentage() {
			return discountPercentage;
		}

		public void setDiscountPercentage(BigDecimal discountPercentage) {
			this.discountPercentage = discountPercentage;
		}

		public LocalDate getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(LocalDate expiryDate) {
			this.expiryDate = expiryDate;
		}

		public Boolean getActive() {
			return active;
		}

		public void setActive(Boolean active) {
			this.active = active;
		}
	    
		
	    
	
	
}
