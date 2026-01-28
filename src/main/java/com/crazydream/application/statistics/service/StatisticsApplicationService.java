package com.crazydream.application.statistics.service;

import com.crazydream.application.statistics.dto.CategoryStatisticsDTO;
import com.crazydream.application.statistics.dto.DashboardStatisticsDTO;
import com.crazydream.application.statistics.dto.TrendsStatisticsDTO;
import com.crazydream.domain.goal.model.aggregate.Goal;
import com.crazydream.domain.goal.repository.GoalRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.user.model.aggregate.User;
import com.crazydream.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsApplicationService {
    
    @Autowired
    private GoalRepository goalRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 获取目标统计
     */
    public Map<String, Integer> getGoalStatistics(Long userId) {
        List<Goal> goals = goalRepository.findByUserId(UserId.of(userId));
        
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", goals.size());
        stats.put("inProgress", (int) goals.stream().filter(g -> "in_progress".equals(g.getStatus().getCode())).count());
        stats.put("completed", (int) goals.stream().filter(g -> "completed".equals(g.getStatus().getCode())).count());
        stats.put("notStarted", (int) goals.stream().filter(g -> "not_started".equals(g.getStatus().getCode())).count());
        
        return stats;
    }
    
    /**
     * 获取仪表盘统计
     */
    public DashboardStatisticsDTO getDashboardStatistics(Long userId) {
        Map<String, Integer> goalStats = getGoalStatistics(userId);
        
        User user = userRepository.findById(UserId.of(userId))
                .orElse(null);
        
        String username = user != null && user.getNickName() != null 
                ? user.getNickName().getValue() 
                : "User" + userId;
        
        return new DashboardStatisticsDTO(goalStats, userId, username);
    }
    
    /**
     * 获取趋势统计 - 简化实现
     */
    public TrendsStatisticsDTO getTrendsStatistics(Long userId) {
        // 简化实现：返回空趋势数据
        // 实际应该根据目标的完成时间统计
        return new TrendsStatisticsDTO(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
    
    /**
     * 获取分类统计
     */
    public List<CategoryStatisticsDTO> getCategoryStatistics(Long userId) {
        List<Goal> goals = goalRepository.findByUserId(UserId.of(userId));
        
        // 按分类分组统计
        Map<Long, List<Goal>> categoryGoals = goals.stream()
                .filter(g -> g.getCategoryId() != null)
                .collect(Collectors.groupingBy(g -> g.getCategoryId().getValue()));
        
        List<CategoryStatisticsDTO> stats = new ArrayList<>();
        for (Map.Entry<Long, List<Goal>> entry : categoryGoals.entrySet()) {
            Long categoryId = entry.getKey();
            List<Goal> categoryGoalList = entry.getValue();
            
            int total = categoryGoalList.size();
            int completed = (int) categoryGoalList.stream()
                    .filter(g -> "completed".equals(g.getStatus().getCode()))
                    .count();
            int inProgress = (int) categoryGoalList.stream()
                    .filter(g -> "in_progress".equals(g.getStatus().getCode()))
                    .count();
            
            stats.add(new CategoryStatisticsDTO(
                    categoryId,
                    "Category " + categoryId,
                    total,
                    completed,
                    inProgress
            ));
        }
        
        return stats;
    }
}
