package com.crazydream.application.todo.command;

import lombok.Data;

@Data
public class CompleteTodoCommand {
    private Long id;
    private Long userId;
    private Integer actualMinutes;
}
