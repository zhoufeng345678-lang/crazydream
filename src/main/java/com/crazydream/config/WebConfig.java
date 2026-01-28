package com.crazydream.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    /**
     * 配置RestTemplate Bean
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(5000))
                .setReadTimeout(Duration.ofMillis(5000))
                .build();
    }
    
    // 添加重定向配置，将旧的登录路径重定向到新的登录路径
    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addRedirectViewController("/auth/login", "/api/auth/login");
        registry.addRedirectViewController("/auth/register", "/api/auth/register");
    }
}
