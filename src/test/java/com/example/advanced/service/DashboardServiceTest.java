package com.example.advanced.service;

import com.example.advanced.dto.ChartData;
import com.example.advanced.entity.postgres.LoginHistory;
import com.example.advanced.repository.postgres.LoginHistoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// 1. Dùng Mockito để chạy Test
@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    // 2. Tạo bản Mock cho Repository (Hàng giả)
    @Mock
    private LoginHistoryRepository loginHistoryRepository;

    // 3. Inject hàng giả vào Service thật
    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void testGetLoginStatistics_WhenHasData_ShouldReturnCorrectGrouping() {
        // --- GIVEN (Chuẩn bị dữ liệu giả) ---
        // Giả lập 3 dòng log: 2 dòng ngày 16/12, 1 dòng ngày 17/12
        LoginHistory log1 = new LoginHistory("user1@test.com", LocalDateTime.of(2025, 12, 16, 10, 0), "SUCCESS");
        LoginHistory log2 = new LoginHistory("user2@test.com", LocalDateTime.of(2025, 12, 16, 15, 0), "SUCCESS");
        LoginHistory log3 = new LoginHistory("user1@test.com", LocalDateTime.of(2025, 12, 17, 0, 0), "FAILED");
        
        List<LoginHistory> fakeLogs = Arrays.asList(log1, log2, log3);

        // Dạy cho con Mock Repository học: Khi ai gọi findAll() -> Trả về fakeLogs ngay
        Mockito.when(loginHistoryRepository.findAll()).thenReturn(fakeLogs);

        // --- WHEN (Thực hiện hành động) ---
        List<ChartData> result = dashboardService.getLoginStatistics();

        // --- THEN (Kiểm tra kết quả) ---
        // Mong đợi: Có 2 nhóm ngày (16 và 17)
        Assertions.assertEquals(2, result.size());
        
        // Kiểm tra ngày 16/12 phải có giá trị là 2
        ChartData dataDay16 = result.stream().filter(d -> d.getLabel().equals("2025-12-16")).findFirst().get();
        Assertions.assertEquals(2, dataDay16.getValue());
        
        // Kiểm tra ngày 17/12 phải có giá trị là 1
        ChartData dataDay17 = result.stream().filter(d -> d.getLabel().equals("2025-12-17")).findFirst().get();
        Assertions.assertEquals(1, dataDay17.getValue());
    }
    
    @Test
    void testGetLoginStatistics_WhenNoData_ShouldReturnEmptyList() {
        // --- GIVEN ---
        // Giả lập Repository trả về rỗng
        Mockito.when(loginHistoryRepository.findAll()).thenReturn(Collections.emptyList());

        // --- WHEN ---
        List<ChartData> result = dashboardService.getLoginStatistics();

        // --- THEN ---
        Assertions.assertTrue(result.isEmpty());
    }
}
