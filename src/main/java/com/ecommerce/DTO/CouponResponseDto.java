package com.ecommerce.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;

@Builder
public class CouponResponseDto {

	 private Long couponId;

	    private String couponCode;

	    private BigDecimal discountPercentage;

	    private LocalDate expiryDate;

	    private Boolean active;
	    
	    public CouponResponseDto() {}

		public CouponResponseDto(Long couponId, String couponCode, BigDecimal discountPercentage, LocalDate expiryDate,
				Boolean active) {
		
			this.couponId = couponId;
			this.couponCode = couponCode;
			this.discountPercentage = discountPercentage;
			this.expiryDate = expiryDate;
			this.active = active;
		}

		public Long getCouponId() {
			return couponId;
		}

		public void setCouponId(Long couponId) {
			this.couponId = couponId;
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
