package com.example.demo;

import com.example.demo.api.PhoneRequest;
import com.example.demo.api.UserRequest;
import com.example.demo.model.Phone;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenUtil;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class DemoApplicationTests {

	private UserService userService;
	private UserRepository userRepository;
	private JwtTokenUtil jwtTokenUtil;
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	private void init(){
		userRepository = Mockito.mock(UserRepository.class);
		jwtTokenUtil = Mockito.mock(JwtTokenUtil.class);
		passwordEncoder = Mockito.mock(PasswordEncoder.class);
		this.userService = new UserService(userRepository, jwtTokenUtil, passwordEncoder);
	}


	@Test
	public void testCreateUser(){

		UserRequest userRequest = UserRequest.builder()
				.email("test@test.com")
				.name("name")
				.password("12Ee")
				.phones(Collections.singletonList(PhoneRequest.builder()
						.cityCode("291")
						.number("32323")
						.countryCode("52")
						.build()))
				.build();


		Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
		Mockito.when(userRepository.save(any(User.class))).thenReturn(createUser(userRequest));

		try{
			userService.createUser(userRequest);
		} catch (Exception e){
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testAlreadyUsedEmail(){

		UserRequest userRequest = UserRequest.builder()
				.email("test@test.com")
				.name("name")
				.password("12Ee")
				.phones(Collections.singletonList(PhoneRequest.builder()
						.cityCode("291")
						.number("32323")
						.countryCode("52")
						.build()))
				.build();


		Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(createUser(userRequest)));
		Mockito.when(userRepository.save(any(User.class))).thenReturn(createUser(userRequest));

		Exception exception = assertThrows(RuntimeException.class, () -> {
			userService.createUser(userRequest);
		});

		String expectedMessage = "Email Already in Use!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}


	public User createUser(UserRequest userRequest){

		User savedUser = User.builder()
				.email(userRequest.getEmail())
				.name(userRequest.getName())
				.password(userRequest.getPassword())
				.build();

		savedUser.setPhones(userRequest.getPhones().stream().map(phoneRequest -> Phone.builder()
				.user(savedUser)
				.cityCode(phoneRequest.getCityCode())
				.number(phoneRequest.getNumber())
				.countryCode(phoneRequest.getCountryCode())
				.build()).collect(Collectors.toList()));

		return savedUser;
	}

}
