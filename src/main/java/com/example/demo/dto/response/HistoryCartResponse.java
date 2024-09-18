package com.example.demo.dto.response;
import java.util.List;

import com.example.demo.dto.PaginationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class HistoryCartResponse {
  private long total;
  private List<FoodListResponseDto> data;
  private String message;
  private Integer statusCode;
  private String status;
  private PaginationDto pagination;
}
