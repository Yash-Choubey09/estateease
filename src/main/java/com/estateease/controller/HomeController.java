package com.estateease.controller;

import com.estateease.model.*;
import com.estateease.repository.PropertyRepository;
import com.estateease.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PropertyRepository properties;
    private final CurrentUserService current;

    public HomeController(PropertyRepository p, CurrentUserService c) {
        this.properties = p;
        this.current = c;
    }

    @GetMapping("/")
    public String home(Model m) {
        m.addAttribute("properties", properties.findTop6ByAvailabilityOrderByCreatedAtDesc(Availability.AVAILABLE));
        m.addAttribute("total", properties.count());
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        var u = current.get(auth);
        return "redirect:" + (u.getRole() == Role.OWNER ? "/owner/dashboard" : "/tenant/dashboard");
    }

    @GetMapping("/access-denied")
    public String denied() {
        return "access-denied";
    }
}
