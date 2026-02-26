package com.crazydream.domain.family.model.valueobject;

import java.util.Objects;

/**
 * 家庭成员ID值对象
 */
public class FamilyMemberId {
    private final Long value;
    
    private FamilyMemberId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("家庭成员ID不能为空或小于等于0");
        }
        this.value = value;
    }
    
    public static FamilyMemberId of(Long value) {
        return new FamilyMemberId(value);
    }
    
    public Long getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyMemberId that = (FamilyMemberId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "FamilyMemberId{" + value + '}';
    }
}
