package com.ecommerce.ServiceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommerce.Constants.Constants;
import com.ecommerce.DTO.OrderItemResponseDto;
import com.ecommerce.DTO.OrderResponseDto;
import com.ecommerce.Exceptions.OrderNotFoundException;
import com.ecommerce.Model.Order;
import com.ecommerce.Model.OrderItem;
import com.ecommerce.Repository.OrderRepository;
import com.ecommerce.Service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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

        List<OrderItemResponseDto> orderItems = order.getOrderItems()
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
                .orderItems(orderItems)
                .build();
    }
    
    @Override
    public OrderResponseDto getOrderById(Long orderId) {

        logger.info("Fetching order with id: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {

                    logger.error("Order not found with id: {}", orderId);

                    return new OrderNotFoundException(Constants.ORDER_NOT_FOUND);
                });

        logger.info("Order fetched successfully with id: {}", orderId);

        return mapToOrderResponse(order);
    }
    
    @Override
    public Page<OrderResponseDto> getOrdersByUserId(
            Long userId,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        logger.info("Fetching orders for user id: {}", userId);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findByUserUserId(userId, pageable);

        logger.info("Orders fetched successfully for user id: {}", userId);

        return orders.map(this::mapToOrderResponse);
    }
    
    @Override
    public Page<OrderResponseDto> getAllOrders(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        logger.info("Fetching all orders.");

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Order> orders = orderRepository.findAll(pageable);

        logger.info("Total orders found: {}", orders.getTotalElements());

        return orders.map(this::mapToOrderResponse);
    }
}