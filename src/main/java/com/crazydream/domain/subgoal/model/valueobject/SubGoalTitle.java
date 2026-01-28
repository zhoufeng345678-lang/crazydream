package com.crazydream.domain.subgoal.model.valueobject;

import java.util.Objects;

/**
 * 子目标标题值对象
 */
public class SubGoalTitle {
    private static final int MAX_LENGTH = 100;
    
    private final String value;
    
    private SubGoalTitle(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("子目标标题不能为空");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("子目标标题长度不能超过" + MAX_LENGTH + "个字符");
        }
        this.value = value.trim();
    }
    
    public static SubGoalTitle of(String value) {
        return new SubGoalTitle(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubGoalTitle that = (SubGoalTitle) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
