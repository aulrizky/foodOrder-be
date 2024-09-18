package com.example.demo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
  @NotEmpty(message = "{username.required}")
  private String username;
  @NotEmpty(message = "{fullname.required}")
  private String fullname;
  @NotEmpty(message = "{password.required}")
  @Size(min=6,message = "{password.min_length}")
  private String password;
}
