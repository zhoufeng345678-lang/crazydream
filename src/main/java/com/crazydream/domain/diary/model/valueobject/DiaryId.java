package com.crazydream.domain.diary.model.valueobject;

import java.util.Objects;

/**
 * 日记ID值对象
 * 封装日记的唯一标识符
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public class DiaryId {
    private final Long value;
    
    private DiaryId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("日记ID不能为空或非正数");
        }
        this.value = value;
    }
    
    public static DiaryId of(Long value) {
        return new DiaryId(value);
    }
    
    public Long getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiaryId diaryId = (DiaryId) o;
        return Objects.equals(value, diaryId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "DiaryId{" + value + '}';
    }
}
