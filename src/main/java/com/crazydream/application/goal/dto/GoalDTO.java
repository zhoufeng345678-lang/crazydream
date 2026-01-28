package com.crazydream.application.goal.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 目标数据传输对象
 * 用于 Application 层和 Interface 层之间的数据传输
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
@Data
public class GoalDTO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Long categoryId;
    private String priority;
    private LocalDateTime deadline;
    private Integer progress;
    private String status;
    private String imageUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
