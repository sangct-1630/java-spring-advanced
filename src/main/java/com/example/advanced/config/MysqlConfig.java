package com.example.advanced.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.example.advanced.repository.mysql", // <--- Chỉ quét thư mục này
    entityManagerFactoryRef = "mysqlEntityManagerFactory",
    transactionManagerRef = "mysqlTransactionManager"
)
public class MysqlConfig {

    @Primary // Đánh dấu đây là DB chính (mặc định)
    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql") // Đọc từ application.properties
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("mysqlDataSource") DataSource dataSource) {
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update"); // Tự động tạo/update bảng
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect"); // (Tùy chọn) Khai báo rõ dialect

        return builder
                .dataSource(dataSource)
                .packages("com.example.advanced.entity.mysql") 
                .persistenceUnit("mysqlPU")
                .properties(properties) // <--- QUAN TRỌNG: Thêm dòng này
                .build();
    }

    @Primary
    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("mysqlEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
