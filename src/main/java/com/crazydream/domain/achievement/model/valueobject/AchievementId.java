package com.crazydream.domain.achievement.model.valueobject;

import java.util.Objects;

public class AchievementId {
    private final Long value;
    
    private AchievementId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("成就ID不能为空或非正数");
        }
        this.value = value;
    }
    
    public static AchievementId of(Long value) {
        return new AchievementId(value);
    }
    
    public Long getValue() { return value; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AchievementId that = (AchievementId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
