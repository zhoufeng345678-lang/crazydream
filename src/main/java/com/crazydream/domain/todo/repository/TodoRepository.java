package com.crazydream.domain.todo.repository;

import com.crazydream.domain.todo.model.aggregate.Todo;
import com.crazydream.domain.todo.model.valueobject.TodoId;
import com.crazydream.domain.todo.model.valueobject.TodoStatus;
import com.crazydream.domain.todo.model.valueobject.TodoPriority;
import com.crazydream.domain.shared.model.UserId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 待办仓储接口(防腐层)
 * 定义在Domain层,实现在Infrastructure层
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public interface TodoRepository {
    
    /**
     * 保存待办(新增或更新)
     */
    Todo save(Todo todo);
    
    /**
     * 根据ID查找待办
     */
    Optional<Todo> findById(TodoId id);
    
    /**
     * 根据用户ID查找所有待办
     */
    List<Todo> findByUserId(UserId userId);
    
    /**
     * 根据用户ID和状态查找待办
     */
    List<Todo> findByUserIdAndStatus(UserId userId, TodoStatus status);
    
    /**
     * 根据用户ID和优先级查找待办
     */
    List<Todo> findByUserIdAndPriority(UserId userId, TodoPriority priority);
    
    /**
     * 查找今日待办
     */
    List<Todo> findTodayTodos(UserId userId, LocalDate date);
    
    /**
     * 查找超期待办
     */
    List<Todo> findOverdueTodos(UserId userId, LocalDateTime now);
    
    /**
     * 查找需要提醒的待办
     */
    List<Todo> findNeedRemind(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 删除待办
     */
    boolean delete(TodoId id);
    
    /**
     * 统计用户已完成的待办数量
     */
    int countCompletedByUserId(UserId userId);
    
    /**
     * 统计用户完成的高优先级待办数量
     */
    int countHighPriorityCompleted(UserId userId);
    
    /**
     * 统计用户连续完成待办的天数
     */
    int countConsecutiveDays(UserId userId);
}
