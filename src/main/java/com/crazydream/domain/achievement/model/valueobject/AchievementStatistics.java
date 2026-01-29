package com.crazydream.domain.achievement.model.valueobject;

import java.util.HashMap;
import java.util.Map;

/**
 * 成就统计数据值对象
 * 封装成就检查所需的所有统计数据
 */
public class AchievementStatistics {
    
    // 目标统计
    private final int totalGoals;           // 总目标数
    private final int completedGoals;       // 已完成目标数
    
    // 连续打卡统计
    private final int consecutiveDays;      // 当前连续完成天数
    private final int maxConsecutiveDays;   // 最长连续完成天数
    
    // 分类统计
    private final Map<Long, Integer> categoryGoalCounts;  // categoryId -> count
    private final int completedCategoriesCount;           // 已完成目标的分类数
    
    // 时间维度统计
    private final int earlyBirdCount;       // 早上6-8点完成的目标数
    private final int nightOwlCount;        // 晚上22-24点完成的目标数
    private final int speedMasterCount;     // 24小时内完成的目标数
    private final int deadlineKeeperCount;  // 提前完成的目标数
    
    // 里程碑统计
    private final int registrationDays;     // 注册天数
    private final double completionRate;    // 目标完成率 (0.0-1.0)
    
    // 日记统计
    private final int totalDiaries;         // 日记总数
    private final int consecutiveDiaryDays; // 连续写日记天数
    
    // 待办统计
    private final int completedTodos;       // 已完成待办数
    private final int highPriorityCompletedCount; // 完成的高优先级待办数
    private final int consecutiveTodoDays;  // 连续完成待办天数
    
    private AchievementStatistics(Builder builder) {
        this.totalGoals = builder.totalGoals;
        this.completedGoals = builder.completedGoals;
        this.consecutiveDays = builder.consecutiveDays;
        this.maxConsecutiveDays = builder.maxConsecutiveDays;
        this.categoryGoalCounts = builder.categoryGoalCounts;
        this.completedCategoriesCount = builder.completedCategoriesCount;
        this.earlyBirdCount = builder.earlyBirdCount;
        this.nightOwlCount = builder.nightOwlCount;
        this.speedMasterCount = builder.speedMasterCount;
        this.deadlineKeeperCount = builder.deadlineKeeperCount;
        this.registrationDays = builder.registrationDays;
        this.completionRate = builder.completionRate;
        this.totalDiaries = builder.totalDiaries;
        this.consecutiveDiaryDays = builder.consecutiveDiaryDays;
        this.completedTodos = builder.completedTodos;
        this.highPriorityCompletedCount = builder.highPriorityCompletedCount;
        this.consecutiveTodoDays = builder.consecutiveTodoDays;
    }
    
    public int getTotalGoals() {
        return totalGoals;
    }
    
    public int getCompletedGoals() {
        return completedGoals;
    }
    
    public int getConsecutiveDays() {
        return consecutiveDays;
    }
    
    public int getMaxConsecutiveDays() {
        return maxConsecutiveDays;
    }
    
    public Map<Long, Integer> getCategoryGoalCounts() {
        return categoryGoalCounts;
    }
    
    public int getCompletedCategoriesCount() {
        return completedCategoriesCount;
    }
    
    public int getEarlyBirdCount() {
        return earlyBirdCount;
    }
    
    public int getNightOwlCount() {
        return nightOwlCount;
    }
    
    public int getSpeedMasterCount() {
        return speedMasterCount;
    }
    
    public int getDeadlineKeeperCount() {
        return deadlineKeeperCount;
    }
    
    public int getRegistrationDays() {
        return registrationDays;
    }
    
    public double getCompletionRate() {
        return completionRate;
    }
    
    public int getTotalDiaries() {
        return totalDiaries;
    }
    
    public int getConsecutiveDiaryDays() {
        return consecutiveDiaryDays;
    }
    
    public int getCompletedTodos() {
        return completedTodos;
    }
    
