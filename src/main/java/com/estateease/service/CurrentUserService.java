package com.estateease.service;
import com.estateease.model.AppUser; import com.estateease.repository.UserRepository; import org.springframework.security.core.Authentication; import org.springframework.stereotype.Service;
@Service public class CurrentUserService { private final UserRepository users; public CurrentUserService(UserRepository users){this.users=users;}
 public AppUser get(Authentication auth){return users.findByEmail(auth.getName()).orElseThrow();}
}
