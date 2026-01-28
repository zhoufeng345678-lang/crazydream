package com.crazydream.domain.category.model.valueobject;

import java.util.Objects;

public class CategoryName {
    private final String value;
    
    private CategoryName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("分类名称不能为空");
        }
        this.value = value.trim();
    }
    
    public static CategoryName of(String value) {
        return new CategoryName(value);
    }
    
    public String getValue() { return value; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryName that = (CategoryName) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
