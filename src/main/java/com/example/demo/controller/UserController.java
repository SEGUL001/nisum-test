package com.example.demo.controller;

import com.example.demo.api.UserRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<List<User>> findUsers(){
        return ResponseEntity.ok(userService.findUsers());
    }

    @PostMapping
    ResponseEntity<User> createUser(@RequestBody @Valid UserRequest userRequest){
        return ResponseEntity.ok(userService.createUser(userRequest));
    }
}
