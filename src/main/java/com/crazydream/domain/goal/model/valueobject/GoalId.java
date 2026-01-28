package com.crazydream.domain.goal.model.valueobject;

import java.util.Objects;

/**
 * 目标ID值对象
 * 封装目标的唯一标识符
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public class GoalId {
    private final Long value;
    
    private GoalId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("目标ID不能为空或非正数");
        }
        this.value = value;
    }
    
    public static GoalId of(Long value) {
        return new GoalId(value);
    }
    
    public Long getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoalId goalId = (GoalId) o;
        return Objects.equals(value, goalId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "GoalId{" + value + '}';
    }
}
