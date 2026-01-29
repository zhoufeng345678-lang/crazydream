package com.crazydream.application.todo.event;

import lombok.Getter;

@Getter
public class TodoCompletedEvent {
    private final Long todoId;
    private final Long userId;
    
    public TodoCompletedEvent(Long todoId, Long userId) {
        this.todoId = todoId;
        this.userId = userId;
    }
}
