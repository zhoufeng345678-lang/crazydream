package com.crazydream.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS配置类
 * 用于初始化OSS客户端和管理OSS相关配置信息
 * 
 * @author CrazyDream Team
 * @since 2025-12-09
 */
@Configuration
public class OssConfig {
    
    /**
     * OSS服务端点
     */
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    
    /**
     * 阿里云访问密钥ID
     */
    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;
    
    /**
     * 阿里云访问密钥密钥
     */
    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;
    
    /**
     * OSS存储桶名称
     */
    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;
    
    /**
     * OSS自定义域名
     */
    @Value("${aliyun.oss.domain}")
    private String domain;
    
    /**
     * 初始化OSS客户端
     * 
     * @return OSS客户端实例
     */
    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
    
    /**
     * 获取OSS服务端点
     * 
     * @return OSS服务端点
     */
    public String getEndpoint() {
        return endpoint;
    }
    
    /**
     * 获取阿里云访问密钥ID
     * 
     * @return 阿里云访问密钥ID
     */
    public String getAccessKeyId() {
        return accessKeyId;
    }
    
    /**
     * 获取阿里云访问密钥密钥
     * 
     * @return 阿里云访问密钥密钥
     */
    public String getAccessKeySecret() {
        return accessKeySecret;
    }
    
    /**
     * 获取OSS存储桶名称
     * 
     * @return OSS存储桶名称
     */
    public String getBucketName() {
        return bucketName;
    }
    
    /**
     * 获取OSS自定义域名
     * 
     * @return OSS自定义域名
     */
    public String getDomain() {
        return domain;
    }
}