package com.example.advanced.entity.postgres;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_history")
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private LocalDateTime loginTime;
    private String status; // Ví dụ: "SUCCESS", "FAILED"

    public LoginHistory() {} 

    public LoginHistory(String email, LocalDateTime loginTime, String status) {
        this.email = email;
        this.loginTime = loginTime;
        this.status = status;
    }

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public LocalDateTime getLoginTime() { return loginTime; }
    public String getStatus() { return status; }
}