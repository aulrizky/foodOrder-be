package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.CartRequest;
import com.example.demo.dto.request.CheckoutCartIdRequest;
import com.example.demo.dto.request.CheckoutCartRequest;
import com.example.demo.dto.request.FoodListRequestDto;
import com.example.demo.dto.request.PageRequest;
import com.example.demo.dto.request.PutQtyRequest;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.dto.response.FoodDetailResponse;
import com.example.demo.dto.response.FoodListResponse;
import com.example.demo.dto.response.FoodResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.dto.response.MyCartResponse;
import com.example.demo.dto.response.OrderHistoryResponse;
import com.example.demo.services.FoodService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/food-order")
public class FoodController {
  @Autowired
  private FoodService foodService;

  @GetMapping("/foods")
  public ResponseEntity<FoodListResponse> getFoods(
      @ModelAttribute FoodListRequestDto foodListRequestDto,
      PageRequest pageRequest) {
    return foodService.getFoods(foodListRequestDto, pageRequest.getPage());
  }

  @PutMapping("/foods/{foodId}/favorites")
  public ResponseEntity<FoodListResponse> toggleFavorites(@PathVariable Integer foodId) {
    return foodService.toggleFavorite(foodId);
  }

  @PostMapping("/cart")
  public ResponseEntity<CartResponse> addtoCart(@RequestBody CartRequest request) {
    return foodService.addToCart(request);
  }

  @DeleteMapping("/cart/{foodId}")
  public ResponseEntity<CartResponse> deleteCart(@PathVariable Integer foodId) {
    return foodService.deleteCart(foodId);
  }

  @GetMapping("/foods/{foodId}")
  public ResponseEntity<FoodDetailResponse> getFoodById(@PathVariable Integer foodId) {
    return foodService.getFoodsById(foodId);
  }

  @GetMapping("/history")
  public ResponseEntity<OrderHistoryResponse> getHistoryOder(
      @PageableDefault(page = 0, size = 5) Pageable pageRequest) {
    return foodService.getHistoryOrderResponse(pageRequest);
  }

  @GetMapping("/cart")
  public ResponseEntity<MyCartResponse> getMethodName() {
    return foodService.myChart();
  }

  @PutMapping("/cart/{cartId}")
  public ResponseEntity<MessageResponse> putMethodName(@PathVariable Integer cartId,
      @RequestBody PutQtyRequest request) {
    return foodService.putQtyCart(request, cartId);
  }

  @PostMapping("/cart/checkout")
  public ResponseEntity<MessageResponse> checkoutCart(@RequestBody List<CheckoutCartIdRequest> request) {
    return foodService.checkoutCart(request);
  }

  @GetMapping("/foods/my-favorite-foods")
  public ResponseEntity<FoodListResponse> myFavfood(@ModelAttribute FoodListRequestDto foodListRequestDto,
      PageRequest pageRequest) {
    return foodService.getMyFavFoods(foodListRequestDto, pageRequest.getPage());
  }

}
