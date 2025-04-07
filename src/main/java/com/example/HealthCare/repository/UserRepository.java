package com.example.HealthCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HealthCare.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  User findByRefreshTokenAndEmail(String token, String email);

  
}
