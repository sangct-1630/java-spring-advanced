package com.example.advanced.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Bật tính năng Broker xử lý tin nhắn
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 1. Điểm đăng ký kết nối: Client sẽ gọi vào http://localhost:8090/ws
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Cho phép mọi nguồn (để dễ test)
                .withSockJS(); // Tự động dùng SockJS nếu trình duyệt không hỗ trợ WS gốc
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 2. Cấu hình định tuyến tin nhắn
        // Các tin nhắn Server gửi xuống Client sẽ có prefix là /topic
        registry.enableSimpleBroker("/topic");
        
        // Các tin nhắn Client gửi lên Server sẽ có prefix là /app
        registry.setApplicationDestinationPrefixes("/app");
    }
}
