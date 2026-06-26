package com.ecommerce.Service;

import org.springframework.data.domain.Page;
import com.ecommerce.DTO.UserRequestDto;
import com.ecommerce.DTO.UserResponseDto;
import com.ecommerce.DTO.UserUpdateDto;

public interface UserService {

	public UserResponseDto createUser(UserRequestDto requestDto);
	
	public UserResponseDto getUserById(Long userId);
	
	public UserResponseDto getUserByEmail(String email);
	
	public Page<UserResponseDto> getAllUser(int page, int size, String sortBy, String sortDir);
	
	public UserResponseDto updateUserById(Long userId, UserUpdateDto dto);
	
	public void deleteUserById(Long userId);
	
}
