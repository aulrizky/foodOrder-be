package com.example.demo.dto.response;
import java.util.List;

import com.example.demo.dto.OrderDto;
import com.example.demo.dto.PaginationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryResponse {
  private long total ;
  private List<OrderDto> data;
  private String message;
  private Integer statusCode;
  private String status;
  private PaginationDto pagination;
}
