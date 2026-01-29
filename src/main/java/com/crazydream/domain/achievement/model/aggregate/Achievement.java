package com.crazydream.domain.achievement.model.aggregate;

import com.crazydream.domain.achievement.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 成就聚合根（充血模型）
 */
public class Achievement {
    private AchievementId id;
    private UserId userId;
    private AchievementType type;
    private boolean unlocked;
    private LocalDateTime unlockedTime;
    private LocalDateTime createTime;
    
    // 新增字段
    private Integer progress;        // 当前进度值
    private Integer target;          // 目标进度值
    private String category;         // 成就分类
    private String tier;             // 成就等级
    private String icon;             // 成就图标emoji
    
    private Achievement() {}
    
    public static Achievement create(UserId userId, AchievementType type) {
        Achievement achievement = new Achievement();
        achievement.userId = userId;
        achievement.type = type;
        achievement.unlocked = false;
        achievement.createTime = LocalDateTime.now();
        
        // 初始化新字段
        achievement.progress = 0;
        achievement.target = type.getTarget();
        achievement.category = type.getCategory();
        achievement.tier = type.getTier();
        achievement.icon = type.getIcon();
        
        return achievement;
    }
    
    public static Achievement rebuild(AchievementId id, UserId userId, AchievementType type,
                                     boolean unlocked, LocalDateTime unlockedTime, 
                                     LocalDateTime createTime) {
        Achievement achievement = new Achievement();
        achievement.id = id;
        achievement.userId = userId;
        achievement.type = type;
        achievement.unlocked = unlocked;
        achievement.unlockedTime = unlockedTime;
        achievement.createTime = createTime;
        
        // 初始化新字段（如果未设置，使用类型默认值）
        achievement.progress = 0;
        achievement.target = type.getTarget();
        achievement.category = type.getCategory();
        achievement.tier = type.getTier();
        achievement.icon = type.getIcon();
        
        return achievement;
    }
    
    // ==================== 业务行为 ====================
    
    public void unlock() {
        if (unlocked) {
            throw new IllegalStateException("成就已解锁");
        }
        this.unlocked = true;
        this.unlockedTime = LocalDateTime.now();
    }
    
    /**
     * 更新成就进度
     * @param current 当前进度值
     * @param target 目标进度值
     */
    public void updateProgress(int current, int target) {
        this.progress = current;
        this.target = target;
    }
    
    /**
     * 检查成就是否可以解锁
     * 使用统计数据进行判断，支持所有成就类型
     * 
     * @param statistics 成就统计数据
     * @return 是否可以解锁
     */
    public boolean canUnlock(com.crazydream.domain.achievement.model.valueobject.AchievementStatistics statistics) {
        switch (type) {
            // 目标完成数量系列
            case FIRST_GOAL:
                return statistics.getCompletedGoals() >= 1;
            case GOAL_COMPLETED_10:
                return statistics.getCompletedGoals() >= 10;
            case GOAL_COMPLETED_30:
                return statistics.getCompletedGoals() >= 30;
            case GOAL_COMPLETED_50:
                return statistics.getCompletedGoals() >= 50;
            case GOAL_COMPLETED_100:
                return statistics.getCompletedGoals() >= 100;
            case GOAL_COMPLETED_200:
                return statistics.getCompletedGoals() >= 200;
            
            // 连续打卡系列
            case CONSECUTIVE_3:
                return statistics.getConsecutiveDays() >= 3;
            case CONSECUTIVE_7:
                return statistics.getConsecutiveDays() >= 7;
            case CONSECUTIVE_14:
                return statistics.getConsecutiveDays() >= 14;
            case CONSECUTIVE_30:
                return statistics.getConsecutiveDays() >= 30;
            case CONSECUTIVE_100:
                return statistics.getConsecutiveDays() >= 100;
            
            // 分类专注系列
            case CATEGORY_MASTER_10:
                return statistics.getMaxCategoryGoalCount() >= 10;
            case CATEGORY_MASTER_30:
                return statistics.getMaxCategoryGoalCount() >= 30;
            case ALL_CATEGORY_EXPLORER:
                // 需要在所有分类都完成至少1个，默认6个预设分类
                return statistics.getCompletedCategoriesCount() >= 6;
            
            // 效率提升系列
            case EARLY_BIRD:
                return statistics.getEarlyBirdCount() >= 5;
            case NIGHT_OWL:
                return statistics.getNightOwlCount() >= 5;
            case SPEED_MASTER:
                return statistics.getSpeedMasterCount() >= 10;
            case DEADLINE_KEEPER:
                return statistics.getDeadlineKeeperCount() >= 20;
            
            // 里程碑系列
            case FIRST_WEEK:
                return statistics.getRegistrationDays() >= 7;
            case FIRST_MONTH:
                return statistics.getRegistrationDays() >= 30;
            case ONE_YEAR:
                return statistics.getRegistrationDays() >= 365;
            case HIGH_COMPLETION_RATE:
                return statistics.getCompletionRate() >= 0.9 
                    && statistics.getCompletedGoals() >= 20;
            
            // 日记系列
            case FIRST_DIARY:
                return statistics.getTotalDiaries() >= 1;
            case DIARY_WEEK:
                return statistics.getConsecutiveDiaryDays() >= 7;
            case DIARY_MONTH:
                return statistics.getConsecutiveDiaryDays() >= 30;
            case DIARY_100:
                return statistics.getTotalDiaries() >= 100;
            
            // 待办系列
            case FIRST_TODO:
                return statistics.getCompletedTodos() >= 1;
            case TODO_COMPLETED_10:
                return statistics.getCompletedTodos() >= 10;
            case TODO_PRIORITY:
                return statistics.getHighPriorityCompletedCount() >= 10;
            case TODO_STREAK:
                return statistics.getConsecutiveTodoDays() >= 7;
            
            // 等级提升(预留)
            case LEVEL_UP:
                return false; // 需要单独处理用户等级
            
            default:
                return false;
        }
    }
    
    /**
     * 兼容旧的canUnlock方法，保留以避免破坏现有代码
     * @deprecated 使用 {@link #canUnlock(AchievementStatistics)} 代替
     */
    @Deprecated
    public boolean canUnlock(int goalCount, int level) {
        switch (type) {
            case FIRST_GOAL:
                return goalCount >= 1;
            case GOAL_COMPLETED_10:
                return goalCount >= 10;
            case GOAL_COMPLETED_30:
                return goalCount >= 30;
            case GOAL_COMPLETED_50:
                return goalCount >= 50;
            case GOAL_COMPLETED_100:
                return goalCount >= 100;
            case GOAL_COMPLETED_200:
                return goalCount >= 200;
            case LEVEL_UP:
                return level >= 2;
            default:
                return false;
        }
    }
    
    public boolean belongsTo(UserId userId) {
        return this.userId.equals(userId);
    }
    
    // ==================== Getters ====================
    
    public AchievementId getId() { return id; }
    public UserId getUserId() { return userId; }
    public AchievementType getType() { return type; }
    public boolean isUnlocked() { return unlocked; }
    public LocalDateTime getUnlockedTime() { return unlockedTime; }
    public LocalDateTime getCreateTime() { return createTime; }
    public Integer getProgress() { return progress; }
    public Integer getTarget() { return target; }
    public String getCategory() { return category; }
    public String getTier() { return tier; }
    public String getIcon() { return icon; }
    
    public void setId(AchievementId id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Achievement that = (Achievement) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
