package com.example.demo.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CartResponse {
  private long total;
  private FoodListResponseDto data;
  private String message;
  private Integer statusCode;
  private String status;
}
