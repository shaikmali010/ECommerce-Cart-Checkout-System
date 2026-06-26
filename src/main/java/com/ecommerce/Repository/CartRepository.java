package com.ecommerce.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.Model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

	Optional<Cart> findByUserUserId(Long userId);
	
	boolean existsByUserUserId(Long userId);
}
