package com.example.demo.dto.response;
import java.util.List;

import com.example.demo.dto.CartDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyCartResponse {
  private List <CartDto> data;
  private String message;
  private Integer statusCode;
  private String status;
}
