package com.crazydream.domain.goal.model.valueobject;

import java.util.Objects;

/**
 * 目标进度值对象
 * 封装目标完成进度（0-100）
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public class GoalProgress {
    private static final int MIN_PROGRESS = 0;
    private static final int MAX_PROGRESS = 100;
    
    private final Integer value;
    
    private GoalProgress(Integer value) {
        if (value == null) {
            value = 0; // 默认进度为0
        }
        if (value < MIN_PROGRESS || value > MAX_PROGRESS) {
            throw new IllegalArgumentException("目标进度必须在" + MIN_PROGRESS + "-" + MAX_PROGRESS + "之间");
        }
        this.value = value;
    }
    
    public static GoalProgress of(Integer value) {
        return new GoalProgress(value);
    }
    
    public static GoalProgress zero() {
        return new GoalProgress(0);
    }
    
    public static GoalProgress completed() {
        return new GoalProgress(100);
    }
    
    public Integer getValue() {
        return value;
    }
    
    public boolean isCompleted() {
        return value == MAX_PROGRESS;
    }
    
    public boolean isNotStarted() {
        return value == MIN_PROGRESS;
    }
    
    public GoalProgress increase(int amount) {
        return new GoalProgress(Math.min(value + amount, MAX_PROGRESS));
    }
    
    public GoalProgress decrease(int amount) {
        return new GoalProgress(Math.max(value - amount, MIN_PROGRESS));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoalProgress that = (GoalProgress) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value + "%";
    }
}
