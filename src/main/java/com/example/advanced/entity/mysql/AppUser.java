package com.example.advanced.entity.mysql;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class AppUser {

	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String provider; // "GOOGLE", "FACEBOOK"...
    private String role;

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}
