package com.crazydream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Crazydream 应用主入口类
 * 人生计划清单后端服务的启动类，负责初始化和启动 Spring Boot 应用
 * 
 * @author CrazyDream Team
 * @since 2025-12-09
 */
@SpringBootApplication
@MapperScan("com.crazydream.infrastructure.persistence.mapper")
@EnableAsync
public class CrazydreamApplication {

    /**
     * 应用程序启动方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(CrazydreamApplication.class, args);
    }

}