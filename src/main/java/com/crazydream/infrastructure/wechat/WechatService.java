package com.crazydream.infrastructure.wechat;

import com.crazydream.config.WechatConfig;
import com.crazydream.domain.user.model.valueobject.WechatOpenId;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Objects;

/**
 * 微信开放平台服务
 */
@Service
public class WechatService {
    
    private static final Logger logger = LoggerFactory.getLogger(WechatService.class);
    
    @Autowired
    private WechatConfig wechatConfig;
    
    private final RestTemplate restTemplate;
    
    public WechatService(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }
    
    /**
     * 通过微信授权码换取OpenID
     * 
     * @param code 微信授权码
     * @return WechatOpenId
     * @throws IllegalArgumentException 当授权码无效或微信API调用失败时
     */
    public WechatOpenId getOpenIdByCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("微信授权码不能为空");
        }
        
        try {
            // 构建微信API请求URL
            String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    wechatConfig.getApi().getCode2sessionUrl(),
                    wechatConfig.getAppId(),
                    wechatConfig.getAppSecret(),
                    code);
            
            logger.info("调用微信code2Session接口，code: {}", code);
            
            // 调用微信API
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
            JsonNode body = Objects.requireNonNull(response.getBody(), "微信API返回空响应");
            
            // 检查错误码
            if (body.has("errcode")) {
                int errcode = body.get("errcode").asInt();
                String errmsg = body.has("errmsg") ? body.get("errmsg").asText() : "未知错误";
                
                logger.error("微信API返回错误: errcode={}, errmsg={}", errcode, errmsg);
                
                // 常见错误码处理
                switch (errcode) {
                    case 40029:
                        throw new IllegalArgumentException("微信授权失败，请重新登录");
                    case -1:
                        throw new IllegalArgumentException("微信服务繁忙，请稍后重试");
                    case 40163:
                        throw new IllegalArgumentException("微信授权码已使用");
                    default:
                        throw new IllegalArgumentException("微信授权失败: " + errmsg);
                }
            }
            
            // 获取openid
            if (!body.has("openid")) {
                logger.error("微信API响应中缺少openid字段: {}", body.toString());
                throw new IllegalArgumentException("微信服务异常，请稍后重试");
            }
            
            String openid = body.get("openid").asText();
            logger.info("微信授权成功，openid: {}", openid);
            
            return WechatOpenId.ofNullable(openid);
            
        } catch (IllegalArgumentException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            logger.error("调用微信API异常", e);
            throw new IllegalArgumentException("微信服务暂时不可用，请稍后重试");
        }
    }
}
