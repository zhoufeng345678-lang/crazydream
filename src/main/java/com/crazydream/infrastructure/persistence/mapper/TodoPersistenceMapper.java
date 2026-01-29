package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.TodoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 待办事项持久化Mapper
 * 仅负责数据库操作,不包含业务逻辑
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Mapper
public interface TodoPersistenceMapper {
    
    /**
     * 插入待办
     */
    int insert(TodoPO todo);
    
    /**
     * 更新待办
     */
    int update(TodoPO todo);
    
    /**
     * 根据ID查询
     */
    TodoPO selectById(Long id);
    
    /**
     * 根据用户ID查询
     */
    List<TodoPO> selectByUserId(Long userId);
    
    /**
     * 根据用户ID和状态查询
     */
    List<TodoPO> selectByUserIdAndStatus(@Param("userId") Long userId, 
                                          @Param("status") String status);
    
    /**
     * 根据用户ID和优先级查询
     */
    List<TodoPO> selectByUserIdAndPriority(@Param("userId") Long userId, 
                                            @Param("priority") String priority);
    
    /**
     * 查找今日待办
     */
    List<TodoPO> selectTodayTodos(@Param("userId") Long userId, 
                                   @Param("date") LocalDate date);
    
    /**
     * 查找超期待办
     */
    List<TodoPO> selectOverdueTodos(@Param("userId") Long userId, 
                                     @Param("now") LocalDateTime now);
    
    /**
     * 查找需要提醒的待办
     */
    List<TodoPO> selectNeedRemind(@Param("startTime") LocalDateTime startTime, 
                                   @Param("endTime") LocalDateTime endTime);
    
    /**
     * 删除
     */
    int deleteById(Long id);
    
    /**
     * 统计用户已完成的待办数量
     */
    int countCompletedByUserId(@Param("userId") Long userId);
    
    /**
     * 统计用户完成的高优先级待办数量
     */
    int countHighPriorityCompleted(@Param("userId") Long userId);
    
    /**
     * 统计用户连续完成待办的天数
     */
    int countConsecutiveDays(@Param("userId") Long userId);
}
