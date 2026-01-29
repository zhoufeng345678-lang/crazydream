package com.crazydream.application.achievement.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AchievementDTO {
    private Long id;
    private Long userId;
    private String type;
    private String name;            // 成就名称
    private String typeName;       
    private String description;
    private String iconUrl;         // 图标URL/emoji
    private String category;        // 分类
    private String tier;            // 等级
    private Integer progress;       // 当前进度
    private Integer target;         // 目标进度
    private Boolean unlocked;
    private LocalDateTime unlockedTime;
    private LocalDateTime createTime;
}
