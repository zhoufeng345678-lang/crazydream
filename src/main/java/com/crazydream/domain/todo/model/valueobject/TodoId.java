package com.crazydream.domain.todo.model.valueobject;

import java.util.Objects;

/**
 * 待办ID值对象
 * 封装待办事项的唯一标识符
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public class TodoId {
    private final Long value;
    
    private TodoId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("待办ID不能为空或非正数");
        }
        this.value = value;
    }
    
    public static TodoId of(Long value) {
        return new TodoId(value);
    }
    
    public Long getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoId todoId = (TodoId) o;
        return Objects.equals(value, todoId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "TodoId{" + value + '}';
    }
}
