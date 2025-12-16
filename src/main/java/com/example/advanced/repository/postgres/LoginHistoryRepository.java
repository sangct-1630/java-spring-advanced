package com.example.advanced.repository.postgres;
import com.example.advanced.entity.postgres.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
}
