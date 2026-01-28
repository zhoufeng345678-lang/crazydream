package com.crazydream.domain.goal.model.aggregate;

import com.crazydream.domain.goal.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.shared.model.CategoryId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 目标聚合根（充血模型）
 * 封装目标的所有业务逻辑和行为
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public class Goal {
    // 聚合根ID
    private GoalId id;
    
    // 所属用户
    private UserId userId;
    
    // 目标标题
    private GoalTitle title;
    
    // 目标描述
    private String description;
    
    // 分类ID
    private CategoryId categoryId;
    
    // 优先级
    private Priority priority;
    
    // 截止日期
    private LocalDateTime deadline;
    
    // 进度
    private GoalProgress progress;
    
    // 状态
    private GoalStatus status;
    
    // 目标图片URL
    private String imageUrl;
    
    // 创建时间
    private LocalDateTime createTime;
    
    // 更新时间
    private LocalDateTime updateTime;
    
    // ==================== 构造函数 ====================
    
    /**
     * 私有构造函数，通过工厂方法创建
     */
    private Goal() {
    }
    
    /**
     * 创建新目标（工厂方法）
     */
    public static Goal create(UserId userId, GoalTitle title, String description,
                             CategoryId categoryId, Priority priority, LocalDateTime deadline) {
        Goal goal = new Goal();
        goal.userId = userId;
        goal.title = title;
        goal.description = description;
        goal.categoryId = categoryId;
        goal.priority = priority != null ? priority : Priority.MEDIUM;
        goal.deadline = deadline;
        goal.progress = GoalProgress.zero();
        goal.status = GoalStatus.NOT_STARTED;
        goal.createTime = LocalDateTime.now();
        goal.updateTime = LocalDateTime.now();
        
        return goal;
    }
    
    /**
     * 重建目标（从数据库加载）
     */
    public static Goal rebuild(GoalId id, UserId userId, GoalTitle title, String description,
                               CategoryId categoryId, Priority priority, LocalDateTime deadline,
                               GoalProgress progress, GoalStatus status, String imageUrl,
                               LocalDateTime createTime, LocalDateTime updateTime) {
        Goal goal = new Goal();
        goal.id = id;
        goal.userId = userId;
        goal.title = title;
        goal.description = description;
        goal.categoryId = categoryId;
        goal.priority = priority;
        goal.deadline = deadline;
        goal.progress = progress;
        goal.status = status;
        goal.imageUrl = imageUrl;
        goal.createTime = createTime;
        goal.updateTime = updateTime;
        
        return goal;
    }
    
    // ==================== 业务行为 ====================
    
    /**
     * 开始执行目标
     */
    public void start() {
        if (!status.canTransitionTo(GoalStatus.IN_PROGRESS)) {
            throw new IllegalStateException("当前状态不能转换为进行中: " + status);
        }
        this.status = GoalStatus.IN_PROGRESS;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 更新目标进度
     */
    public void updateProgress(int newProgress) {
        if (status.isTerminal()) {
            throw new IllegalStateException("目标已结束，不能更新进度");
        }
        
        this.progress = GoalProgress.of(newProgress);
        
        // 如果目标处于未开始状态，且进度大于0，自动转为进行中状态
        if (status == GoalStatus.NOT_STARTED && newProgress > 0) {
            this.status = GoalStatus.IN_PROGRESS;
        }
        
        // 进度达到100%自动完成
        if (progress.isCompleted() && status != GoalStatus.COMPLETED) {
            this.complete();
        }
        
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 完成目标
     */
    public void complete() {
        if (!status.canTransitionTo(GoalStatus.COMPLETED)) {
            throw new IllegalStateException("当前状态不能转换为已完成: " + status);
        }
        this.status = GoalStatus.COMPLETED;
        this.progress = GoalProgress.completed();
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 放弃目标
     */
    public void abandon() {
        if (!status.canTransitionTo(GoalStatus.ABANDONED)) {
            throw new IllegalStateException("当前状态不能转换为已放弃: " + status);
        }
        this.status = GoalStatus.ABANDONED;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 更新目标信息
     */
    public void update(GoalTitle title, String description, CategoryId categoryId,
                      Priority priority, LocalDateTime deadline, String imageUrl) {
        if (status.isTerminal()) {
            throw new IllegalStateException("目标已结束，不能更新信息");
        }
        
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.priority = priority;
        this.deadline = deadline;
        this.imageUrl = imageUrl;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 设置目标图片
     */
    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 判断是否已逾期
     */
    public boolean isOverdue() {
        if (deadline == null || status.isTerminal()) {
            return false;
        }
        return LocalDateTime.now().isAfter(deadline);
    }
    
    /**
     * 判断是否属于指定用户
     */
    public boolean belongsTo(UserId userId) {
        return this.userId.equals(userId);
    }
    
    /**
     * 判断是否属于指定分类
     */
    public boolean belongsToCategory(CategoryId categoryId) {
        return this.categoryId != null && this.categoryId.equals(categoryId);
    }
    
    // ==================== Getters ====================
    
    public GoalId getId() {
        return id;
    }
    
    public UserId getUserId() {
        return userId;
    }
    
    public GoalTitle getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public CategoryId getCategoryId() {
        return categoryId;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public LocalDateTime getDeadline() {
        return deadline;
    }
    
    public GoalProgress getProgress() {
        return progress;
    }
    
    public GoalStatus getStatus() {
        return status;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    // 设置ID（仅供Repository使用）
    public void setId(GoalId id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return Objects.equals(id, goal.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", title=" + title +
                ", status=" + status +
                ", progress=" + progress +
                '}';
    }
}
