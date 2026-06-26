package com.ecommerce.ServiceImpl;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.Constants.Constants;
import com.ecommerce.DTO.UserRequestDto;
import com.ecommerce.DTO.UserResponseDto;
import com.ecommerce.DTO.UserUpdateDto;
import com.ecommerce.Exceptions.UserAlreadyExistsException;
import com.ecommerce.Exceptions.UserNotFoundException;
import com.ecommerce.Model.User;
import com.ecommerce.Repository.UserRepository;
import com.ecommerce.Service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;
	
	private static final Logger logger = LoggerFactory.getLogger(
			UserServiceImpl.class);
	
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	private UserResponseDto mapToResponse(User user) {
		
		return UserResponseDto.builder()
				.userId(user.getUserId())
				.userName(user.getUserName())
				.email(user.getEmail())
				.build();
	}
	
	private User mapToEntity(UserRequestDto requestDto) {
		
		return User.builder()
				.userName(requestDto.getUserName())
				.email(requestDto.getEmail())
				.password(passwordEncoder.encode(requestDto.getPassword()))
				.build();
	}
	
	@Override
	public UserResponseDto createUser(
			UserRequestDto requestDto) {
		
		  logger.info("Registering user with email: {}", requestDto.getEmail());
		
		if(userRepository.existsByEmail(requestDto.getEmail())) {
			
			logger.warn("Registration failed. Email already exists: {}", requestDto.getEmail());
			
			throw new UserAlreadyExistsException(Constants.USER_EXISTS);
		}
		
		User user = mapToEntity(requestDto);
		
		User savedUser = userRepository.save(user);
		
		logger.info("User registered successfully with id: {}", savedUser.getUserId());
		
		return mapToResponse(savedUser);
	}
	
	@Override
	public UserResponseDto getUserById(Long userId) {
		
		logger.info("Fetching user details by userId: {}", userId);
		
		User user = userRepository.findById(userId)
				   .orElseThrow(() -> {
					   
					   logger.error("user not found with id: {}",userId);
					   
					   return new UserNotFoundException(Constants.USER_NOT_FOUND);
				   });
		
		logger.info("User details fetched successfully with id: {} ", user.getUserId());
		
		return mapToResponse(user);
	}
	
	@Override
	public Page<UserResponseDto> getAllUser(int page, int size, String sortBy, String sortDir){
		
		logger.info("Fetching all customers data");
		
		Sort sort = sortDir.equalsIgnoreCase("asc")
				    ? Sort.by(sortBy).ascending()
				    : Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(page, size, sort);
		
		logger.info("Fetching all the user data in page no: {}, page size: {}, sortBy: {}, sortDir: {}",
				page, size, sortBy, sortDir);
		
		Page<User> user = userRepository.findAll(pageable);
		
		logger.info("Total customer found: {}", user.getTotalElements());
		
		return user.map(this::mapToResponse);
	}
	
	@Override
	public UserResponseDto getUserByEmail(String email) {
		
		logger.info("Fetching user details by email {}",email);
		
		User user = userRepository.findByEmail(email).orElseThrow( () ->{
			
			logger.error("User with {} Does not exists ",email);
			
			return new UserNotFoundException(Constants.USER_NOT_FOUND);
		});
		
		logger.info("User fetched successfully with email {}", email);
		
		return mapToResponse(user);
	}
	
	@Override
	public UserResponseDto updateUserById(Long userId, UserUpdateDto dto) {
		
		logger.info("Updating the user by id  {}", userId);
		
		User user = userRepository.findById(userId).orElseThrow(() -> {
			
			logger.error("User not found with the id {} ", userId);
			
			return new UserNotFoundException(Constants.USER_NOT_FOUND);
		});
		
		if(dto.getEmail() != null &&
				!dto.getEmail().equals(user.getEmail())
				&& userRepository.existsByEmail(dto.getEmail())) {
			
			logger.warn("Email already exists: {}", dto.getEmail());
			
			throw new UserAlreadyExistsException(Constants.USER_EXISTS);
		}
		
		    user.setUserName(dto.getUserName());
	
		    user.setEmail(dto.getEmail());
		
		User savedUser = userRepository.save(user);
		
		logger.info("User updated successfully with id {}", savedUser.getUserId());
		
		return mapToResponse(savedUser);
	}
	
	@Override
	public void deleteUserById(final Long userId) {

	    logger.info("Deleting user with id: {}", userId);

	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> {

	                logger.error("User not found with id: {}", userId);

	                return new UserNotFoundException(Constants.USER_NOT_FOUND);
	            });

	    userRepository.delete(user);

	    logger.info("User deleted successfully with id: {}", userId);
	}

}
