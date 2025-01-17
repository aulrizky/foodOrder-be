package com.example.demo.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodListResponse {
  private long total;
  private List<FoodListResponseDto> data;
  private String message;
  private Integer statusCode;
  private String status;

}
