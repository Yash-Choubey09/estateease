package com.estateease.security;
import com.estateease.repository.UserRepository; import org.springframework.security.core.userdetails.*; import org.springframework.stereotype.Service;
@Service public class CustomUserDetailsService implements UserDetailsService {
 private final UserRepository users; public CustomUserDetailsService(UserRepository users){this.users=users;}
 @Override public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
  var user=users.findByEmail(email.toLowerCase()).orElseThrow(()->new UsernameNotFoundException("Account not found"));
  return org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword()).roles(user.getRole().name()).build();
 }
}
