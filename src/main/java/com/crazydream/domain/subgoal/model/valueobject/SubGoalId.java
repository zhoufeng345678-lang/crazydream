package com.crazydream.domain.subgoal.model.valueobject;

import java.util.Objects;

/**
 * 子目标ID值对象
 */
public class SubGoalId {
    private final Long value;
    
    private SubGoalId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("子目标ID不能为空或非正数");
        }
        this.value = value;
    }
    
    public static SubGoalId of(Long value) {
        return new SubGoalId(value);
    }
    
    public Long getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubGoalId that = (SubGoalId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
