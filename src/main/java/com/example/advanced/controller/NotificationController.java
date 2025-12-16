package com.example.advanced.controller;

import com.example.advanced.dto.EmailRequest;
import com.example.advanced.service.NotificationService;
import com.example.advanced.service.RabbitMQProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RabbitMQProducer producer; // Inject Producer
    
    @GetMapping("/send-email")
    public String triggerEmail(@RequestParam String email) {
        // Gọi hàm async
        notificationService.sendEmail(email);
        
        // Trả về kết quả NGAY LẬP TỨC, mặc dù email chưa gửi xong
        return "Đã tiếp nhận yêu cầu gửi mail cho: " + email + ". Hệ thống đang xử lý ngầm!";
    }
    


    @GetMapping("/send-email-queue")
    public String sendEmailQueue(@RequestParam String email) {
        // Tạo object request
        EmailRequest request = new EmailRequest();
        request.setToEmail(email);
        request.setSubject("Test RabbitMQ");
        request.setBody("Hello from Message Queue!");

        // 1. Đẩy vào hàng đợi (Cực nhanh, tốn < 5ms)
        producer.sendEmailRequest(request);

        // 2. Trả về kết quả ngay
        return "Đã đẩy yêu cầu vào hàng đợi RabbitMQ! Hệ thống sẽ xử lý dần.";
    }
}
