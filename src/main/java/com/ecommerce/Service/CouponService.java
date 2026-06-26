package com.ecommerce.Service;

import org.springframework.data.domain.Page;

import com.ecommerce.DTO.CouponRequestDto;
import com.ecommerce.DTO.CouponResponseDto;

public interface CouponService {

    CouponResponseDto createCoupon(CouponRequestDto requestDto);

    CouponResponseDto getCouponById(Long couponId);

    CouponResponseDto getCouponByCode(String couponCode);

    Page<CouponResponseDto> getAllCoupons(
            int page,
            int size,
            String sortBy,
            String sortDir);

    CouponResponseDto updateCoupon(
            Long couponId,
            CouponRequestDto requestDto);

    void deleteCoupon(Long couponId);

}
