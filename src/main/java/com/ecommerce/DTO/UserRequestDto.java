package com.ecommerce.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequestDto {
	
	@NotBlank(message = "Name is required")
	@Size(min = 3, max = 50, message = "Name must be batween 3 and 50 characters")
	@Pattern(regexp = "^[A-Za-z ]+$",
				message = "Name must contain only letters and spaces")
	private String userName;
	
	@NotBlank(message = "Email is required")
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
	         message = "Invalid email formate")
	private String email;
	
	@NotNull(message = "Password is required")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$",
	         message = "Password must contain uppercase, lowercase, number, special character and be 8 - 20 character long")
	private String password;

	public UserRequestDto() {}
	
	public UserRequestDto(
			 String userName,
			 String email,
			 String password) {
		
		this.userName = userName;
		this.email = email;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
