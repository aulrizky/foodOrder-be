package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodResponse {
  private FoodListResponseDto data;
  private String message;
  private Integer statusCode;
  private String status;
}
