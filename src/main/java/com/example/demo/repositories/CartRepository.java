package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Carts;
import com.example.demo.model.Users;

import java.util.List;

public interface CartRepository extends JpaRepository<Carts, Integer> {
  @Query("SELECT c FROM Carts c WHERE c.users.userId = :userId and c.foods.foodId=:foodId and c.isDeleted=false")
  Optional<Carts> findCartByFoodAndUser(@Param("userId") int userId, @Param("foodId") int foodId);

  @Query("SELECT c FROM Carts c WHERE c.users.userId = :userId  and c.isDeleted= false")
  List<Carts> findByUserIdandIsDelete(@Param("userId") int userId);

  @Query("SELECT c FROM Carts c WHERE c.users.userId = :userId and c.cartId = :cartId")
  Optional<Carts> findCartByUserIdAndChartId(@Param("userId") int userId, @Param("cartId") int cartId);

  Optional<Carts> findByCartId(int cartId);
}
