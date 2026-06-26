package com.ecommerce.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		http
		     .csrf(csrf -> csrf.disable())
		     .authorizeHttpRequests(auth -> auth.requestMatchers(
		    		 "/swagger-ui/**",
		    		 "/v3/api-docs/**",
		    		 "/h2-console/**",
		    		 "/api/users/**",
		    		 "/api/products/**",
		    		 "/api/cart/**",
		    		 "/api/orders/**",
		    		 "/api/coupons/**",
		    		 "/api/checkout/**")
		    		 .permitAll()
		    		 .anyRequest().authenticated())
		     .headers(headers -> headers.frameOptions(frame -> frame.disable()))
		     .httpBasic(Customizer.withDefaults());
		
		return http.build();
	}
}
