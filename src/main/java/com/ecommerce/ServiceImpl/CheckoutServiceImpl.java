package com.ecommerce.ServiceImpl;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ecommerce.Constants.Constants;
import com.ecommerce.DTO.CheckoutRequestDto;
import com.ecommerce.DTO.OrderItemResponseDto;
import com.ecommerce.DTO.OrderResponseDto;
import com.ecommerce.Exceptions.CartNotFoundException;
import com.ecommerce.Exceptions.CouponExpiredException;
import com.ecommerce.Exceptions.CouponInactiveException;
import com.ecommerce.Exceptions.CouponNotFoundException;
import com.ecommerce.Exceptions.InsufficientStockException;
import com.ecommerce.Exceptions.OrderNotFoundException;
import com.ecommerce.Exceptions.PaymentFailedException;
import com.ecommerce.Exceptions.UserNotFoundException;
import com.ecommerce.Model.Cart;
import com.ecommerce.Model.CartItem;
import com.ecommerce.Model.Coupon;
import com.ecommerce.Model.Order;
import com.ecommerce.Model.OrderItem;
import com.ecommerce.Model.Product;
import com.ecommerce.Model.User;
import com.ecommerce.Repository.CartRepository;
import com.ecommerce.Repository.CouponRepository;
import com.ecommerce.Repository.OrderItemRepository;
import com.ecommerce.Repository.OrderRepository;
import com.ecommerce.Repository.ProductRepository;
import com.ecommerce.Repository.UserRepository;
import com.ecommerce.Service.CheckoutService;

import jakarta.transaction.Transactional;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(CheckoutServiceImpl.class);

    public CheckoutServiceImpl(
            UserRepository userRepository,
            CartRepository cartRepository,
            ProductRepository productRepository,
            CouponRepository couponRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {

        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }
    
    private OrderItemResponseDto mapToOrderItemResponse(OrderItem orderItem) {

        return OrderItemResponseDto.builder()
                .orderItemId(orderItem.getOrderItemId())
                .productId(orderItem.getProduct().getProductId())
                .productName(orderItem.getProduct().getProductName())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }
    
    private OrderResponseDto mapToOrderResponse(Order order) {

        List<OrderItemResponseDto> items = order.getOrderItems()
                .stream()
                .map(this::mapToOrderItemResponse)
                .toList();

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .paymentStatus(order.getPaymentStatus())
                .orderDate(order.getOrderDate())
                .orderItems(items)
                .build();
    }
    
   
    
    @Override
    @Transactional
    public OrderResponseDto checkout(CheckoutRequestDto requestDto) {

        logger.info("Checkout started for user: {}",
                requestDto.getUserId());
        
//        Find User
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> {

                    logger.error("User not found with id: {}", requestDto.getUserId());

                    return new UserNotFoundException(Constants.USER_NOT_FOUND);
                });
        
//        Find Cart
        Cart cart = cartRepository.findByUserUserId(user.getUserId())
                .orElseThrow(() -> {

                    logger.error("Cart not found for user id: {}", user.getUserId());

                    return new CartNotFoundException(Constants.CART_NOT_FOUND);
                });
        
//        Validate Cart
        if (cart.getCartItems().isEmpty()) {

            logger.warn("Checkout failed. Cart is empty for user id: {}",
                    user.getUserId());

            throw new CartNotFoundException("Cart is empty.");
        }
        
     // Validate Product Stock
        for (CartItem cartItem : cart.getCartItems()) {

            Product product = cartItem.getProduct();

            if (cartItem.getQuantity() > product.getStockQuantity()) {

                logger.warn(
                        "Insufficient stock for product: {}. Available: {}, Requested: {}",
                        product.getProductName(),
                        product.getStockQuantity(),
                        cartItem.getQuantity());

                throw new InsufficientStockException(
                        "Insufficient stock for product: "
                                + product.getProductName());
            }
        }
        
     // Calculate Total Amount
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {

            totalAmount = totalAmount.add(cartItem.getTotalPrice());
        }

        logger.info("Total cart amount: {}", totalAmount);
        
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal finalAmount = totalAmount;

        if (requestDto.getCouponCode() != null &&
                !requestDto.getCouponCode().isBlank()) {

            Coupon coupon = couponRepository
                    .findByCouponCode(requestDto.getCouponCode())
                    .orElseThrow(() -> {

                        logger.error("Coupon not found: {}",
                                requestDto.getCouponCode());

                        return new CouponNotFoundException(
                                Constants.COUPON_NOT_FOUND);
                    });

            // Check whether coupon is active
            if (!coupon.getActive()) {

                logger.warn("Coupon is inactive: {}",
                        coupon.getCouponCode());

                throw new CouponInactiveException(Constants.COUPON_INACTIVE);
            }

            // Check expiry
            if (coupon.getExpiryDate().isBefore(LocalDate.now())) {

                logger.warn("Coupon expired: {}",
                        coupon.getCouponCode());

                throw new CouponExpiredException(Constants.COUPON_EXPIRED);
            }

            // Calculate discount
            discountAmount = totalAmount.multiply(
                    coupon.getDiscountPercentage()
                            .divide(BigDecimal.valueOf(100)));

            finalAmount = totalAmount.subtract(discountAmount);

            logger.info("Coupon {} applied successfully. Discount: {}",
                    coupon.getCouponCode(),
                    discountAmount);
        }
        
     // Payment Validation
        if (!"SUCCESS".equalsIgnoreCase(requestDto.getPaymentStatus())) {

            logger.error("Payment failed for user id: {}",
                    user.getUserId());

            throw new PaymentFailedException(Constants.PAYMENT_FAILED);
        }

        logger.info("Payment successful for user id: {}",
                user.getUserId());
        
     // Reduce Product Stock
        for (CartItem cartItem : cart.getCartItems()) {

            Product product = cartItem.getProduct();

            product.setStockQuantity(
                    product.getStockQuantity() - cartItem.getQuantity());

            productRepository.save(product);

            logger.info("Stock updated for product: {}. Remaining stock: {}",
                    product.getProductName(),
                    product.getStockQuantity());
        }
        
     // Create Order
        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .paymentStatus(requestDto.getPaymentStatus())
                .orderDate(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        logger.info("Order created successfully with id: {}",
                savedOrder.getOrderId());
        
     // Create Order Items
        for (CartItem cartItem : cart.getCartItems()) {

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getUnitPrice())
                    .totalPrice(cartItem.getTotalPrice())
                    .build();

            orderItemRepository.save(orderItem);
            
            savedOrder.getOrderItems().add(orderItem);

            logger.info("Order item created for product: {}",
                    cartItem.getProduct().getProductName());
        }
        
     // Clear Cart
        cart.getCartItems().clear();

        cartRepository.save(cart);

        logger.info("Cart cleared successfully for user id: {}",
                user.getUserId());
      

        Order updatedOrder = orderRepository.findById(savedOrder.getOrderId())
                .orElseThrow(() ->
                        new OrderNotFoundException(Constants.ORDER_NOT_FOUND));

        return mapToOrderResponse(updatedOrder);
    }
    
}