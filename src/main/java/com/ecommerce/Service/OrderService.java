package com.ecommerce.Service;

import org.springframework.data.domain.Page;

import com.ecommerce.DTO.OrderResponseDto;

public interface OrderService {

    OrderResponseDto getOrderById(Long orderId);

    Page<OrderResponseDto> getOrdersByUserId(
            Long userId,
            int page,
            int size,
            String sortBy,
            String sortDir);

    Page<OrderResponseDto> getAllOrders(
            int page,
            int size,
            String sortBy,
            String sortDir);
}