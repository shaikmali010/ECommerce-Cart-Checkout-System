package com.ecommerce.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ecommerce.Constants.Constants;
import com.ecommerce.DTO.CartItemResponseDto;
import com.ecommerce.DTO.CartRequestDto;
import com.ecommerce.DTO.CartResponseDto;
import com.ecommerce.Exceptions.CartItemNotFoundException;
import com.ecommerce.Exceptions.CartNotFoundException;
import com.ecommerce.Exceptions.InsufficientStockException;
import com.ecommerce.Exceptions.ProductNotFoundException;
import com.ecommerce.Exceptions.UserNotFoundException;
import com.ecommerce.Model.Cart;
import com.ecommerce.Model.CartItem;
import com.ecommerce.Model.Product;
import com.ecommerce.Model.User;
import com.ecommerce.Repository.CartItemRepository;
import com.ecommerce.Repository.CartRepository;
import com.ecommerce.Repository.ProductRepository;
import com.ecommerce.Repository.UserRepository;
import com.ecommerce.Service.CartService;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {
	
	private final CartRepository cartRepository;
	
	private final CartItemRepository cartItemRepository;
	
	private final UserRepository userRepository;
	
	private final ProductRepository productRepository;
	
	private static final Logger logger = LoggerFactory
			.getLogger(CartServiceImpl.class);

	public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
			UserRepository userRepository, ProductRepository productRepository) {
	
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}
	
	private CartItemResponseDto mapToCartItemResponse(CartItem cartItem) {
		
		return CartItemResponseDto.builder()
				.cartItemId(cartItem.getCartItemId())
				.productId(cartItem.getProduct().getProductId())
				.productName(cartItem.getProduct().getProductName())
				.quantity(cartItem.getQuantity())
				.unitPrice(cartItem.getUnitPrice())
				.totalPrice(cartItem.getTotalPrice())
				.build();
	}
	
	private CartResponseDto mapToCartResponse(Cart cart) {
		
		List<CartItemResponseDto> cartItem = cart.getCartItems()
				.stream().map(this::mapToCartItemResponse)
				.toList();
		
		BigDecimal grandTotal = cartItem.stream()
				.map(CartItemResponseDto::getTotalPrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return CartResponseDto.builder()
				.cartId(cart.getCartId())
				.userId(cart.getUser().getUserId())
				.cartItems(cartItem)
				.grandTotal(grandTotal)
				.build();
	}
	
	@Override
	@Transactional
	public CartResponseDto addProductToCart(CartRequestDto requestDto) {
		
		logger.info("Adding product to cart for user id: {}", requestDto.getUserId());
		
//		Find the user
		User user = userRepository.findById(requestDto.getUserId())
				.orElseThrow(() -> {
					
					logger.error("User not found with id: {}", requestDto.getUserId());

					return new UserNotFoundException(Constants.USER_NOT_FOUND);
				});
		
//		Find the Product
		Product product = productRepository.findById(requestDto.getProductId())
				.orElseThrow(() -> {
					
					logger.error("Product not found with id: {}", 
							requestDto.getProductId());
					
					return new ProductNotFoundException(Constants.PRODUCT_NOT_FOUND);
				});
		
//		find the Users cart. If not available, create a new cart
		
		Cart cart = cartRepository.findByUserUserId(user.getUserId())
				.orElseGet(() -> {
					
					logger.info("Cart not found. creating new cart.");
					
					Cart newCart = Cart.builder()
							.user(user)
							.build();
					return cartRepository.save(newCart);
				});
		
//		Check whether this product already exists in the cart.
		CartItem cartItem = cartItemRepository.findByCartCartIdAndProductProductId(
				 			cart.getCartId(), product.getProductId())
							.orElse(null);
		
//		If Product already exist, increase quantity
		
		if (cartItem != null) {

		    int updatedQuantity =
		            cartItem.getQuantity() + requestDto.getQuantity();

		    if (updatedQuantity > product.getStockQuantity()) {

		        logger.warn(
		                "Insufficient stock. Available: {}, Requested: {}",
		                product.getStockQuantity(),
		                updatedQuantity);

		        throw new InsufficientStockException(
		                "Insufficient stock. Available stock: "
		                        + product.getStockQuantity());
		    }

		    logger.info("Product already exists in cart. Update quantity.");

		    cartItem.setQuantity(updatedQuantity);

		    cartItem.setTotalPrice(
		            cartItem.getUnitPrice()
		                    .multiply(BigDecimal.valueOf(updatedQuantity)));
		}else {
			
//			Otherwise create a new CartItem
			
			
			if (requestDto.getQuantity() > product.getStockQuantity()) {

			    logger.warn(
			            "Insufficient stock. Available: {}, Requested: {}",
			            product.getStockQuantity(),
			            requestDto.getQuantity());

			    throw new InsufficientStockException(
			            "Insufficient stock. Available stock: "
			                    + product.getStockQuantity());
			}
			
			logger.info("Adding new product to cart.");
			
			cartItem = CartItem.builder()
					.cart(cart)
					.product(product)
					.quantity(requestDto.getQuantity())
					.unitPrice(product.getPrice())
					.totalPrice(product.getPrice().multiply(BigDecimal.valueOf(
							requestDto.getQuantity())))
					.build();
			
			cart.getCartItems().add(cartItem);
		}
		
       cartItemRepository.save(cartItem);
       
       Cart updatedCart = cartRepository.findById(cart.getCartId())
    		   .orElseThrow(() -> new CartNotFoundException(Constants.CART_NOT_FOUND));
				                               
		logger.info("Product added successfully to cart.");
       
		return mapToCartResponse(updatedCart);
	}
	
	
	@Override
	public CartResponseDto getCartByUserId(Long userId) {

	    logger.info("Fetching cart for user id: {}", userId);

	    Cart cart = cartRepository.findByUserUserId(userId)
	            .orElseThrow(() -> {

	                logger.error("Cart not found for user id: {}", userId);

	                return new CartNotFoundException(Constants.CART_NOT_FOUND);
	            });

	    logger.info("Cart fetched successfully for user id: {}", userId);

	    return mapToCartResponse(cart);
	}
	

	@Override
	@Transactional
	public CartResponseDto updateCartItem(Long cartItemId, Integer quantity) {

	    logger.info("Updating cart item with id: {}", cartItemId);

	    CartItem cartItem = cartItemRepository.findById(cartItemId)
	            .orElseThrow(() -> {

	                logger.error("Cart item not found with id: {}", cartItemId);

	                return new CartItemNotFoundException(Constants.CART_ITEM_NOT_FOUND);
	            });

	 // Stock validation
	    if (quantity > cartItem.getProduct().getStockQuantity()) {

	        logger.warn("Requested quantity {} exceeds available stock {}",
	                quantity,
	                cartItem.getProduct().getStockQuantity());

	        throw new InsufficientStockException(
	                "Insufficient stock. Available stock: "
	                        + cartItem.getProduct().getStockQuantity()
	                        + ", Requested: "
	                        + quantity);
	    }
	    
	    cartItem.setQuantity(quantity);

	    cartItem.setTotalPrice(
	            cartItem.getUnitPrice()
	                    .multiply(BigDecimal.valueOf(quantity)));

	    CartItem updatedCartItem = cartItemRepository.save(cartItem);

	    logger.info("Cart item updated successfully with id: {}", updatedCartItem.getCartItemId());

	    return mapToCartResponse(updatedCartItem.getCart());
	}
	
	@Override
	@Transactional
	public void removeCartItem(Long cartItemId) {

	    logger.info("Removing cart item with id: {}", cartItemId);

	    CartItem cartItem = cartItemRepository.findById(cartItemId)
	            .orElseThrow(() -> {

	                logger.error("Cart item not found with id: {}", cartItemId);

	                return new CartItemNotFoundException(Constants.CART_ITEM_NOT_FOUND);
	            });

	    cartItemRepository.delete(cartItem);

	    logger.info("Cart item removed successfully with id: {}", cartItemId);
	}
	
	@Override
	@Transactional
	public void clearCart(Long userId) {

	    logger.info("Clearing cart for user id: {}", userId);

	    Cart cart = cartRepository.findByUserUserId(userId)
	            .orElseThrow(() -> {

	                logger.error("Cart not found for user id: {}", userId);

	                return new CartNotFoundException(Constants.CART_NOT_FOUND);
	            });

	    cart.getCartItems().clear();

	    cartRepository.save(cart);

	    logger.info("Cart cleared successfully for user id: {}", userId);
	}
}
