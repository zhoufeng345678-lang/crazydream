package com.crazydream.application.todo.assembler;

import com.crazydream.application.todo.command.CreateTodoCommand;
import com.crazydream.application.todo.dto.TodoDTO;
import com.crazydream.domain.todo.model.aggregate.Todo;
import com.crazydream.domain.todo.model.valueobject.TodoPriority;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.goal.model.valueobject.GoalId;

import java.util.List;
import java.util.stream.Collectors;

public class TodoAssembler {
    
    public static Todo toDomain(CreateTodoCommand command) {
        Todo todo = Todo.create(
            UserId.of(command.getUserId()),
            command.getTitle(),
            TodoPriority.fromCode(command.getPriority())
        );
        
        if (command.getDescription() != null) {
            todo.setDescription(command.getDescription());
        }
        
        if (command.getDueDate() != null) {
            todo.setDueDate(command.getDueDate());
        }
        
        if (command.getRemindTime() != null) {
            todo.setReminder(command.getRemindTime());
        }
        
        if (command.getRelatedGoalId() != null) {
            todo.linkToGoal(GoalId.of(command.getRelatedGoalId()));
        }
        
        if (command.getEstimatedMinutes() != null) {
            todo.setEstimatedMinutes(command.getEstimatedMinutes());
        }
        
        return todo;
    }
    
    public static TodoDTO toDTO(Todo todo) {
        if (todo == null) {
            return null;
        }
        
        TodoDTO dto = new TodoDTO();
        dto.setId(todo.getId() != null ? todo.getId().getValue() : null);
        dto.setUserId(todo.getUserId().getValue());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setPriority(todo.getPriority().getCode());
        dto.setStatus(todo.getStatus().getCode());
        dto.setDueDate(todo.getDueDate());
        dto.setRemindTime(todo.getRemindTime());
        dto.setRemindSent(todo.isRemindSent());
        dto.setRelatedGoalId(todo.getRelatedGoalId() != null ? todo.getRelatedGoalId().getValue() : null);
        dto.setCompletedTime(todo.getCompletedTime());
        dto.setSortOrder(todo.getSortOrder());
        dto.setTags(todo.getTags());
        dto.setEstimatedMinutes(todo.getEstimatedMinutes());
        dto.setActualMinutes(todo.getActualMinutes());
        dto.setCreateTime(todo.getCreateTime());
        dto.setUpdateTime(todo.getUpdateTime());
        
        return dto;
    }
    
    public static List<TodoDTO> toDTOList(List<Todo> todos) {
        return todos.stream()
                .map(TodoAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
