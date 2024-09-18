package com.example.demo.dto;


import java.sql.Timestamp;
import java.util.List;

import com.example.demo.dto.request.FoodListRequestDto;
import com.example.demo.dto.response.FoodListResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
  private Integer cartId;
  private Integer userId;
  private String username;
  private Integer qty;
  private Boolean isDelete;
  private String createdBy;
  private Timestamp createdTime;
  private String modififiedBy;
  private Timestamp modifiedTime;
  private FoodListResponseDto foods;
}
