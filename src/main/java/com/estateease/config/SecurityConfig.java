package com.estateease.config;
import com.estateease.security.CustomUserDetailsService; import org.springframework.context.annotation.*; import org.springframework.security.authentication.dao.DaoAuthenticationProvider; import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.SecurityFilterChain;
@Configuration public class SecurityConfig {
 private final CustomUserDetailsService userDetails;
 public SecurityConfig(CustomUserDetailsService userDetails){this.userDetails=userDetails;}
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean DaoAuthenticationProvider authenticationProvider(){var p=new DaoAuthenticationProvider();p.setUserDetailsService(userDetails);p.setPasswordEncoder(passwordEncoder());return p;}
 @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  http.authenticationProvider(authenticationProvider()).authorizeHttpRequests(a->a.requestMatchers("/","/properties","/properties/*","/register","/login","/css/**","/images/**","/error").permitAll().requestMatchers("/owner/**").hasRole("OWNER").requestMatchers("/tenant/**").hasRole("TENANT").anyRequest().authenticated())
  .formLogin(f->f.loginPage("/login").defaultSuccessUrl("/dashboard",true).permitAll())
  .logout(l->l.logoutSuccessUrl("/?logout").permitAll())
  .exceptionHandling(e->e.accessDeniedPage("/access-denied"));
  return http.build();
 }
}
