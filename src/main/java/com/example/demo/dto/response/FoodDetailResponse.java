package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodDetailResponse {
  private FoodListDetailDto data;
  private String message;
  private Integer statusCode;
  private String status;
}
