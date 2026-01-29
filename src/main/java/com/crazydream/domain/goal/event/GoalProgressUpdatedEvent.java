package com.crazydream.domain.goal.event;

import java.time.LocalDateTime;

/**
 * 目标进度更新事件
 * 当目标进度发生变化时发布此事件
 */
public class GoalProgressUpdatedEvent {
    
    private final Long goalId;
    private final Long userId;
    private final int oldProgress;
    private final int newProgress;
    private final LocalDateTime updateTime;
    
    public GoalProgressUpdatedEvent(Long goalId, Long userId, int oldProgress, int newProgress) {
        this.goalId = goalId;
        this.userId = userId;
        this.oldProgress = oldProgress;
        this.newProgress = newProgress;
        this.updateTime = LocalDateTime.now();
    }
    
    public Long getGoalId() {
        return goalId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public int getOldProgress() {
        return oldProgress;
    }
    
    public int getNewProgress() {
        return newProgress;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    /**
     * 判断是否达到100%完成
     */
    public boolean isCompleted() {
        return newProgress >= 100;
    }
    
    /**
     * 获取进度增量
     */
    public int getProgressDelta() {
        return newProgress - oldProgress;
    }
    
    @Override
    public String toString() {
        return "GoalProgressUpdatedEvent{" +
                "goalId=" + goalId +
                ", userId=" + userId +
                ", oldProgress=" + oldProgress +
                ", newProgress=" + newProgress +
                ", updateTime=" + updateTime +
                '}';
    }
}
