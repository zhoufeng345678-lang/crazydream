package com.crazydream.domain.subgoal.model.aggregate;

import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.goal.model.valueobject.GoalProgress;
import com.crazydream.domain.subgoal.model.valueobject.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 子目标聚合根（充血模型）
 * SubGoal 是独立的聚合根
 */
public class SubGoal {
    private SubGoalId id;
    private GoalId goalId;  // 关联的Goal ID
    private SubGoalTitle title;
    private String description;
    private GoalProgress progress;
    private SubGoalStatus status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private SubGoal() {}
    
    /**
     * 创建子目标
     */
    public static SubGoal create(GoalId goalId, SubGoalTitle title, String description) {
        SubGoal subGoal = new SubGoal();
        subGoal.goalId = goalId;
        subGoal.title = title;
        subGoal.description = description;
        subGoal.progress = GoalProgress.zero();
        subGoal.status = SubGoalStatus.NOT_STARTED;
        subGoal.createTime = LocalDateTime.now();
        subGoal.updateTime = LocalDateTime.now();
        return subGoal;
    }
    
    /**
     * 重建子目标（从数据库加载）
     */
    public static SubGoal rebuild(SubGoalId id, GoalId goalId, SubGoalTitle title,
                                  String description, GoalProgress progress, SubGoalStatus status,
                                  LocalDateTime createTime, LocalDateTime updateTime) {
        SubGoal subGoal = new SubGoal();
        subGoal.id = id;
        subGoal.goalId = goalId;
        subGoal.title = title;
        subGoal.description = description;
        subGoal.progress = progress;
        subGoal.status = status;
        subGoal.createTime = createTime;
        subGoal.updateTime = updateTime;
        return subGoal;
    }
    
    // ==================== 业务行为 ====================
    
    public void start() {
        this.status = SubGoalStatus.IN_PROGRESS;
        this.updateTime = LocalDateTime.now();
    }
    
    public void updateProgress(int newProgress) {
        this.progress = GoalProgress.of(newProgress);
        if (progress.isCompleted()) {
            this.complete();
        }
        this.updateTime = LocalDateTime.now();
    }
    
    public void complete() {
        this.status = SubGoalStatus.COMPLETED;
        this.progress = GoalProgress.completed();
        this.updateTime = LocalDateTime.now();
    }
    
    public void update(SubGoalTitle title, String description) {
        this.title = title;
        this.description = description;
        this.updateTime = LocalDateTime.now();
    }
    
    public boolean belongsToGoal(GoalId goalId) {
        return this.goalId.equals(goalId);
    }
    
    // ==================== Getters ====================
    
    public SubGoalId getId() { return id; }
    public GoalId getGoalId() { return goalId; }
    public SubGoalTitle getTitle() { return title; }
    public String getDescription() { return description; }
    public GoalProgress getProgress() { return progress; }
    public SubGoalStatus getStatus() { return status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    
    public void setId(SubGoalId id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubGoal subGoal = (SubGoal) o;
        return Objects.equals(id, subGoal.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
