package com.crazydream.application.diary.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 日记数据传输对象
 * 用于Application层和Interface层之间的数据传输
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Data
public class DiaryDTO {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String category;
    private List<String> tags;
    private String mood;
    private String weather;
    private Long relatedGoalId;
    private List<String> imageUrls;
    private Boolean isPublic;
    private Integer viewCount;
    private LocalDate diaryDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
