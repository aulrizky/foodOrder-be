package com.example.demo.controllers;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/user-management")
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping("/users/sign-up")
  public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
    return userService.registerUser(request);
  }

  @PostMapping("/users/sign-in")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    return userService.loginUser(request);
  }

}
