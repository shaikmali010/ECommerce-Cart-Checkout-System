package com.ecommerce.ServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommerce.Constants.Constants;
import com.ecommerce.DTO.ProductRequestDto;
import com.ecommerce.DTO.ProductResponseDto;
import com.ecommerce.Exceptions.ProductAlreadyExistsException;
import com.ecommerce.Exceptions.ProductNotFoundException;
import com.ecommerce.Model.Product;
import com.ecommerce.Repository.ProductRepository;
import com.ecommerce.Service.ProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository productRepository;
	
	private static final Logger logger = LoggerFactory
			.getLogger(ProductServiceImpl.class);
	
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	private ProductResponseDto mapToResponse(Product product) {
		
		return ProductResponseDto.builder()
						.productId(product.getProductId())
				        .productName(product.getProductName())
				        .description(product.getDescription())
				        .price(product.getPrice())
				        .stockQuantity(product.getStockQuantity())
				        .build();
	}
	
	private Product mapToEntity(ProductRequestDto requestDto) {
		
		return Product.builder()
				.productName(requestDto.getProductName())
				.description(requestDto.getDescription())
				.price(requestDto.getPrice())
				.stockQuantity(requestDto.getStockQuantity())
				.build();
	}
	
//	create product
	@Override
	@Transactional
	public ProductResponseDto createProduct(ProductRequestDto requestDto) {
		
		logger.info("Creating product with name: {}", requestDto.getProductName());

		if (productRepository.existsByProductName(requestDto.getProductName())) {
			
			logger.warn("Product already exists with name: {}",
			        requestDto.getProductName());

		    throw new ProductAlreadyExistsException(Constants.PRODUCT_ALREADY_EXISTS);
		}

		Product product = mapToEntity(requestDto);

		Product savedProduct = productRepository.save(product);

		logger.info("Product created successfully with id: {}", savedProduct.getProductId());

		return mapToResponse(savedProduct);
	}
	
	@Override
	public ProductResponseDto getProductById(Long productId) {
		
		logger.info("Fetching the product by id: {}", productId);
		
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> {
					logger.error("The id does not exists {}", productId);
					
					return new ProductNotFoundException(Constants.PRODUCT_NOT_FOUND);
				});
		
		return mapToResponse(product);
	}
	
	@Override
	public Page<ProductResponseDto> getAllProducts(int page, int size, String sortBy, String sortDir){
		
		logger.info("Fetching all Products data");
		
		Sort sort = sortDir.equalsIgnoreCase("asc")
						?Sort.by(sortBy).ascending()
					    :Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(page, size, sort);
		
		logger.info("fetching all product details by page: {}, size: {}, sortBy: {}, sortDir: {}",
				page, size, sortBy, sortDir);
		
		Page<Product> product = productRepository.findAll(pageable);
		
		logger.info("Total Products found: {}", product.getTotalElements());
		
		return product.map(this::mapToResponse);
	}
	
	@Override
	public ProductResponseDto getProductByName(String productName) {
		
		logger.info("Fetching product details by name: {}", productName);
		
		Product product = productRepository.findByProductNameIgnoreCase(productName)
				           .orElseThrow(() -> {
				        	   
				        	   logger.error("The product Does not Exist with the name: {}", productName);
				        	   
				        	   return new ProductNotFoundException(Constants.PRODUCT_NOT_FOUND);
				           });
		return mapToResponse(product);
	}
	
	@Override
	public ProductResponseDto updateProductById(Long productId, ProductRequestDto requestDto) {
		
		logger.info("Updating the product with id: {}", productId);
		
		Product product = productRepository.findById(productId)
				          .orElseThrow(() -> {
				        	  
				        	  logger.error("Product not found with id: {}", productId);
				        	  
				        	  return new ProductNotFoundException(Constants.PRODUCT_NOT_FOUND);
				          });
		
		if(requestDto.getProductName() != null 
				&& !requestDto.getProductName().equals(product.getProductName())
				&& productRepository.existsByProductName(requestDto.getProductName())) {
			
			logger.warn("Product already exists with name: {}", 
					requestDto.getProductName());
			
			throw new ProductAlreadyExistsException(Constants.PRODUCT_ALREADY_EXISTS);
		}
		
		product.setProductName(requestDto.getProductName());
		product.setDescription(requestDto.getDescription());
		product.setPrice(requestDto.getPrice());
		product.setStockQuantity(requestDto.getStockQuantity());
		
		Product updatedProduct = productRepository.save(product);
		
		logger.info("Product Updated successfully with id: {}", updatedProduct.getProductId());
		
		return mapToResponse(updatedProduct);
	}
	
	@Override
	public void deleteProductById(Long productId) {
		
		logger.info("Deleting Product with id: {]", productId);
		
		Product product = productRepository.findById(productId)
		          .orElseThrow(() -> {
		        	  
		        	  logger.error("Product not found with id: {}", productId);
		        	  
		        	  return new ProductNotFoundException(Constants.PRODUCT_NOT_FOUND);
		          });
		
		productRepository.delete(product);
		
		logger.info("Product deleted successfully with id: {}", productId);
		
		
	}
	

}
