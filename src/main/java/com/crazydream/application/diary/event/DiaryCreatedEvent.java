package com.crazydream.application.diary.event;

import lombok.Getter;

/**
 * 日记创建事件
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Getter
public class DiaryCreatedEvent {
    private final Long diaryId;
    private final Long userId;
    
    public DiaryCreatedEvent(Long diaryId, Long userId) {
        this.diaryId = diaryId;
        this.userId = userId;
    }
}
