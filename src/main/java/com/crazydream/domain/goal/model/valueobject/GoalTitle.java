package com.crazydream.domain.goal.model.valueobject;

import java.util.Objects;

/**
 * 目标标题值对象
 * 封装目标标题及其验证规则
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public class GoalTitle {
    private static final int MAX_LENGTH = 100;
    private static final int MIN_LENGTH = 1;
    
    private final String value;
    
    private GoalTitle(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("目标标题不能为空");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("目标标题长度不能超过" + MAX_LENGTH + "个字符");
        }
        if (value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("目标标题长度不能少于" + MIN_LENGTH + "个字符");
        }
        this.value = value.trim();
    }
    
    public static GoalTitle of(String value) {
        return new GoalTitle(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoalTitle goalTitle = (GoalTitle) o;
        return Objects.equals(value, goalTitle.value);
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
