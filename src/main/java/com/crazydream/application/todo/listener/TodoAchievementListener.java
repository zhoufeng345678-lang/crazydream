package com.crazydream.application.todo.listener;

import com.crazydream.application.achievement.service.AchievementApplicationService;
import com.crazydream.application.todo.event.TodoCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 待办成就监听器
 * 监听待办相关事件,触发成就检查
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Component
public class TodoAchievementListener {
    
    private static final Logger logger = LoggerFactory.getLogger(TodoAchievementListener.class);
    
    @Autowired
    private AchievementApplicationService achievementService;
    
    /**
     * 监听待办完成事件
     */
    @EventListener
    @Async
    public void handleTodoCompleted(TodoCompletedEvent event) {
        try {
            logger.info("监听到待办完成事件: todoId={}, userId={}", event.getTodoId(), event.getUserId());
            
            // 触发成就检查
            achievementService.checkAndUnlock(event.getUserId());
            
            logger.info("待办完成后成就检查完成: userId={}", event.getUserId());
        } catch (Exception e) {
            logger.error("处理待办完成事件失败: todoId={}, userId={}", 
                event.getTodoId(), event.getUserId(), e);
        }
    }
}
