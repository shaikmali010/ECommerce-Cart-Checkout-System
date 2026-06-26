package com.ecommerce.Service;

import org.springframework.data.domain.Page;

import com.ecommerce.DTO.ProductRequestDto;
import com.ecommerce.DTO.ProductResponseDto;

public interface ProductService {

	ProductResponseDto createProduct(ProductRequestDto requestDto);
	
	ProductResponseDto getProductById(Long productId);
	
	Page<ProductResponseDto> getAllProducts(int page, int size, String sortBy, String sortDir);
	
	ProductResponseDto getProductByName(String productName);
	
	ProductResponseDto updateProductById(Long productId, ProductRequestDto reqyestDto);
	
	void deleteProductById(Long productId);
	
	
}
