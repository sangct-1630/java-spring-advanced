package com.example.advanced.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; 
    }

    @GetMapping("/user-info")
    public String userInfo(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));
            model.addAttribute("picture", principal.getAttribute("picture"));
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        
        return "profile";
    }
}