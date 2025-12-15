package com.example.advanced.config;

import com.example.advanced.entity.mysql.AppUser;
import com.example.advanced.entity.postgres.LoginHistory;
import com.example.advanced.repository.mysql.UserRepository;
import com.example.advanced.repository.postgres.LoginHistoryRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        Authentication authentication) throws IOException, ServletException {
        
    	DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        userRepository.findByEmail(email).ifPresentOrElse(
            user -> System.out.println("User cũ: " + email),
            () -> {
                AppUser newUser = new AppUser();
                newUser.setEmail(email);
                newUser.setName(oauthUser.getAttribute("name"));
                newUser.setProvider("GOOGLE");
                
                newUser.setRole("ROLE_USER"); 
                
                userRepository.save(newUser);
            }
        );

        LoginHistory history = new LoginHistory(email, LocalDateTime.now(), "SUCCESS");
        loginHistoryRepository.save(history);
        System.out.println("✅ Đã ghi log vào PostgreSQL!");

        response.sendRedirect("/user-info");
    }
}
