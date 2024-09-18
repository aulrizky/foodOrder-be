package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.example.demo.model.Foods;

public interface FoodRepository extends JpaRepository<Foods,Integer>,JpaSpecificationExecutor<Foods>{
  
}
