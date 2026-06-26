package com.ecommerce.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductRequestDto {

	 @NotBlank(message = "Product name is required")
	    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
	    private String productName;

	    @NotBlank(message = "Description is required")
	    @Size(max = 500, message = "Description cannot exceed 500 characters")
	    private String description;

	    @NotNull(message = "Price is required")
	    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
	    private BigDecimal price;

	    @NotNull(message = "Stock quantity is required")
	    @Min(value = 0, message = "Stock quantity cannot be negative")
	    private Integer stockQuantity;

		public ProductRequestDto(
				String productName,
				String description,
				BigDecimal price,
				Integer stockQuantity) {

			this.productName = productName;
			this.description = description;
			this.price = price;
			this.stockQuantity = stockQuantity;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public Integer getStockQuantity() {
			return stockQuantity;
		}

		public void setStockQuantity(Integer stockQuantity) {
			this.stockQuantity = stockQuantity;
		}
	    

		
		
}
