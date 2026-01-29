package com.crazydream.application.diary.listener;

import com.crazydream.application.achievement.service.AchievementApplicationService;
import com.crazydream.application.diary.event.DiaryCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 日记成就监听器
 * 监听日记相关事件,触发成就检查
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Component
public class DiaryAchievementListener {
    
    private static final Logger logger = LoggerFactory.getLogger(DiaryAchievementListener.class);
    
    @Autowired
    private AchievementApplicationService achievementService;
    
    /**
     * 监听日记创建事件
     */
    @EventListener
    @Async
    public void handleDiaryCreated(DiaryCreatedEvent event) {
        try {
            logger.info("监听到日记创建事件: diaryId={}, userId={}", event.getDiaryId(), event.getUserId());
            
            // 触发成就检查
            achievementService.checkAndUnlock(event.getUserId());
            
            logger.info("日记创建后成就检查完成: userId={}", event.getUserId());
        } catch (Exception e) {
            logger.error("处理日记创建事件失败: diaryId={}, userId={}", 
                event.getDiaryId(), event.getUserId(), e);
        }
    }
}
