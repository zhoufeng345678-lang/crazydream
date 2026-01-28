package com.crazydream.domain.user.model.valueobject;

import java.util.Objects;

/**
 * 微信OpenID值对象
 * 用于微信第三方登录集成
 */
public class WechatOpenId {
    private static final int MAX_LENGTH = 100;
    
    private final String value;
    
    private WechatOpenId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("微信OpenID不能为空");
        }
        String cleanValue = value.trim();
        if (cleanValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("微信OpenID长度不能超过" + MAX_LENGTH + "个字符");
        }
        this.value = cleanValue;
    }
    
    public static WechatOpenId of(String value) {
        return new WechatOpenId(value);
    }
    
    /**
     * 创建可选的微信OpenID（允许null）
     */
    public static WechatOpenId ofNullable(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return new WechatOpenId(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WechatOpenId that = (WechatOpenId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
