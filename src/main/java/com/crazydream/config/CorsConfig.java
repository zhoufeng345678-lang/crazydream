package com.crazydream.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS配置类
 * 用于配置跨域资源共享，允许前端应用访问后端API
 *
 * @author CrazyDream Team
 * @since 2025-12-10
 */
@Configuration
public class CorsConfig {

    /**
     * 创建CORS过滤器
     *
     * @return CorsFilter实例
     */
    @Bean
    public CorsFilter corsFilter() {
        // 创建CORS配置
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许所有来源的请求
        config.addAllowedOriginPattern("*");
        
        // 允许所有HTTP方法
        config.addAllowedMethod("*");
        
        // 允许所有HTTP头
        config.addAllowedHeader("*");
        
        // 允许携带凭证
        config.setAllowCredentials(true);
        
        // 设置暴露的响应头，用于获取token等
        config.addExposedHeader("Authorization");
        config.addExposedHeader("X-Total-Count");
        
        // 创建URL匹配源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        // 对所有API路径应用CORS配置
        source.registerCorsConfiguration("/api/**", config);
        
        return new CorsFilter(source);
    }
}