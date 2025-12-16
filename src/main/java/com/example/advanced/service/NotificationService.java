package com.example.advanced.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Async("taskExecutor") // Chạy ở luồng riêng (Bất đồng bộ)
    public void sendEmail(String toEmail) {
        logger.info("📩 Đang chuẩn bị gửi email thật cho [{}]...", toEmail);
        
        try {
            // 1. Tạo nội dung email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@demo-app.com"); // (Gmail sẽ tự override bằng email thật của em)
            message.setTo(toEmail);
            message.setSubject("🔥 Chúc mừng! Test Async Email thành công");
            message.setText("Xin chào,\n\nNếu bạn nhận được email này nghĩa là tính năng Async Email của bạn đã hoạt động chuẩn Enterprise!\n\nRegards,\nSpring Boot Trainer.");

            // 2. Thực hiện gửi (Hành động này tốn thời gian nhất)
            mailSender.send(message);

            logger.info("✅ Đã gửi email thành công cho [{}]!", toEmail);

        } catch (Exception e) {
            logger.error("❌ Gửi mail thất bại: {}", e.getMessage());
        }
    }

    // 2. TÍNH NĂNG SCHEDULER (Chạy định kỳ)
    // Ví dụ: Cứ 10 giây chạy 1 lần (quét DB, báo cáo, dọn rác...)
    // fixedRate = 10000ms = 10s
    @Scheduled(fixedRate = 30000) 
    public void autoReport() {
        logger.info("⏰ [Scheduler] Báo cáo hệ thống lúc: {} - Bởi: {}", 
                    LocalDateTime.now(), Thread.currentThread().getName());
    }
}
