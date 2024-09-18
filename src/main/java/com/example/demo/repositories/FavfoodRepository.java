package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Favfoods;
import com.example.demo.model.FavfoodsId;
import com.example.demo.model.Foods;

public interface FavfoodRepository extends JpaRepository<Favfoods, FavfoodsId>, JpaSpecificationExecutor<Favfoods> {
  @Query("SELECT f FROM Favfoods f WHERE f.users.userId = :userId and f.foods.foodId = :foodId")
  Optional<Favfoods> findFavoriteFoodByAndUser(int userId, int foodId);

  @Query("SELECT f FROM Favfoods f WHERE f.users.userId = :userId")
  Page<Favfoods> findFavoriteFoodByUser(@Param("userId") int userId, Pageable page);
}
