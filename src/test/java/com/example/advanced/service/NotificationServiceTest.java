package com.example.advanced.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

// 1. Kích hoạt Mockito
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    // 2. Tạo Mock cho JavaMailSender (Đối tượng giả, không gửi mail thật)
    @Mock
    private JavaMailSender mailSender;

    // 3. Inject Mock vào Service cần test
    @InjectMocks
    private NotificationService notificationService;

    // --- CASE 1: Gửi thành công ---
    @Test
    void testSendEmail_WhenSuccess_ShouldCallMailSender() {
        // --- GIVEN ---
        String toEmail = "test@gmail.com";

        // --- WHEN ---
        notificationService.sendEmail(toEmail);

        // --- THEN ---
        // A. Xác minh xem hàm mailSender.send() có được gọi đúng 1 lần không?
        // ArgumentCaptor dùng để bắt lấy cái lá thư (Message) mà Service đã gửi đi để kiểm tra nội dung
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        
        verify(mailSender, times(1)).send(messageCaptor.capture());

        // B. Mổ xẻ lá thư xem viết đúng không
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        
        Assertions.assertEquals(toEmail, Objects.requireNonNull(sentMessage.getTo())[0]);
        Assertions.assertEquals("🔥 Chúc mừng! Test Async Email thành công", sentMessage.getSubject());
        Assertions.assertTrue(Objects.requireNonNull(sentMessage.getText()).contains("Xin chào"));
    }

    // --- CASE 2: Gửi thất bại (Ví dụ mất mạng) ---
    @Test
    void testSendEmail_WhenException_ShouldCatchAndLog() {
        // --- GIVEN ---
        String toEmail = "fail@gmail.com";
        
        // Dạy cho Mock: Hễ ai gọi send() thì ném lỗi ngay
        Mockito.doThrow(new MailSendException("Mất kết nối SMTP")).when(mailSender).send(any(SimpleMailMessage.class));

        // --- WHEN ---
        // Gọi hàm. Nếu logic của em KHÔNG có try-catch, test này sẽ đỏ (fail).
        // Vì em CÓ try-catch, nên hàm này sẽ chạy êm ru, chỉ log error ra thôi.
        Assertions.assertDoesNotThrow(() -> notificationService.sendEmail(toEmail));

        // --- THEN ---
        // Vẫn đảm bảo là hàm send đã được gọi (dù thất bại)
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    // --- CASE 3: Test Scheduler (Chỉ test logic, không test thời gian) ---
    @Test
    void testAutoReport_ShouldRunWithoutError() {
        // Hàm này chỉ log ra màn hình, không có logic phức tạp
        // Ta chỉ cần đảm bảo gọi nó không bị lỗi chết chương trình
        Assertions.assertDoesNotThrow(() -> notificationService.autoReport());
    }
}
