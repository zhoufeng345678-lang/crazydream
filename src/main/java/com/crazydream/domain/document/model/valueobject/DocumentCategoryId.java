package com.crazydream.domain.document.model.valueobject;

import java.util.Objects;

/**
 * 资料分类ID值对象
 */
public class DocumentCategoryId {
    
    private final Long value;
    
    private DocumentCategoryId(Long value) {
        this.value = value;
    }
    
    public static DocumentCategoryId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("资料分类ID不能为空或小于等于0");
        }
        return new DocumentCategoryId(value);
    }
    
    public Long getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentCategoryId that = (DocumentCategoryId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
