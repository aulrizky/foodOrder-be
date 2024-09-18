package com.example.demo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
  @NotEmpty(message = "{username.required}")
  private String username;
  @NotEmpty(message = "{password.required}")
  private String password;
}
