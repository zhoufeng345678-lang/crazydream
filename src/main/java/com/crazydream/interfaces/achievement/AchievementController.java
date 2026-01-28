package com.crazydream.interfaces.achievement;

import com.crazydream.application.achievement.dto.AchievementDTO;
import com.crazydream.application.achievement.service.AchievementApplicationService;
import com.crazydream.utils.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/achievements")
public class AchievementController {
    
    private static final Logger logger = LoggerFactory.getLogger(AchievementController.class);
    
    @Autowired
    private AchievementApplicationService achievementApplicationService;
    
    @Value("${security.test.default-user-id:1}")
    private Long defaultUserId;
    
    @GetMapping
    public ResponseResult<List<AchievementDTO>> getUserAchievements() {
        try {
            Long userId = getCurrentUserId();
            List<AchievementDTO> dtos = achievementApplicationService.getUserAchievements(userId);
            return ResponseResult.success(dtos);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    @GetMapping("/unlocked")
    public ResponseResult<List<AchievementDTO>> getUnlockedAchievements() {
        try {
            Long userId = getCurrentUserId();
            List<AchievementDTO> dtos = achievementApplicationService.getUnlockedAchievements(userId);
            return ResponseResult.success(dtos);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    @PostMapping("/unlock")
    public ResponseResult<AchievementDTO> unlockAchievement(@RequestBody UnlockAchievementRequest request) {
        try {
            Long userId = request.getUserId() != null ? request.getUserId() : getCurrentUserId();
            AchievementDTO dto = achievementApplicationService.unlockAchievement(userId, request.getAchievementId());
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    // 内部类：解锁请求
    public static class UnlockAchievementRequest {
        private Long userId;
        private Long achievementId;
        
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getAchievementId() { return achievementId; }
        public void setAchievementId(Long achievementId) { this.achievementId = achievementId; }
    }
    
    private Long getCurrentUserId() {
        logger.info("[DEBUG] getCurrentUserId被调用，defaultUserId配置值: {}", defaultUserId);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("[DEBUG] Authentication对象: {}", authentication);
        
        if (authentication == null || authentication.getPrincipal() == null) {
            logger.info("[DEBUG] 未找到认证信息，返回defaultUserId: {}", defaultUserId);
            return defaultUserId;
        }
        
        Object principal = authentication.getPrincipal();
        logger.info("[DEBUG] Principal对象: {}, 类型: {}", principal, principal.getClass().getName());
        
        if ("anonymousUser".equals(principal)) {
            logger.info("[DEBUG] 匿名用户访问，返回defaultUserId: {}", defaultUserId);
            return defaultUserId;
        }
        
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            Long userId = Long.parseLong(((org.springframework.security.core.userdetails.User) principal).getUsername());
            logger.info("[DEBUG] 从UserDetails解析userId: {}", userId);
            return userId;
        } else if (principal instanceof Long) {
            logger.info("[DEBUG] Principal是Long类型: {}", principal);
            return (Long) principal;
        } else if (principal instanceof String) {
            try {
                Long userId = Long.parseLong((String) principal);
                logger.info("[DEBUG] 从String解析userId: {}", userId);
                return userId;
            } catch (NumberFormatException e) {
                logger.warn("[DEBUG] 无法解析用户ID: {}, 返回defaultUserId: {}", principal, defaultUserId);
                return defaultUserId;
            }
        }
        
        logger.warn("[DEBUG] 不支持的认证信息格式: {}, 返回defaultUserId: {}", principal.getClass(), defaultUserId);
        return defaultUserId;
    }
}
