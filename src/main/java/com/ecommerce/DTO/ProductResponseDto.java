package com.ecommerce.DTO;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDto {

	    private Long productId;
	    private String productName;
	    private String description;
	    private BigDecimal price;
	    private Integer stockQuantity;
	
}

