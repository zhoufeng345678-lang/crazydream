package com.crazydream.application.goal.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 创建目标命令
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
@Data
public class CreateGoalCommand {
    private Long userId;
    private String title;
    private String description;
    private Long categoryId;
    private String priority;
    private LocalDateTime deadline;
}
