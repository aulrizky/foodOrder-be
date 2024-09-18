package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import com.example.demo.model.UserDetail;
import com.example.demo.model.Users;
import com.example.demo.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserDetailService implements UserDetailsService{
  
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
    Users user = userRepository.findByUsername(username).get();
    return UserDetail.build(user);
  }
}
