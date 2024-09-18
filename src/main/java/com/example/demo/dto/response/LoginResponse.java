package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
  private String message;

  private int statusCode;

  private String status;

  private Userdata data;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Userdata{
    private int id;
    private String token;
    private String type;
    private String username;
  }
}
