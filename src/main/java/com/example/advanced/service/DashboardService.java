package com.example.advanced.service;

import com.example.advanced.dto.ChartData;
import com.example.advanced.entity.postgres.LoginHistory;
import com.example.advanced.repository.postgres.LoginHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    public List<ChartData> getLoginStatistics() {
        List<LoginHistory> logs = loginHistoryRepository.findAll();

        Map<String, Long> stats = logs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getLoginTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        Collectors.counting()
                ));

        List<ChartData> result = new ArrayList<>();
        stats.forEach((date, count) -> result.add(new ChartData(date, count)));

        result.sort((a, b) -> a.getLabel().compareTo(b.getLabel()));

        return result;
    }
}
