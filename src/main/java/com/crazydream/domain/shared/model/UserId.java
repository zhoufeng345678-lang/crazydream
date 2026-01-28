package com.crazydream.domain.shared.model;

import java.util.Objects;

/**
 * 用户ID值对象
 * 跨领域共享的用户标识符
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public class UserId {
    private final Long value;
    
    private UserId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或非正数");
        }
        this.value = value;
    }
    
    public static UserId of(Long value) {
        return new UserId(value);
    }
    
    public Long getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "UserId{" + value + '}';
    }
}
