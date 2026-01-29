package com.crazydream.domain.goal.event;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 目标完成事件
 * 当目标状态变为已完成时发布此事件
 */
public class GoalCompletedEvent {
    
    private final Long goalId;
    private final Long userId;
    private final Long categoryId;
    private final LocalDateTime completedTime;
    private final long completionDurationHours;  // 从创建到完成的时长(小时)
    
    public GoalCompletedEvent(Long goalId, Long userId, Long categoryId, 
                             LocalDateTime createTime, LocalDateTime completedTime) {
        this.goalId = goalId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.completedTime = completedTime;
        
        // 计算完成时长
        if (createTime != null && completedTime != null) {
            this.completionDurationHours = ChronoUnit.HOURS.between(createTime, completedTime);
        } else {
            this.completionDurationHours = 0;
        }
    }
    
    public Long getGoalId() {
        return goalId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public LocalDateTime getCompletedTime() {
        return completedTime;
    }
    
    public long getCompletionDurationHours() {
        return completionDurationHours;
    }
    
    @Override
    public String toString() {
        return "GoalCompletedEvent{" +
                "goalId=" + goalId +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", completedTime=" + completedTime +
                ", completionDurationHours=" + completionDurationHours +
                '}';
    }
}
