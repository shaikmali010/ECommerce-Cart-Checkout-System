package com.ecommerce.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;


@Builder
public class OrderResponseDto {

    private Long orderId;

    private Long userId;

    private BigDecimal totalAmount;

    private BigDecimal discountAmount;

    private BigDecimal finalAmount;

    private String paymentStatus;

    private LocalDateTime orderDate;

    private List<OrderItemResponseDto> orderItems;
    
    public OrderResponseDto() {}

	public OrderResponseDto(Long orderId, Long userId, BigDecimal totalAmount, BigDecimal discountAmount,
			BigDecimal finalAmount, String paymentStatus, LocalDateTime orderDate,
			List<OrderItemResponseDto> orderItems) {
		
		this.orderId = orderId;
		this.userId = userId;
		this.totalAmount = totalAmount;
		this.discountAmount = discountAmount;
		this.finalAmount = finalAmount;
		this.paymentStatus = paymentStatus;
		this.orderDate = orderDate;
		this.orderItems = orderItems;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public List<OrderItemResponseDto> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemResponseDto> orderItems) {
		this.orderItems = orderItems;
	}
    
    
}