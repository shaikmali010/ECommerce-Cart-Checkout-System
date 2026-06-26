package com.ecommerce.Controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.DTO.CouponRequestDto;
import com.ecommerce.DTO.CouponResponseDto;
import com.ecommerce.Service.CouponService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/coupons")
@Validated
@Tag(
        name = "Coupon APIs",
        description = "APIs for managing coupons"
)
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    @Operation(
            summary = "Create Coupon",
            description = "Creates a new coupon."
    )
    public ResponseEntity<CouponResponseDto> createCoupon(
            @Valid @RequestBody CouponRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(couponService.createCoupon(requestDto));
    }

    @GetMapping("/{couponId}")
    @Operation(
            summary = "Get Coupon By Id",
            description = "Fetch coupon details using coupon id."
    )
    public ResponseEntity<CouponResponseDto> getCouponById(
            @PathVariable Long couponId) {

        return ResponseEntity.ok(
                couponService.getCouponById(couponId));
    }

    @GetMapping("/code/{couponCode}")
    @Operation(
            summary = "Get Coupon By Code",
            description = "Fetch coupon details using coupon code."
    )
    public ResponseEntity<CouponResponseDto> getCouponByCode(
            @PathVariable String couponCode) {

        return ResponseEntity.ok(
                couponService.getCouponByCode(couponCode));
    }

    @GetMapping
    @Operation(
            summary = "Get All Coupons",
            description = "Retrieve all coupons with paging and sorting."
    )
    public ResponseEntity<Page<CouponResponseDto>> getAllCoupons(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "couponId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(
                couponService.getAllCoupons(
                        page,
                        size,
                        sortBy,
                        sortDir));
    }

    @PutMapping("/{couponId}")
    @Operation(
            summary = "Update Coupon",
            description = "Update an existing coupon."
    )
    public ResponseEntity<CouponResponseDto> updateCoupon(
            @PathVariable Long couponId,
            @Valid @RequestBody CouponRequestDto requestDto) {

        return ResponseEntity.ok(
                couponService.updateCoupon(couponId, requestDto));
    }

    @DeleteMapping("/{couponId}")
    @Operation(
            summary = "Delete Coupon",
            description = "Delete a coupon using coupon id."
    )
    public ResponseEntity<String> deleteCoupon(
            @PathVariable Long couponId) {

        couponService.deleteCoupon(couponId);

        return ResponseEntity.ok("Coupon deleted successfully.");
    }
}