package com.ecommerce.Model;

import java.math.BigDecimal;

import com.ecommerce.Audit.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "products")
@Builder
public class Product extends Auditable{
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long productId;
		
		@Column(nullable = false)
		private String productName;
		
		@Column(nullable = false)
		private String description;
		
		@Column(nullable = false)
		private BigDecimal price;
		
		@Column(nullable = false)
		private Integer stockQuantity;

		
		public Product() {}
		
		public Product(Long productId, 
				String productName, 
				String description, 
				BigDecimal price,
				Integer stockQuantity) {
			
			this.productId = productId;
			this.productName = productName;
			this.description = description;
			this.price = price;
			this.stockQuantity = stockQuantity;
		}

		public Long getProductId() {
			return productId;
		}

		public void setProductId(Long productId) {
			this.productId = productId;
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
