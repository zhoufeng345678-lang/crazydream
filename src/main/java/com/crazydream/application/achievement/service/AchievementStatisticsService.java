package com.crazydream.application.achievement.service;

import com.crazydream.domain.achievement.model.valueobject.AchievementStatistics;
import com.crazydream.domain.goal.model.aggregate.Goal;
import com.crazydream.domain.goal.repository.GoalRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.user.model.aggregate.User;
import com.crazydream.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 成就统计数据服务
 * 负责收集用户成就解锁所需的各类统计数据
 */
@Service
public class AchievementStatisticsService {
    
    @Autowired
    private GoalRepository goalRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public AchievementStatistics collectStatistics(Long userIdValue) {
        UserId userId = UserId.of(userIdValue);
        List<Goal> goals = goalRepository.findByUserId(userId);
        User user = userRepository.findById(userId)
                .orElse(null);
        
        int totalGoals = goals.size();
        int completedGoals = goalRepository.countCompletedByUserId(userId);
        
        // 计算当前连续完成天数
        int consecutiveDays = calculateCurrentConsecutiveDays(goals);
        
        // 分类统计
        Map<Long, Integer> categoryCounts = new HashMap<>();
        goals.stream()
                .filter(g -> g.getStatus() == com.crazydream.domain.goal.model.valueobject.GoalStatus.COMPLETED)
                .forEach(g -> {
                    if (g.getCategoryId() != null && g.getCategoryId().getValue() != null) {
                        Long categoryId = g.getCategoryId().getValue();
                        categoryCounts.merge(categoryId, 1, Integer::sum);
                    }
                });
        int completedCategoriesCount = categoryCounts.size();
        
        // 时间维度统计
        int earlyBirdCount = 0;
        int nightOwlCount = 0;
        int speedMasterCount = 0;
        int deadlineKeeperCount = 0;
        
        for (Goal goal : goals) {
            if (goal.getStatus() != com.crazydream.domain.goal.model.valueobject.GoalStatus.COMPLETED) {
                continue;
            }
            LocalDateTime createTime = goal.getCreateTime();
            LocalDateTime updateTime = goal.getUpdateTime();
            if (updateTime == null) {
                continue;
            }
            int hour = updateTime.getHour();
            if (hour >= 6 && hour < 8) {
                earlyBirdCount++;
            }
            if (hour >= 22 || hour < 0) {
                nightOwlCount++;
            }
            if (createTime != null) {
                long hoursBetween = ChronoUnit.HOURS.between(createTime, updateTime);
                if (hoursBetween <= 24) {
                    speedMasterCount++;
                }
            }
            if (goal.getDeadline() != null && updateTime.isBefore(goal.getDeadline())) {
                deadlineKeeperCount++;
            }
        }
        
        // 注册天数
        int registrationDays = 0;
        if (user != null && user.getCreateTime() != null) {
            LocalDate createDate = user.getCreateTime().toLocalDate();
            LocalDate today = LocalDate.now();
            registrationDays = (int) ChronoUnit.DAYS.between(createDate, today) + 1;
        }
        
        // 完成率（不包含已放弃目标）
        int activeGoals = goalRepository.countActiveByUserId(userId);
        double completionRate = activeGoals > 0 ? (double) completedGoals / activeGoals : 0.0;
        
        return new AchievementStatistics.Builder()
                .totalGoals(totalGoals)
                .completedGoals(completedGoals)
                .consecutiveDays(consecutiveDays)
                .maxConsecutiveDays(consecutiveDays)
                .categoryGoalCounts(categoryCounts)
                .completedCategoriesCount(completedCategoriesCount)
                .earlyBirdCount(earlyBirdCount)
                .nightOwlCount(nightOwlCount)
                .speedMasterCount(speedMasterCount)
                .deadlineKeeperCount(deadlineKeeperCount)
                .registrationDays(registrationDays)
                .completionRate(completionRate)
                .build();
    }
    
    private int calculateCurrentConsecutiveDays(List<Goal> goals) {
        // 获取所有完成目标的日期
        Set<LocalDate> completionDates = goals.stream()
                .filter(g -> g.getStatus() == com.crazydream.domain.goal.model.valueobject.GoalStatus.COMPLETED)
                .map(g -> g.getUpdateTime())
                .filter(dt -> dt != null)
                .map(LocalDateTime::toLocalDate)
                .collect(Collectors.toSet());
        
        if (completionDates.isEmpty()) {
            return 0;
        }
        
        int streak = 0;
        LocalDate today = LocalDate.now();
        LocalDate cursor = today;
        while (completionDates.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }
}
