package com.ecommerce.ServiceImpl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ecommerce.DTO.CheckoutRequestDto;
import com.ecommerce.DTO.OrderResponseDto;
import com.ecommerce.Exceptions.CartNotFoundException;
import com.ecommerce.Exceptions.CouponExpiredException;
import com.ecommerce.Exceptions.CouponInactiveException;
import com.ecommerce.Exceptions.CouponNotFoundException;
import com.ecommerce.Exceptions.InsufficientStockException;
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

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTest {

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;
    
  
    
    @Test
    void shouldThrowUserNotFoundException() {

        CheckoutRequestDto request = new CheckoutRequestDto();
        request.setUserId(1L);
        request.setPaymentStatus("SUCCESS");

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> checkoutService.checkout(request));

        verify(userRepository).findById(1L);
    }
    
    @Test
    void shouldThrowCartNotFoundException() {

        CheckoutRequestDto request = new CheckoutRequestDto();
        request.setUserId(1L);
        request.setPaymentStatus("SUCCESS");

        User user = new User();
        user.setUserId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserUserId(1L))
                .thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class,
                () -> checkoutService.checkout(request));

        verify(cartRepository).findByUserUserId(1L);
    }
    
    @Test
    void shouldThrowExceptionWhenCartIsEmpty() {

        CheckoutRequestDto request = new CheckoutRequestDto();
        request.setUserId(1L);
        request.setPaymentStatus("SUCCESS");

        User user = new User();
        user.setUserId(1L);

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserUserId(1L))
                .thenReturn(Optional.of(cart));

        assertThrows(CartNotFoundException.class,
                () -> checkoutService.checkout(request));
    }
    
    @Test
    void shouldThrowCouponNotFoundException() {

        CheckoutRequestDto request = new CheckoutRequestDto();
        request.setUserId(1L);
        request.setCouponCode("WELCOME10");
        request.setPaymentStatus("SUCCESS");

        User user = new User();
        user.setUserId(1L);

        Product product = new Product();
        product.setStockQuantity(10);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setUnitPrice(BigDecimal.valueOf(1000));
        item.setTotalPrice(BigDecimal.valueOf(2000));

        Cart cart = new Cart();
        cart.setCartItems(List.of(item));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserUserId(1L))
                .thenReturn(Optional.of(cart));

        when(couponRepository.findByCouponCode("WELCOME10"))
                .thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class,
                () -> checkoutService.checkout(request));
    }
    
    @Test
    void shouldThrowPaymentFailedException() {

        CheckoutRequestDto request = new CheckoutRequestDto();
        request.setUserId(1L);
        request.setPaymentStatus("FAILED");

        User user = new User();
        user.setUserId(1L);

        Product product = new Product();
        product.setStockQuantity(10);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setUnitPrice(BigDecimal.valueOf(1000));
        item.setTotalPrice(BigDecimal.valueOf(2000));

        Cart cart = new Cart();
        cart.setCartItems(List.of(item));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserUserId(1L))
                .thenReturn(Optional.of(cart));

        assertThrows(PaymentFailedException.class,
                () -> checkoutService.checkout(request));
    }
    
    @Test
    void shouldThrowInsufficientStockException() {

        CheckoutRequestDto request = new CheckoutRequestDto();
        request.setUserId(1L);
        request.setPaymentStatus("SUCCESS");

        Product product = new Product();
        product.setStockQuantity(5);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(10);
        cartItem.setProduct(product);

        Cart cart = new Cart();
        cart.setCartItems(List.of(cartItem));

        User user = new User();
        user.setUserId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserUserId(1L))
                .thenReturn(Optional.of(cart));

        assertThrows(InsufficientStockException.class,
                () -> checkoutService.checkout(request));
    }
    
    @Test
    void shouldCheckoutSuccessfully() {

        // Request
        CheckoutRequestDto request = new CheckoutRequestDto();
        request.setUserId(1L);
        request.setPaymentStatus("SUCCESS");

        // User
        User user = new User();
        user.setUserId(1L);

        // Product
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Dell Laptop");
        product.setPrice(BigDecimal.valueOf(50000));
        product.setStockQuantity(10);

        // Cart Item
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setUnitPrice(BigDecimal.valueOf(50000));
        cartItem.setTotalPrice(BigDecimal.valueOf(100000));

        // Cart
        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>(List.of(cartItem)));

        // Order
        Order savedOrder = Order.builder()
                .orderId(1L)
                .user(user)
                .totalAmount(BigDecimal.valueOf(100000))
                .discountAmount(BigDecimal.ZERO)
                .finalAmount(BigDecimal.valueOf(100000))
                .paymentStatus("SUCCESS")
                .orderDate(LocalDateTime.now())
                .build();

        // Mock Repository Calls
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserUserId(1L))
                .thenReturn(Optional.of(cart));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(savedOrder));

        // Execute
        OrderResponseDto response = checkoutService.checkout(request);

        // Assertions
        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals(1L, response.getUserId());
        assertEquals("SUCCESS", response.getPaymentStatus());
        assertEquals(BigDecimal.valueOf(100000), response.getTotalAmount());
        assertEquals(BigDecimal.valueOf(100000), response.getFinalAmount());

        verify(orderRepository, times(1)).save(any(Order.class));

        verify(orderItemRepository, times(1))
                .save(any(OrderItem.class));

        verify(productRepository, times(1))
                .save(any(Product.class));

        verify(cartRepository, times(1))
                .save(any(Cart.class));
        verify(userRepository).findById(1L);
        verify(cartRepository).findByUserUserId(1L);
    }
    
    @Test
    void shouldThrowCouponInactiveException() {

        CheckoutRequestDto request = new CheckoutRequestDto();
        request.setUserId(1L);
        request.setCouponCode("WELCOME10");
        request.setPaymentStatus("SUCCESS");

        User user = new User();
        user.setUserId(1L);

        Product product = new Product();
        product.setStockQuantity(10);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setTotalPrice(BigDecimal.valueOf(1000));

        Cart cart = new Cart();
        cart.setCartItems(List.of(cartItem));

        Coupon coupon = new Coupon();
        coupon.setCouponCode("WELCOME10");
        coupon.setActive(false);          // Inactive coupon
        coupon.setExpiryDate(LocalDate.now().plusDays(5));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserUserId(1L))
                .thenReturn(Optional.of(cart));

        when(couponRepository.findByCouponCode("WELCOME10"))
                .thenReturn(Optional.of(coupon));

        assertThrows(CouponInactiveException.class,
                () -> checkoutService.checkout(request));
    }
    
    @Test
    void shouldThrowCouponExpiredException() {

        CheckoutRequestDto request = new CheckoutRequestDto();
        request.setUserId(1L);
        request.setCouponCode("WELCOME10");
        request.setPaymentStatus("SUCCESS");

        User user = new User();
        user.setUserId(1L);

        Product product = new Product();
        product.setStockQuantity(10);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setTotalPrice(BigDecimal.valueOf(1000));

        Cart cart = new Cart();
        cart.setCartItems(List.of(cartItem));

        Coupon coupon = new Coupon();
        coupon.setCouponCode("WELCOME10");
        coupon.setActive(true);
        coupon.setExpiryDate(LocalDate.now().minusDays(1)); // Expired

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cartRepository.findByUserUserId(1L))
                .thenReturn(Optional.of(cart));

        when(couponRepository.findByCouponCode("WELCOME10"))
                .thenReturn(Optional.of(coupon));

        assertThrows(CouponExpiredException.class,
                () -> checkoutService.checkout(request));
    }
}