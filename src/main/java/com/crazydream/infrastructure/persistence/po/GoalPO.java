package com.crazydream.infrastructure.persistence.po;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 目标持久化对象（PO）
 * 仅用于 MyBatis 数据库映射，不包含业务逻辑
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
@Data
public class GoalPO {
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
