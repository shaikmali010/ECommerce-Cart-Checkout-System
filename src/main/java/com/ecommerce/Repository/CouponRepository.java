package com.ecommerce.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.Model.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long>{

	Optional<Coupon> findByCouponCode(String couponCode);

    boolean existsByCouponCode(String couponCode);
}
