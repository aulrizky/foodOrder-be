package com.example.demo.services.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.dto.request.FoodListRequestDto;
import com.example.demo.model.Foods;

import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FoodSpecification {
  public static Specification<Foods> foodFilter(FoodListRequestDto foodListRequestDto) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<Predicate>();

      if (foodListRequestDto.getFoodName() != null) {
        String foodNameValue = "%" + foodListRequestDto.getFoodName().toLowerCase() + "%";
        Predicate foodNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("foodName")), foodNameValue);
        predicates.add(foodNamePredicate);

        log.info("Added foodNamePredicate: {}", foodNameValue);
      }

      if (foodListRequestDto.getCategoryId() != null) {
        // String foodCategoryValue = "%" + foodListRequestDto.getCategoryId()+"%";
        Predicate foodCategoryPredicate = criteriaBuilder.equal(root.get("categories").get("categoryId"),
            foodListRequestDto.getCategoryId());
        predicates.add(foodCategoryPredicate);
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

    };
  }
}
