package com.example.advanced.dto;

import java.io.Serializable;

public class EmailRequest implements Serializable {
    private String toEmail;
    private String subject;
    private String body;

    // Constructor, Getter, Setter, toString (Có thể dùng @Data của Lombok)
    public EmailRequest() {}
    
    public EmailRequest(String toEmail, String subject, String body) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
    }

    public String getToEmail() { return toEmail; }
    public void setToEmail(String toEmail) { this.toEmail = toEmail; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    
    @Override
    public String toString() {
        return "EmailRequest{to='" + toEmail + "'}";
    }
}
