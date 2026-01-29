package com.crazydream.domain.todo.model.aggregate;

import com.crazydream.domain.todo.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.goal.model.valueobject.GoalId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 待办事项聚合根(充血模型)
 * 封装待办事项的所有业务逻辑和行为
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public class Todo {
    // 聚合根ID
    private TodoId id;
    
    // 所属用户
    private UserId userId;
    
    // 待办标题
    private String title;
    
    // 待办描述
    private String description;
    
    // 优先级
    private TodoPriority priority;
    
    // 状态
    private TodoStatus status;
    
    // 截止时间
    private LocalDateTime dueDate;
    
    // 提醒时间
    private LocalDateTime remindTime;
    
    // 提醒是否已发送
    private boolean remindSent;
    
    // 关联的目标ID
    private GoalId relatedGoalId;
    
    // 完成时间
    private LocalDateTime completedTime;
    
    // 排序
    private int sortOrder;
    
    // 标签列表
    private List<String> tags;
    
    // 预计耗时(分钟)
    private Integer estimatedMinutes;
    
    // 实际耗时(分钟)
    private Integer actualMinutes;
    
    // 创建时间
    private LocalDateTime createTime;
    
    // 更新时间
    private LocalDateTime updateTime;
    
    // ==================== 构造函数 ====================
    
    /**
     * 私有构造函数,通过工厂方法创建
     */
    private Todo() {
        this.tags = new ArrayList<>();
    }
    
    /**
     * 创建新待办(工厂方法)
     */
    public static Todo create(UserId userId, String title, TodoPriority priority) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("待办标题不能为空");
        }
        
        Todo todo = new Todo();
        todo.userId = userId;
        todo.title = title;
        todo.priority = priority != null ? priority : TodoPriority.MEDIUM;
        todo.status = TodoStatus.PENDING;
        todo.remindSent = false;
        todo.sortOrder = 0;
        todo.createTime = LocalDateTime.now();
        todo.updateTime = LocalDateTime.now();
        
        return todo;
    }
    
    /**
     * 重建待办(从数据库加载)
     */
    public static Todo rebuild(TodoId id, UserId userId, String title, String description,
                                TodoPriority priority, TodoStatus status, LocalDateTime dueDate,
                                LocalDateTime remindTime, boolean remindSent, GoalId relatedGoalId,
                                LocalDateTime completedTime, int sortOrder, List<String> tags,
                                Integer estimatedMinutes, Integer actualMinutes,
                                LocalDateTime createTime, LocalDateTime updateTime) {
        Todo todo = new Todo();
        todo.id = id;
        todo.userId = userId;
        todo.title = title;
        todo.description = description;
        todo.priority = priority;
        todo.status = status;
        todo.dueDate = dueDate;
        todo.remindTime = remindTime;
        todo.remindSent = remindSent;
        todo.relatedGoalId = relatedGoalId;
        todo.completedTime = completedTime;
        todo.sortOrder = sortOrder;
        todo.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        todo.estimatedMinutes = estimatedMinutes;
        todo.actualMinutes = actualMinutes;
        todo.createTime = createTime;
        todo.updateTime = updateTime;
        
        return todo;
    }
    
    // ==================== 业务方法 ====================
    
    /**
     * 更新待办信息
     */
    public void updateInfo(String title, String description) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 设置优先级
     */
    public void setPriority(TodoPriority priority) {
        if (priority != null) {
            this.priority = priority;
            this.updateTime = LocalDateTime.now();
        }
    }
    
    /**
     * 设置截止时间
     */
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 开始执行待办
     */
    public void start() {
        if (!status.canTransitionTo(TodoStatus.IN_PROGRESS)) {
            throw new IllegalStateException("当前状态不允许开始执行: " + status);
        }
        this.status = TodoStatus.IN_PROGRESS;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 完成待办
     */
    public void complete(Integer actualMinutes) {
        if (!status.canTransitionTo(TodoStatus.COMPLETED)) {
            throw new IllegalStateException("当前状态不允许完成: " + status);
        }
        this.status = TodoStatus.COMPLETED;
        this.completedTime = LocalDateTime.now();
        this.actualMinutes = actualMinutes;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 取消待办
     */
    public void cancel() {
        if (!status.canTransitionTo(TodoStatus.CANCELLED)) {
            throw new IllegalStateException("当前状态不允许取消: " + status);
        }
        this.status = TodoStatus.CANCELLED;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 设置提醒
     */
    public void setReminder(LocalDateTime remindTime) {
        this.remindTime = remindTime;
        this.remindSent = false;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 标记提醒已发送
     */
    public void markRemindSent() {
        this.remindSent = true;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 关联到目标
     */
    public void linkToGoal(GoalId goalId) {
        this.relatedGoalId = goalId;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 取消关联目标
     */
    public void unlinkGoal() {
        this.relatedGoalId = null;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 添加标签
     */
    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty() && !this.tags.contains(tag)) {
            this.tags.add(tag);
            this.updateTime = LocalDateTime.now();
        }
    }
    
    /**
     * 移除标签
     */
    public void removeTag(String tag) {
        this.tags.remove(tag);
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 设置预计耗时
     */
    public void setEstimatedMinutes(Integer minutes) {
        this.estimatedMinutes = minutes;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 是否超期
     */
    public boolean isOverdue() {
        if (dueDate == null || status.isCompleted()) {
            return false;
        }
        return LocalDateTime.now().isAfter(dueDate);
    }
    
    /**
     * 是否需要提醒
     */
    public boolean needsRemind(LocalDateTime now) {
        if (remindTime == null || remindSent || status.isCompleted()) {
            return false;
        }
        return !now.isBefore(remindTime);
    }
    
    // ==================== Getters and Setters ====================
    
    public TodoId getId() {
        return id;
    }
    
    public void setId(TodoId id) {
        this.id = id;
    }
    
    public UserId getUserId() {
        return userId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public TodoPriority getPriority() {
        return priority;
    }
    
    public TodoStatus getStatus() {
        return status;
    }
    
    public void setStatus(TodoStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    
    public LocalDateTime getRemindTime() {
        return remindTime;
    }
    
    public void setRemindTime(LocalDateTime remindTime) {
        this.remindTime = remindTime;
    }
    
    public boolean isRemindSent() {
        return remindSent;
    }
    
    public void setRemindSent(boolean remindSent) {
        this.remindSent = remindSent;
    }
    
    public GoalId getRelatedGoalId() {
        return relatedGoalId;
    }
    
    public void setRelatedGoalId(GoalId relatedGoalId) {
        this.relatedGoalId = relatedGoalId;
    }
    
    public LocalDateTime getCompletedTime() {
        return completedTime;
    }
    
    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }
    
    public int getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public List<String> getTags() {
        return new ArrayList<>(tags);
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
    }
    
    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }
    
    public Integer getActualMinutes() {
        return actualMinutes;
    }
    
    public void setActualMinutes(Integer actualMinutes) {
        this.actualMinutes = actualMinutes;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }
}
