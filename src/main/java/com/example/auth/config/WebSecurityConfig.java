package com.example.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  public static final String REMEMBER_ME_KEY = "REMEBMER_ME_KEY";
  public static final String REMEMBER_ME_COOKE_NAME = "REMEMBER_ME_COOKE";
  
  @Autowired
  private UserDetailsService userDetailsService;
  
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
      return new BCryptPasswordEncoder();
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception{
	// h2 console 사용을 위한 설정 
    http.csrf().ignoringAntMatchers("/h2console/**");
    http.headers().frameOptions().sameOrigin();
    
    http
      .authorizeRequests()
        // 해당 url을 허용한다. 
      	.antMatchers("/resources/**","/loginError","/registration","/h2console/**").permitAll()
        // admin 폴더에 경우 admin 권한이 있는 사용자에게만 허용 
      	.antMatchers("/admin/**").hasAuthority("ADMIN")
      	// user 폴더에 경우 user 권한이 있는 사용자에게만 허용
        .antMatchers("/user/**").hasAuthority("USER")
        .anyRequest().authenticated()
        .and()
      .formLogin()
        .loginPage("/login")
        .successHandler(new CustomAuthenticationSuccess()) // 로그인 성공 핸들러 
        .failureHandler(new CustomAuthenticationFailure()) // 로그인 실패 핸들러 
        .permitAll()
      .and()
        .rememberMe()
        .key(REMEMBER_ME_KEY)
        //.rememberMeParameter("remember-me")
        .rememberMeServices(tokenBasedRememberMeServices())
      .and()
        .logout()
        .deleteCookies(REMEMBER_ME_COOKE_NAME)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
        .permitAll()
        .and()
      .logout()
        .permitAll()
        .and()
       .exceptionHandling().accessDeniedPage("/403"); // 권한이 없을경우 해당 url로 이동
  }
  
  @Bean
  public RememberMeServices tokenBasedRememberMeServices() {
    TokenBasedRememberMeServices tokenBasedRememberMeServices 
      = new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService());
    tokenBasedRememberMeServices.setAlwaysRemember(false);
    tokenBasedRememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 31);
    tokenBasedRememberMeServices.setCookieName(REMEMBER_ME_COOKE_NAME);
    return tokenBasedRememberMeServices;
  }
  
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
  }  
}
