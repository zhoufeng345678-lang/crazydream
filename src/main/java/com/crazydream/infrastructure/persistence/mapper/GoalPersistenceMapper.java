package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.GoalPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 目标持久化 Mapper（新架构）
 * 仅负责数据库操作，不包含业务逻辑
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
@Mapper
public interface GoalPersistenceMapper {
    
    /**
     * 插入目标
     */
    int insert(GoalPO goal);
    
    /**
     * 更新目标
     */
    int update(GoalPO goal);
    
    /**
     * 根据ID查询
     */
    GoalPO selectById(Long id);
    
    /**
     * 根据用户ID查询
     */
    List<GoalPO> selectByUserId(Long userId);
    
    /**
     * 根据分类ID和用户ID查询
     */
    List<GoalPO> selectByCategoryIdAndUserId(@Param("categoryId") Long categoryId, 
                                              @Param("userId") Long userId);
    
    /**
     * 删除
     */
    int deleteById(Long id);
    
    /**
     * 批量删除
     */
    int batchDelete(@Param("ids") List<Long> ids);
    
    /**
     * 获取最近更新的目标
     */
    List<GoalPO> selectRecentByUserId(@Param("userId") Long userId, @Param("limit") int limit);
    
    /**
     * 获取今日提醒的目标
     */
    List<GoalPO> selectTodayReminderGoals(@Param("userId") Long userId, @Param("date") String date);
    
    /**
     * 统计用户已完成的目标数量
     */
    int countCompletedByUserId(@Param("userId") Long userId);
    
    /**
     * 统计用户的有效目标数量（不包含已放弃的目标）
     */
    int countActiveByUserId(@Param("userId") Long userId);
}
