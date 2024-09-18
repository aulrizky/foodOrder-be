package com.example.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail implements UserDetails{
  
  private int userId;
  private String username;
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  public static UserDetail build(Users user){
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("USER"));

    return new UserDetail(user.getUserId(),user.getUsername(),user.getPassword(),authorities);

  }  
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
      return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
      return true;
  }

  @Override
  public boolean isAccountNonLocked() {
      return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
      return true;
  }

  @Override
  public boolean isEnabled() {
      return true;
  }
}
