package com.example.auth.web.service;

import javax.servlet.http.HttpSession;

import com.example.auth.web.model.User;

import javassist.bytecode.DuplicateMemberException;

public interface UserService {
  User saveUser(User user,String[] roles) throws DuplicateMemberException ;
  User findByUsername(String username);
  Long getUserId(HttpSession session);
}
