package com.example.demo.dto;

import java.sql.Date;
// import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
  private Integer orderId;
  private Integer totalItem;
  private String tanggalOrder;
  private Integer totalOrder;
  private List<OrderDetailDto> orderDetails;
}
