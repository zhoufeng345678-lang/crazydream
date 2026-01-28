package com.crazydream.domain.shared.model;

import java.util.Objects;

/**
 * 分类ID值对象
 * 跨领域共享的分类标识符
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public class CategoryId {
    private final Long value;
    
    private CategoryId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("分类ID不能为空或非正数");
        }
        this.value = value;
    }
    
    public static CategoryId of(Long value) {
        return new CategoryId(value);
    }
    
    public Long getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryId that = (CategoryId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "CategoryId{" + value + '}';
    }
}
