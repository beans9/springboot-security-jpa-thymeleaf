package com.example.auth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.auth.web.model.User;
import com.example.auth.web.service.SecurityService;
import com.example.auth.web.service.UserService;

import javassist.bytecode.DuplicateMemberException;

@Controller
@SessionAttributes("user_id")
public class WebController {
  @Autowired
  UserService userService;
  
  @Autowired
  private SecurityService securityService;
  
  @RequestMapping(value="/main")
  public String main(HttpSession session){  
    Long user_id = (Long)session.getAttribute("user_id");
    System.out.println(user_id);
    return "main";
  }
  
  // 로그인 
  @RequestMapping("/login")
  public String login(Model model, String error, String logout, HttpServletRequest request ){
    if (logout != null){
      model.addAttribute("logout", "You have been logged out successfully.");
    }
    return "login";
  }
  
  // 로그인 실패시
  @RequestMapping(value="/loginError")
  public String loginError(Model model, String username ){
    model.addAttribute("error", "Your username and password is invalid.");
    model.addAttribute("username",username);
    return "login";
  }
  
  // 회원가입폼 
  @RequestMapping(value="/registration",method=RequestMethod.GET)
  public String registration(Model model){
    model.addAttribute("userForm", new User());
    return "registration";
  }
  
  // 회원가입 처리 후 로그인 
  @RequestMapping(value="/registration",method=RequestMethod.POST)
  public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, 
		Model model ,String[] roles, HttpSession session){
    String password = userForm.getPassword();
    
    try {
      userForm = userService.saveUser(userForm,roles);
    } catch (DuplicateMemberException e) {
      e.printStackTrace();
      model.addAttribute("error", "username is duplicated.");
      model.addAttribute("userForm",userForm);
      
      return "/registration";
    }
    
    securityService.autologin(userForm.getUsername(),password);
    
    // session
    session.setAttribute("userId", userForm.getId());
    return "redirect:/main";
  }
  
  // admin 사용자 테스트 
  @RequestMapping("/admin")
  public String admin(){
    return "/admin/admin";
  }
  
  // user 사용자 테스트 
  @RequestMapping("/user")
  public String user(){
    return "/user/user";
  }
  
  // 권한없는 페이지를 들어갔을때 
  @RequestMapping("/403")
  public String access(){
    return "/access";
  }
}
