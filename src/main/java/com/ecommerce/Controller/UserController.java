package com.ecommerce.Controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.DTO.UserRequestDto;
import com.ecommerce.DTO.UserResponseDto;
import com.ecommerce.DTO.UserUpdateDto;
import com.ecommerce.Service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Validated
@Tag(name = "User APIs",
      description = "APIs for managing user registration and user operations")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping
	@Operation(
		    summary = "Register a new user",
		    description = "Creates a new user account after validating the request and ensuring the email is unique."
		)
	public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto requestDto){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(userService.createUser(requestDto));
		
	}
	
	@GetMapping("/{userId}")
	@Operation(
		    summary = "Get user by ID",
		    description = "Retrieves the details of a specific user using the user ID."
		)
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
		return ResponseEntity.ok(userService.getUserById(userId));
	}
	
	@GetMapping
	@Operation(
		    summary = "Get all users",
		    description = "Retrieves all users with pagination and sorting support."
		)
	public ResponseEntity<Page<UserResponseDto>> getAllUser(
			@RequestParam(defaultValue = "0")int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "userId") String sortBy, 
			@RequestParam(defaultValue = "asc") String sortDir){
		return ResponseEntity.ok(userService.getAllUser(page, size, sortBy, sortDir));
	}
	
	@GetMapping("/email/{email}")
	@Operation(
		    summary = "Get user by email",
		    description = "Retrieves user details using the registered email address."
		)
	public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
		return ResponseEntity.ok(userService.getUserByEmail(email));
	}
	
	@PutMapping("/{userId}")
	@Operation(
		    summary = "Update user",
		    description = "Updates the user's name and email based on the provided user ID."
		)
	public ResponseEntity<UserResponseDto> updateUserById(@PathVariable Long userId, 
			@Valid @RequestBody UserUpdateDto dto) {
		return ResponseEntity.ok(userService.updateUserById(userId, dto));
	}
	
	@DeleteMapping("/{userId}")
	@Operation(
		    summary = "Delete user",
		    description = "Deletes the user associated with the given user ID."
		)
	public ResponseEntity<String> deleteUserById(@PathVariable Long userId){
		userService.deleteUserById(userId);
		return ResponseEntity.ok("The user deleted successfully");
	}
	

}
