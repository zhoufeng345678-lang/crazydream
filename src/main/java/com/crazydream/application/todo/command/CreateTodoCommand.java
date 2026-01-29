package com.crazydream.application.todo.command;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateTodoCommand {
    private Long userId;
    private String title;
    private String description;
    private String priority;
    private LocalDateTime dueDate;
    private LocalDateTime remindTime;
    private Long relatedGoalId;
    private Integer estimatedMinutes;
}
