package com.example.advanced.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "email_queue";
    public static final String EXCHANGE_NAME = "email_exchange";
    public static final String ROUTING_KEY = "email_routing_key";

    // 1. Tạo hàng đợi (Queue)
    @Bean
    public Queue emailQueue() {
        return new Queue(QUEUE_NAME, true); // true = bền vững (server sập không mất hàng đợi)
    }

    // 2. Tạo tổng đài (Exchange) - Loại Direct (gửi trực tiếp)
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    // 3. Ràng buộc (Binding) Queue vào Exchange
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    // 4. Cấu hình chuyển đổi Message sang JSON (thay vì byte stream khó đọc)
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
