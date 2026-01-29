package com.crazydream.application.todo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 待办数据传输对象
 * 用于Application层和Interface层之间的数据传输
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Data
public class TodoDTO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String priority;
    private String status;
    private LocalDateTime dueDate;
    private LocalDateTime remindTime;
    private Boolean remindSent;
    private Long relatedGoalId;
    private LocalDateTime completedTime;
    private Integer sortOrder;
    private List<String> tags;
    private Integer estimatedMinutes;
    private Integer actualMinutes;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
