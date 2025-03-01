package com.example.HealthCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HealthCare.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {

  User findByEmail(String email);

  boolean existsByEmail(String email);

  User findByRefreshTokenAndEmail(String token, String email);

  
}
