package com.crazydream.domain.goal.repository;

import com.crazydream.domain.goal.model.aggregate.Goal;
import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.shared.model.CategoryId;

import java.util.List;
import java.util.Optional;

/**
 * 目标仓储接口（防腐层）
 * 定义在 Domain 层，实现在 Infrastructure 层
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public interface GoalRepository {
    
    /**
     * 保存目标（新增或更新）
     */
    Goal save(Goal goal);
    
    /**
     * 根据ID查找目标
     */
    Optional<Goal> findById(GoalId id);
    
    /**
     * 根据用户ID查找所有目标
     */
    List<Goal> findByUserId(UserId userId);
    
    /**
     * 根据分类ID和用户ID查找目标
     */
    List<Goal> findByCategoryIdAndUserId(CategoryId categoryId, UserId userId);
    
    /**
     * 删除目标
     */
    boolean delete(GoalId id);
    
    /**
     * 批量删除目标
     */
    int batchDelete(List<GoalId> ids);
    
    /**
     * 获取用户最近更新的目标
     */
    List<Goal> findRecentByUserId(UserId userId, int limit);
    
    /**
     * 获取今日提醒的目标
     */
    List<Goal> findTodayReminderGoals(UserId userId, String date);
    
    /**
     * 统计用户已完成的目标数量
     */
    int countCompletedByUserId(UserId userId);
    
    /**
     * 统计用户的有效目标数量（不包含已放弃的目标）
     */
    int countActiveByUserId(UserId userId);
}
