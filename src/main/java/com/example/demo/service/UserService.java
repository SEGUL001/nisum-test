package com.example.demo.service;

import com.example.demo.api.UserRequest;
import com.example.demo.exception.AppException;
import com.example.demo.model.Phone;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    public List<User> findUsers(){
        return userRepository.findAll();
    }

    public User createUser(UserRequest userRequest){

       Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
        if(existingUser.isPresent()){
            throw new AppException("Email Already in Use!", logger);
        }

        User user = User.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .name(userRequest.getName())
                .isActive(true)
                .lastLogin(new Date())
                .token(jwtTokenUtil.doGenerateToken(userRequest.getEmail()))
                .build();
        userRequest.getPhones().forEach(phone -> user.getPhones().add(Phone.builder()
                .cityCode(phone.getCityCode())
                .user(user)
                .countryCode(phone.getCountryCode())
                .number(phone.getNumber())
                .build()));
        return userRepository.save(user);
    }

}
