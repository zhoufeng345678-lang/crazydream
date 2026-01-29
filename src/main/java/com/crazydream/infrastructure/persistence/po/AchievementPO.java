package com.crazydream.infrastructure.persistence.po;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AchievementPO {
    private Long id;
    private Long userId;
    private String type;
    private String title;
    private String description;
    private String badgeUrl;
    private String unlockCondition;
    private Boolean unlocked;
    private Boolean isUnlocked;
    private java.time.LocalDateTime unlockedTime;
    private java.time.LocalDateTime unlockedAt;
    private Integer progress;            // 当前进度值
    private Integer target;              // 目标进度值
    private String category;             // 成就分类
    private String tier;                 // 成就等级
    private String icon;                 // 成就图标emoji
    private Integer sortOrder;           // 排序权重
    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;
}
