package com.crazydream.infrastructure.persistence.converter;

import com.crazydream.domain.todo.model.aggregate.Todo;
import com.crazydream.domain.todo.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.infrastructure.persistence.po.TodoPO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Todo领域模型与持久化对象转换器
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public class TodoConverter {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * PO -> Domain Entity
     */
    public static Todo toDomain(TodoPO po) {
        if (po == null) {
            return null;
        }
        
        // 解析JSON字段
        List<String> tags = parseJsonArray(po.getTags());
        
        return Todo.rebuild(
            TodoId.of(po.getId()),
            UserId.of(po.getUserId()),
            po.getTitle(),
            po.getDescription(),
            TodoPriority.fromCode(po.getPriority()),
            TodoStatus.fromCode(po.getStatus()),
            po.getDueDate(),
            po.getRemindTime(),
            po.getRemindSent() != null && po.getRemindSent() == 1,
            po.getRelatedGoalId() != null ? GoalId.of(po.getRelatedGoalId()) : null,
            po.getCompletedTime(),
            po.getSortOrder() != null ? po.getSortOrder() : 0,
            tags,
            po.getEstimatedMinutes(),
            po.getActualMinutes(),
            po.getCreateTime(),
            po.getUpdateTime()
        );
    }
    
    /**
     * Domain Entity -> PO
     */
    public static TodoPO toPO(Todo todo) {
        if (todo == null) {
            return null;
        }
        
        TodoPO po = new TodoPO();
        if (todo.getId() != null) {
            po.setId(todo.getId().getValue());
        }
        po.setUserId(todo.getUserId().getValue());
        po.setTitle(todo.getTitle());
        po.setDescription(todo.getDescription());
        po.setPriority(todo.getPriority().getCode());
        po.setStatus(todo.getStatus().getCode());
        po.setDueDate(todo.getDueDate());
        po.setRemindTime(todo.getRemindTime());
        po.setRemindSent(todo.isRemindSent() ? 1 : 0);
        po.setRelatedGoalId(todo.getRelatedGoalId() != null ? todo.getRelatedGoalId().getValue() : null);
        po.setCompletedTime(todo.getCompletedTime());
        po.setSortOrder(todo.getSortOrder());
        po.setTags(toJsonArray(todo.getTags()));
        po.setEstimatedMinutes(todo.getEstimatedMinutes());
        po.setActualMinutes(todo.getActualMinutes());
        po.setCreateTime(todo.getCreateTime());
        po.setUpdateTime(todo.getUpdateTime());
        
        return po;
    }
    
    /**
     * 解析JSON数组
     */
    private static List<String> parseJsonArray(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * 转换为JSON数组
     */
    private static String toJsonArray(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            return null;
        }
    }
}
