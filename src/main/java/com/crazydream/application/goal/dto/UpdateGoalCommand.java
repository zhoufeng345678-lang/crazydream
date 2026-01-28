package com.crazydream.application.goal.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 更新目标命令
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
@Data
public class UpdateGoalCommand {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Long categoryId;
    private String priority;
    private LocalDateTime deadline;
    private String imageUrl;
}
