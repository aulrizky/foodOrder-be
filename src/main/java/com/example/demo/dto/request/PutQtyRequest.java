package com.example.demo.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutQtyRequest {
  private int qty;
}
