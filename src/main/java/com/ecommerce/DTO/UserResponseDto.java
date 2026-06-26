package com.ecommerce.DTO;

import lombok.Builder;

@Builder
public class UserResponseDto {

	private Long userId;
	private String userName;
	private String email;
	
	public UserResponseDto() {}
	
	public UserResponseDto(Long userId, String userName, 
			String email) {
		this.userId = userId;
		this.userName = userName;
		this.email = email;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
