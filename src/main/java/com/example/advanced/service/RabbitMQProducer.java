package com.example.advanced.service;

import com.example.advanced.config.RabbitMQConfig;
import com.example.advanced.dto.EmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmailRequest(EmailRequest request) {
        logger.info("📤 Đẩy yêu cầu gửi mail vào RabbitMQ: {}", request);
        
        // Gửi message vào Exchange với Routing Key cụ thể
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                request
        );
    }
}
