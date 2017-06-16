package com.example.auth.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth.web.model.User;
import com.example.auth.web.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;
    
  @Override
  public void saveUser(User user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userRepository.save(user);
  }
  
  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }
}
