package com.crazydream.interfaces.reminder;

import com.crazydream.application.reminder.dto.*;
import com.crazydream.application.reminder.service.ReminderApplicationService;
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
@RequestMapping("/api/v2/reminders")
public class ReminderController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReminderController.class);
    
    @Autowired
    private ReminderApplicationService reminderApplicationService;
    
    @Value("${security.test.default-user-id:1}")
    private Long defaultUserId;
    
    @PostMapping
    public ResponseResult<ReminderDTO> createReminder(@RequestBody CreateReminderCommand command) {
        try {
            Long userId = getCurrentUserId();
            ReminderDTO dto = reminderApplicationService.createReminder(command, userId);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(400, e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseResult<List<ReminderDTO>> getUserReminders() {
        try {
            Long userId = getCurrentUserId();
            List<ReminderDTO> dtos = reminderApplicationService.getUserReminders(userId);
            return ResponseResult.success(dtos);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    @GetMapping("/unread")
    public ResponseResult<List<ReminderDTO>> getUnreadReminders() {
        try {
            Long userId = getCurrentUserId();
            List<ReminderDTO> dtos = reminderApplicationService.getUnreadReminders(userId);
            return ResponseResult.success(dtos);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/read")
    public ResponseResult<ReminderDTO> markAsRead(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            ReminderDTO dto = reminderApplicationService.markAsRead(id, userId);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(400, e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseResult<Boolean> deleteReminder(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            boolean success = reminderApplicationService.deleteReminder(id, userId);
            return ResponseResult.success(success);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            logger.debug("未找到认证信息，使用默认用户ID: {}", defaultUserId);
            return defaultUserId;
        }
        
        Object principal = authentication.getPrincipal();
        
        if ("anonymousUser".equals(principal)) {
            logger.debug("匿名用户访问，使用默认用户ID: {}", defaultUserId);
            return defaultUserId;
        }
        
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            return Long.parseLong(((org.springframework.security.core.userdetails.User) principal).getUsername());
        } else if (principal instanceof Long) {
            return (Long) principal;
        } else if (principal instanceof String) {
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                logger.warn("无法解析用户ID: {}, 使用默认用户ID: {}", principal, defaultUserId);
                return defaultUserId;
            }
        }
        
        logger.warn("不支持的认证信息格式: {}, 使用默认用户ID: {}", principal.getClass(), defaultUserId);
        return defaultUserId;
    }
}
