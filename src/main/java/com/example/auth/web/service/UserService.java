package com.example.auth.web.service;

import com.example.auth.web.model.User;

public interface UserService {
  void saveUser(User user,String[] roles);
  User findByUsername(String username);
}
