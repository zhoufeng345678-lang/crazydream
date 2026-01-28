package com.crazydream.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信开放平台配置
 */
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WechatConfig {
    
    private String appId;
    private String appSecret;
    private Api api;
    
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public String getAppSecret() {
        return appSecret;
    }
    
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
    
    public Api getApi() {
        return api;
    }
    
    public void setApi(Api api) {
        this.api = api;
    }
    
    public static class Api {
        private String code2sessionUrl;
        private Integer timeout;
        
        public String getCode2sessionUrl() {
            return code2sessionUrl;
        }
        
        public void setCode2sessionUrl(String code2sessionUrl) {
            this.code2sessionUrl = code2sessionUrl;
        }
        
        public Integer getTimeout() {
            return timeout;
        }
        
        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
        }
    }
}
