package com.example.auth.web.model;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Data;

@Data
public class CustomUser extends User{
  private final Long userId;
  
  public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userID){
    super(username, password, authorities);    
    this.userId = userID;
  }  
}
