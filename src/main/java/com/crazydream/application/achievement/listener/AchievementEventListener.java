package com.crazydream.application.achievement.listener;

import com.crazydream.application.achievement.service.AchievementApplicationService;
import com.crazydream.domain.goal.event.GoalCompletedEvent;
import com.crazydream.domain.goal.event.GoalProgressUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 成就事件监听器
 * 监听目标相关的领域事件,触发成就检查和解锁
 * 
 * @author CrazyDream Team
 * @since 2026-01-29
 */
@Component
public class AchievementEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(AchievementEventListener.class);
    
    @Autowired
    private AchievementApplicationService achievementService;
    
    /**
     * 监听目标完成事件
     * 当用户完成目标时,自动触发成就检查
     */
    @EventListener
    @Async
    public void handleGoalCompleted(GoalCompletedEvent event) {
        logger.info("收到GoalCompletedEvent: {}", event);
        
        try {
            // 触发成就检查
            achievementService.checkAndUnlock(event.getUserId());
            logger.info("目标完成事件处理成功,已触发成就检查,用户ID: {}", event.getUserId());
        } catch (Exception e) {
            // 成就检查失败不影响目标完成的主流程
            logger.error("处理目标完成事件失败,用户ID: {}", event.getUserId(), e);
        }
    }
    
    /**
     * 监听目标进度更新事件
     * 当进度达到100%时,也会触发成就检查(双保险机制)
     */
    @EventListener
    @Async
    public void handleGoalProgressUpdated(GoalProgressUpdatedEvent event) {
        logger.debug("收到GoalProgressUpdatedEvent: {}", event);
        
        // 只有达到100%时才触发成就检查
        if (!event.isCompleted()) {
            return;
        }
        
        try {
            // 触发成就检查
            achievementService.checkAndUnlock(event.getUserId());
            logger.info("进度更新事件处理成功(进度达到100%),已触发成就检查,用户ID: {}", event.getUserId());
        } catch (Exception e) {
            logger.error("处理进度更新事件失败,用户ID: {}", event.getUserId(), e);
        }
    }
}