    public int getHighPriorityCompletedCount() {
        return highPriorityCompletedCount;
    }
    
    public int getConsecutiveTodoDays() {
        return consecutiveTodoDays;
    }
    
    /**
     * 获取指定分类的完成目标数
     */
    public int getCategoryGoalCount(Long categoryId) {
        return categoryGoalCounts.getOrDefault(categoryId, 0);
    }
    
    /**
     * 获取最大的单分类完成数
     */
    public int getMaxCategoryGoalCount() {
        return categoryGoalCounts.values().stream()
                .max(Integer::compareTo)
                .orElse(0);
    }
    
    public static class Builder {
        private int totalGoals = 0;
        private int completedGoals = 0;
        private int consecutiveDays = 0;
        private int maxConsecutiveDays = 0;
        private Map<Long, Integer> categoryGoalCounts = new HashMap<>();
        private int completedCategoriesCount = 0;
        private int earlyBirdCount = 0;
        private int nightOwlCount = 0;
        private int speedMasterCount = 0;
        private int deadlineKeeperCount = 0;
        private int registrationDays = 0;
        private double completionRate = 0.0;
        private int totalDiaries = 0;
        private int consecutiveDiaryDays = 0;
        private int completedTodos = 0;
        private int highPriorityCompletedCount = 0;
        private int consecutiveTodoDays = 0;
        
        public Builder totalGoals(int totalGoals) {
            this.totalGoals = totalGoals;
            return this;
        }
        
        public Builder completedGoals(int completedGoals) {
            this.completedGoals = completedGoals;
            return this;
        }
        
        public Builder consecutiveDays(int consecutiveDays) {
            this.consecutiveDays = consecutiveDays;
            return this;
        }
        
        public Builder maxConsecutiveDays(int maxConsecutiveDays) {
            this.maxConsecutiveDays = maxConsecutiveDays;
            return this;
        }
        
        public Builder categoryGoalCounts(Map<Long, Integer> categoryGoalCounts) {
            this.categoryGoalCounts = categoryGoalCounts != null ? categoryGoalCounts : new HashMap<>();
            return this;
        }
        
        public Builder completedCategoriesCount(int completedCategoriesCount) {
            this.completedCategoriesCount = completedCategoriesCount;
            return this;
        }
        
        public Builder earlyBirdCount(int earlyBirdCount) {
            this.earlyBirdCount = earlyBirdCount;
            return this;
        }
        
        public Builder nightOwlCount(int nightOwlCount) {
            this.nightOwlCount = nightOwlCount;
            return this;
        }
        
        public Builder speedMasterCount(int speedMasterCount) {
            this.speedMasterCount = speedMasterCount;
            return this;
        }
        
        public Builder deadlineKeeperCount(int deadlineKeeperCount) {
            this.deadlineKeeperCount = deadlineKeeperCount;
            return this;
        }
        
        public Builder registrationDays(int registrationDays) {
            this.registrationDays = registrationDays;
            return this;
        }
        
        public Builder completionRate(double completionRate) {
            this.completionRate = completionRate;
            return this;
        }
        
        public Builder totalDiaries(int totalDiaries) {
            this.totalDiaries = totalDiaries;
            return this;
        }
        
        public Builder consecutiveDiaryDays(int consecutiveDiaryDays) {
            this.consecutiveDiaryDays = consecutiveDiaryDays;
            return this;
        }
        
        public Builder completedTodos(int completedTodos) {
            this.completedTodos = completedTodos;
            return this;
        }
        
        public Builder highPriorityCompletedCount(int highPriorityCompletedCount) {
            this.highPriorityCompletedCount = highPriorityCompletedCount;
            return this;
        }
        
        public Builder consecutiveTodoDays(int consecutiveTodoDays) {
            this.consecutiveTodoDays = consecutiveTodoDays;
            return this;
        }
        
        public AchievementStatistics build() {
            return new AchievementStatistics(this);
        }
    }
}
