package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
  // @Query("SELECT o FROM OderDetail o WHERE o.order.orderId=:orderId")
  List<OrderDetail> findByOrderOrderId(int orderId);
}
