package com.estateease.controller;
import com.estateease.model.*; import com.estateease.repository.UserRepository; import jakarta.validation.Valid; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.validation.BindingResult; import org.springframework.web.bind.annotation.*; import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller public class AuthController {
 private final UserRepository users; private final PasswordEncoder encoder;
 public AuthController(UserRepository users,PasswordEncoder encoder){this.users=users;this.encoder=encoder;}
 @GetMapping("/register") public String register(Model model){model.addAttribute("user",new AppUser());model.addAttribute("roles",new Role[]{Role.OWNER,Role.TENANT});return "register";}
 @PostMapping("/register") public String create(@Valid @ModelAttribute("user") AppUser user,BindingResult result,@RequestParam String confirmPassword,Model model,RedirectAttributes flash){
  if(users.existsByEmail(user.getEmail())) result.rejectValue("email","duplicate","An account with this email already exists.");
  if(user.getPassword()==null || user.getPassword().length()<8) result.rejectValue("password","weak","Use at least 8 characters.");
  if(!user.getPassword().equals(confirmPassword)) result.rejectValue("password","mismatch","Passwords do not match.");
  if(result.hasErrors()){model.addAttribute("roles",new Role[]{Role.OWNER,Role.TENANT});return "register";}
  user.setPassword(encoder.encode(user.getPassword()));users.save(user);flash.addFlashAttribute("success","Account created. Please sign in.");return "redirect:/login";
 }
 @GetMapping("/login") public String login(){return "login";}
}
