package com.ecommerce.ServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommerce.Constants.Constants;
import com.ecommerce.DTO.CouponRequestDto;
import com.ecommerce.DTO.CouponResponseDto;
import com.ecommerce.Exceptions.CouponAlreadyExistsException;
import com.ecommerce.Exceptions.CouponNotFoundException;
import com.ecommerce.Model.Coupon;
import com.ecommerce.Repository.CouponRepository;
import com.ecommerce.Service.CouponService;

import jakarta.transaction.Transactional;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(CouponServiceImpl.class);

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    private CouponResponseDto mapToResponse(Coupon coupon) {

        return CouponResponseDto.builder()
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .discountPercentage(coupon.getDiscountPercentage())
                .expiryDate(coupon.getExpiryDate())
                .active(coupon.getActive())
                .build();
    }

    private Coupon mapToEntity(CouponRequestDto requestDto) {

        return Coupon.builder()
                .couponCode(requestDto.getCouponCode())
                .discountPercentage(requestDto.getDiscountPercentage())
                .expiryDate(requestDto.getExpiryDate())
                .active(requestDto.getActive())
                .build();
    }
 
    @Override
    @Transactional
    public CouponResponseDto createCoupon(CouponRequestDto requestDto) {

        logger.info("Creating coupon with code: {}", requestDto.getCouponCode());

        if (couponRepository.existsByCouponCode(requestDto.getCouponCode())) {

            logger.warn("Coupon already exists: {}", requestDto.getCouponCode());

            throw new CouponAlreadyExistsException(
                    Constants.COUPON_ALREADY_EXISTS);
        }

        Coupon coupon = mapToEntity(requestDto);

        Coupon savedCoupon = couponRepository.save(coupon);

        logger.info("Coupon created successfully with id: {}",
                savedCoupon.getCouponId());

        return mapToResponse(savedCoupon);
    }
    
    @Override
    public CouponResponseDto getCouponById(Long couponId) {

        logger.info("Fetching coupon with id: {}", couponId);

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> {

                    logger.error("Coupon not found with id: {}", couponId);

                    return new CouponNotFoundException(Constants.COUPON_NOT_FOUND);
                });

        logger.info("Coupon fetched successfully with id: {}", couponId);

        return mapToResponse(coupon);
    }
    
    @Override
    public CouponResponseDto getCouponByCode(String couponCode) {

        logger.info("Fetching coupon with code: {}", couponCode);

        Coupon coupon = couponRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> {

                    logger.error("Coupon not found with code: {}", couponCode);

                    return new CouponNotFoundException(Constants.COUPON_NOT_FOUND);
                });

        logger.info("Coupon fetched successfully with code: {}", couponCode);

        return mapToResponse(coupon);
    }
    
    @Override
    public Page<CouponResponseDto> getAllCoupons(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        logger.info("Fetching all coupons.");

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        logger.info(
                "Fetching coupons - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Page<Coupon> coupons = couponRepository.findAll(pageable);

        logger.info("Total coupons found: {}", coupons.getTotalElements());

        return coupons.map(this::mapToResponse);
    }
    
    @Override
    @Transactional
    public CouponResponseDto updateCoupon(Long couponId,
            CouponRequestDto requestDto) {

        logger.info("Updating coupon with id: {}", couponId);

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> {

                    logger.error("Coupon not found with id: {}", couponId);

                    return new CouponNotFoundException(Constants.COUPON_NOT_FOUND);
                });

        if (requestDto.getCouponCode() != null
                && !requestDto.getCouponCode().equals(coupon.getCouponCode())
                && couponRepository.existsByCouponCode(requestDto.getCouponCode())) {

            logger.warn("Coupon code already exists: {}",
                    requestDto.getCouponCode());

            throw new CouponAlreadyExistsException(
                    Constants.COUPON_ALREADY_EXISTS);
        }

        coupon.setCouponCode(requestDto.getCouponCode());
        coupon.setDiscountPercentage(requestDto.getDiscountPercentage());
        coupon.setExpiryDate(requestDto.getExpiryDate());
        coupon.setActive(requestDto.getActive());

        Coupon updatedCoupon = couponRepository.save(coupon);

        logger.info("Coupon updated successfully with id: {}",
                updatedCoupon.getCouponId());

        return mapToResponse(updatedCoupon);
    }
    
    @Override
    @Transactional
    public void deleteCoupon(Long couponId) {

        logger.info("Deleting coupon with id: {}", couponId);

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> {

                    logger.error("Coupon not found with id: {}", couponId);

                    return new CouponNotFoundException(Constants.COUPON_NOT_FOUND);
                });

        couponRepository.delete(coupon);

        logger.info("Coupon deleted successfully with id: {}", couponId);
    }
    
}
