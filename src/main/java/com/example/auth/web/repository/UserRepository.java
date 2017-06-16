package com.example.auth.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auth.web.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
  public User findByUsername(String username);
}
