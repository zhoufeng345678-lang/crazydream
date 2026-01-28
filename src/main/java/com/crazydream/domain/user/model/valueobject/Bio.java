package com.crazydream.domain.user.model.valueobject;

import java.util.Objects;

/**
 * Bio值对象 - 用户个人简介
 */
public class Bio {
    
    private static final int MAX_LENGTH = 500;
    
    private final String value;
    
    private Bio(String value) {
        this.value = value;
    }
    
    /**
     * 创建Bio对象
     * @param value bio内容，可以为null
     * @return Bio对象
     * @throws IllegalArgumentException 如果bio长度超过限制
     */
    public static Bio of(String value) {
        if (value == null || value.isEmpty()) {
            return new Bio(null);
        }
        
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("个人简介长度不能超过" + MAX_LENGTH + "字符");
        }
        
        return new Bio(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bio bio = (Bio) o;
        return Objects.equals(value, bio.value);
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
