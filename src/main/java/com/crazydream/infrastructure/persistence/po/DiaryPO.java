package com.crazydream.infrastructure.persistence.po;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 日记持久化对象(PO)
 * 仅用于MyBatis数据库映射,不包含业务逻辑
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Data
public class DiaryPO {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String category;
    private String tags;  // JSON字符串
    private String mood;
    private String weather;
    private Long relatedGoalId;
    private String imageUrls;  // JSON字符串
    private Integer isPublic;
    private Integer viewCount;
    private LocalDate diaryDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
