package com.crazydream.application.achievement.service;

import com.crazydream.domain.achievement.model.valueobject.AchievementStatistics;
import com.crazydream.domain.goal.model.aggregate.Goal;
import com.crazydream.domain.goal.repository.GoalRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.user.model.aggregate.User;
import com.crazydream.domain.user.repository.UserRepository;
import com.crazydream.infrastructure.persistence.mapper.DiaryPersistenceMapper;
import com.crazydream.infrastructure.persistence.mapper.TodoPersistenceMapper;
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
    
    @Autowired
    private DiaryPersistenceMapper diaryMapper;
    
    @Autowired
    private TodoPersistenceMapper todoMapper;
    
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
        
        // 完成率(不包含已放弃目标)
        int activeGoals = goalRepository.countActiveByUserId(userId);
        double completionRate = activeGoals > 0 ? (double) completedGoals / activeGoals : 0.0;
            
        // 日记统计
        int totalDiaries = diaryMapper.countByUserId(userIdValue);
        int consecutiveDiaryDays = diaryMapper.countConsecutiveDays(userIdValue);
            
        // 待办统计
        int completedTodos = todoMapper.countCompletedByUserId(userIdValue);
        int highPriorityCompletedCount = todoMapper.countHighPriorityCompleted(userIdValue);
        int consecutiveTodoDays = todoMapper.countConsecutiveDays(userIdValue);
            
        return new AchievementStatistics.Builder()
                .totalGoals(totalGoals)
                .completedGoals(completedGoals)
                .consecutiveDays(consecutiveDays)
                .maxConsecutiveDays(calculateMaxConsecutiveDays(goals))
                .categoryGoalCounts(categoryCounts)
                .completedCategoriesCount(completedCategoriesCount)
                .earlyBirdCount(earlyBirdCount)
                .nightOwlCount(nightOwlCount)
                .speedMasterCount(speedMasterCount)
                .deadlineKeeperCount(deadlineKeeperCount)
                .registrationDays(registrationDays)
                .completionRate(completionRate)
                .totalDiaries(totalDiaries)
                .consecutiveDiaryDays(consecutiveDiaryDays)
                .completedTodos(completedTodos)
                .highPriorityCompletedCount(highPriorityCompletedCount)
                .consecutiveTodoDays(consecutiveTodoDays)
                .build();
    }
    
    /**
     * 计算当前连续完成天数
     * 从今天往前推，连续每天都有完成目标
     */
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
        
        // 从今天开始往前数，每天都有完成记录才计数
        while (completionDates.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        
        return streak;
    }
    
    /**
     * 计算历史最大连续完成天数
     * 遍历所有日期，找出最长的连续区间
     */
    private int calculateMaxConsecutiveDays(List<Goal> goals) {
        // 获取所有完成目标的日期并排序
        List<LocalDate> completionDates = goals.stream()
                .filter(g -> g.getStatus() == com.crazydream.domain.goal.model.valueobject.GoalStatus.COMPLETED)
                .map(g -> g.getUpdateTime())
                .filter(dt -> dt != null)
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        
        if (completionDates.isEmpty()) {
            return 0;
        }
        
        int maxStreak = 1;
        int currentStreak = 1;
        
        // 遍历排序后的日期列表，计算最大连续天数
        for (int i = 1; i < completionDates.size(); i++) {
            LocalDate prevDate = completionDates.get(i - 1);
            LocalDate currDate = completionDates.get(i);
            
            // 如果当前日期与前一个日期相差1天，则为连续
            if (ChronoUnit.DAYS.between(prevDate, currDate) == 1) {
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                // 断开了，重新开始计数
                currentStreak = 1;
            }
        }
        
        return maxStreak;
    }
}
