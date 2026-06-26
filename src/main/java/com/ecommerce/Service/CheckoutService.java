package com.ecommerce.Service;

import com.ecommerce.DTO.CheckoutRequestDto;
import com.ecommerce.DTO.OrderResponseDto;

public interface CheckoutService {

    OrderResponseDto checkout(CheckoutRequestDto requestDto);

}
