package com.example.demo.repositories;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Orders;
public interface OrderRepository extends JpaRepository<Orders,Integer>{
  @Query("SELECT o FROM Orders o WHERE o.user.userId=:userId")
  Page<Orders> findByUserId(@Param("userId") Integer userId,Pageable page);
}
