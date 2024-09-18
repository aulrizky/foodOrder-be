package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Users;
import java.util.Optional;


public interface UserRepository extends JpaRepository<Users,Integer> {
  Optional<Users> findByUsername(String username);

  Boolean existsByUsername(String username);
}
