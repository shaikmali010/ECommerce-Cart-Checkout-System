package com.ecommerce.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateDto {

	@NotBlank(message = "User name is required")
	@Size(min = 3, max = 50, message = "User name must be between 3 and 50 characters")
	@Pattern(
	    regexp = "^[A-Za-z ]+$",
	    message = "User name must contain only letters and spaces"
	)
	private String userName;
	
	@NotBlank(message = "Email is required")
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
				message = "Invalid email formate")
	private String email;

	
	public UserUpdateDto() {}
	
	public UserUpdateDto(String userName,
			String email ) {

		this.userName = userName;
		this.email = email; 
	}

	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}

