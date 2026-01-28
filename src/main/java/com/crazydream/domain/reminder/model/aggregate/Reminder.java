package com.crazydream.domain.reminder.model.aggregate;

import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.reminder.model.valueobject.ReminderId;
import com.crazydream.domain.shared.model.UserId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 提醒聚合根（充血模型）
 */
public class Reminder {
    private ReminderId id;
    private UserId userId;
    private GoalId goalId;
    private String title;
    private LocalDateTime remindTime;
    private boolean read;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private Reminder() {}
    
    public static Reminder create(UserId userId, GoalId goalId, String title, LocalDateTime remindTime) {
        Reminder reminder = new Reminder();
        reminder.userId = userId;
        reminder.goalId = goalId;
        reminder.title = title;
        reminder.remindTime = remindTime;
        reminder.read = false;
        reminder.createTime = LocalDateTime.now();
        reminder.updateTime = LocalDateTime.now();
        return reminder;
    }
    
    public static Reminder rebuild(ReminderId id, UserId userId, GoalId goalId, String title,
                                   LocalDateTime remindTime, boolean read, 
                                   LocalDateTime createTime, LocalDateTime updateTime) {
        Reminder reminder = new Reminder();
        reminder.id = id;
        reminder.userId = userId;
        reminder.goalId = goalId;
        reminder.title = title;
        reminder.remindTime = remindTime;
        reminder.read = read;
        reminder.createTime = createTime;
        reminder.updateTime = updateTime;
        return reminder;
    }
    
    // ==================== 业务行为 ====================
    
    public void markAsRead() {
        this.read = true;
        this.updateTime = LocalDateTime.now();
    }
    
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(remindTime);
    }
    
    public boolean belongsTo(UserId userId) {
        return this.userId.equals(userId);
    }
    
    // ==================== Getters ====================
    
    public ReminderId getId() { return id; }
    public UserId getUserId() { return userId; }
    public GoalId getGoalId() { return goalId; }
    public String getTitle() { return title; }
    public LocalDateTime getRemindTime() { return remindTime; }
    public boolean isRead() { return read; }
    public LocalDateTime getCreateTime() { return createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    
    public void setId(ReminderId id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return Objects.equals(id, reminder.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
