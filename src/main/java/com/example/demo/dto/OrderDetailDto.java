package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
  private Integer orderDetailid;
  private String foodName;
  private String imageFileName;
  private Integer quantity;
  private Integer price;
  private Integer totalHarga;

}
