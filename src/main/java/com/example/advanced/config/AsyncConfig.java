package com.example.advanced.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync      // Bật tính năng Bất đồng bộ
@EnableScheduling // Bật tính năng Lên lịch (Cron Job)
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);     // Số nhân viên nòng cốt (luôn túc trực)
        executor.setMaxPoolSize(10);     // Số nhân viên tối đa (nếu việc ngập đầu thì tuyển thêm)
        executor.setQueueCapacity(100);  // Hàng đợi (nếu full nhân viên thì xếp hồ sơ vào đây)
        executor.setThreadNamePrefix("AsyncThread-"); // Đặt tên để dễ soi log
        executor.initialize();
        return executor;
    }
}
