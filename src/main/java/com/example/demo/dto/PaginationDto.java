package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto {
  private Integer currentPage; 
  private Integer  totalPages;
  private Integer  pageSize;
  private Integer  startIndex; 
  private Integer  endIndex; 
}
