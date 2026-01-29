package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.todo.model.aggregate.Todo;
import com.crazydream.domain.todo.model.valueobject.TodoId;
import com.crazydream.domain.todo.model.valueobject.TodoStatus;
import com.crazydream.domain.todo.model.valueobject.TodoPriority;
import com.crazydream.domain.todo.repository.TodoRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.converter.TodoConverter;
import com.crazydream.infrastructure.persistence.mapper.TodoPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.TodoPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 待办仓储实现(Infrastructure层)
 * 桥接领域模型与持久化
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Repository
public class TodoRepositoryImpl implements TodoRepository {
    
    @Autowired
    private TodoPersistenceMapper mapper;
    
    @Override
    public Todo save(Todo todo) {
        TodoPO po = TodoConverter.toPO(todo);
        
        if (todo.getId() == null) {
            // 新增
            mapper.insert(po);
            // 设置生成的ID回到领域对象
            todo.setId(TodoId.of(po.getId()));
        } else {
            // 更新
            mapper.update(po);
        }
        
        return todo;
    }
    
    @Override
    public Optional<Todo> findById(TodoId id) {
        TodoPO po = mapper.selectById(id.getValue());
        return Optional.ofNullable(TodoConverter.toDomain(po));
    }
    
    @Override
    public List<Todo> findByUserId(UserId userId) {
        List<TodoPO> poList = mapper.selectByUserId(userId.getValue());
        return poList.stream()
                .map(TodoConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Todo> findByUserIdAndStatus(UserId userId, TodoStatus status) {
        List<TodoPO> poList = mapper.selectByUserIdAndStatus(
                userId.getValue(), 
                status.getCode()
        );
        return poList.stream()
                .map(TodoConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Todo> findByUserIdAndPriority(UserId userId, TodoPriority priority) {
        List<TodoPO> poList = mapper.selectByUserIdAndPriority(
                userId.getValue(), 
                priority.getCode()
        );
        return poList.stream()
                .map(TodoConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Todo> findTodayTodos(UserId userId, LocalDate date) {
        List<TodoPO> poList = mapper.selectTodayTodos(userId.getValue(), date);
        return poList.stream()
                .map(TodoConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Todo> findOverdueTodos(UserId userId, LocalDateTime now) {
        List<TodoPO> poList = mapper.selectOverdueTodos(userId.getValue(), now);
        return poList.stream()
                .map(TodoConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Todo> findNeedRemind(LocalDateTime startTime, LocalDateTime endTime) {
        List<TodoPO> poList = mapper.selectNeedRemind(startTime, endTime);
        return poList.stream()
                .map(TodoConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean delete(TodoId id) {
        return mapper.deleteById(id.getValue()) > 0;
    }
    
    @Override
    public int countCompletedByUserId(UserId userId) {
        return mapper.countCompletedByUserId(userId.getValue());
    }
    
    @Override
    public int countHighPriorityCompleted(UserId userId) {
        return mapper.countHighPriorityCompleted(userId.getValue());
    }
    
    @Override
    public int countConsecutiveDays(UserId userId) {
        return mapper.countConsecutiveDays(userId.getValue());
    }
}
