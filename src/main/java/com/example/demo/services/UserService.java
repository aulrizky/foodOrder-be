package com.example.demo.services;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Set;



import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.hibernate.grammars.hql.HqlParser.SecondContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.model.UserDetail;
import com.example.demo.model.Users;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.jwt.JwtUtil;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class UserService {

  @Autowired
  private Validator validator;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MessageSource messageSesource;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtil jwtUtils;

  public ResponseEntity<MessageResponse> registerUser(RegisterRequest request){
    try {
      Set<ConstraintViolation<RegisterRequest>> constraintViolations = validator.validate(request);
      if(!constraintViolations.isEmpty()){
        ConstraintViolation<RegisterRequest> firstViolation = constraintViolations.iterator().next();
        String message = firstViolation.getMessage();
        return ResponseEntity.badRequest()
        .body(new MessageResponse(message,HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_GATEWAY.getReasonPhrase()));
      
      }
      if(userRepository.existsByUsername(request.getUsername())){
        String message = messageSesource.getMessage("username.is_exists",null,Locale.getDefault());
        return ResponseEntity.badRequest()
        .body(new MessageResponse(message,HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_GATEWAY.getReasonPhrase()));
      }

      Users user = Users.builder()
      .username(request.getUsername())
      .fullname(request.getFullname())
      .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
      .isDeleted(false)
      .build();

      userRepository.save(user);
      String message = messageSesource.getMessage("register.success",null,Locale.getDefault());
      String formatMessage = MessageFormat.format(message, request.getUsername(), request.getUsername());

      return ResponseEntity.ok()
      .body(new MessageResponse(formatMessage,HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase()));
    } catch (Exception e) {
      log.error(null, e);
      String message = messageSesource.getMessage("internal.error",null,Locale.getDefault());
      return ResponseEntity
      .internalServerError()
      .body(new MessageResponse(message,HttpStatus.INTERNAL_SERVER_ERROR.value(),HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }
  }
  public ResponseEntity<LoginResponse> loginUser(LoginRequest request){
    try {
      Set<ConstraintViolation<LoginRequest>> constraintViolations = validator.validate(request);
      if(!constraintViolations.isEmpty()){
        ConstraintViolation<LoginRequest> firstViolation = constraintViolations.iterator().next();
        String message = firstViolation.getMessage();
        return ResponseEntity.badRequest()
        .body(new LoginResponse(message,HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_GATEWAY.getReasonPhrase(),null));
      }
        Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetail userDetail = (UserDetail)authentication.getPrincipal();

        String message = messageSesource.getMessage("login.success",null,Locale.getDefault());

        HttpHeaders header = new HttpHeaders();
        header.add("Token", jwt);
        
        log.info(message, jwt);
        return ResponseEntity.ok()
        .body(LoginResponse.builder()
        .data(new LoginResponse.Userdata(userDetail.getUserId(),jwt,"Bearer ",userDetail.getUsername()))
        .message(message)
        .statusCode((int)HttpStatus.OK.value())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .build()
        );
    } catch (AuthenticationException e){
      String message = messageSesource.getMessage("login.error",null,Locale.getDefault());
      return ResponseEntity.badRequest()
        .body(new LoginResponse(message,HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_GATEWAY.getReasonPhrase(),null));
    }catch(Exception e) {
      log.error(null, e);
      String message = messageSesource.getMessage("internal.error",null,Locale.getDefault());
      return ResponseEntity
      .internalServerError()
      .body(new LoginResponse(message,HttpStatus.INTERNAL_SERVER_ERROR.value(),HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),null));
    }
  }
}
