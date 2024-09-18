package com.example.demo.dto.response;

import com.example.demo.dto.FoodCategoryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodListResponseDto {
  private int foodId;
  private FoodCategoryDto categories;
  private String foodName;
  private Integer price;
  private String imageFilename;
  private Boolean isFavorite;
  private Boolean isCartDelete;

}
