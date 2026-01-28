package com.crazydream.domain.user.model.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 手机号值对象
 * 仅支持中国大陆手机号格式（11位数字，1开头）
 */
public class Phone {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1\\d{10}$");
    
    private final String value;
    
    private Phone(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("手机号不能为空");
        }
        String cleanValue = value.trim();
        if (!PHONE_PATTERN.matcher(cleanValue).matches()) {
            throw new IllegalArgumentException("手机号格式不正确");
        }
        this.value = cleanValue;
    }
    
    public static Phone of(String value) {
        return new Phone(value);
    }
    
    /**
     * 创建可选的手机号（允许null）
     */
    public static Phone ofNullable(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return new Phone(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(value, phone.value);
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
