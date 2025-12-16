package com.example.advanced.service;

import com.example.advanced.config.RabbitMQConfig;
import com.example.advanced.dto.EmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate; // <--- Import cái này
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // "Người đưa thư" của WebSocket

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processEmail(EmailRequest request) {
        logger.info("📥 RabbitMQ nhận yêu cầu: {}", request);

        // 1. Gửi email thật (tốn 3-5s)
        notificationService.sendEmail(request.getToEmail());

        // 2. Gửi xong -> Bắn thông báo realtime xuống Client
        String message = "✅ Đã gửi xong email cho: " + request.getToEmail();
        
        // Gửi đến kênh công khai "/topic/notifications"
        messagingTemplate.convertAndSend("/topic/notifications", message);
        
        logger.info("📡 Đã push thông báo WebSocket tới Frontend");
    }
}
