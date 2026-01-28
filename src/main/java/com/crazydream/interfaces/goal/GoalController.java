package com.crazydream.interfaces.goal;

import com.crazydream.application.goal.dto.CreateGoalCommand;
import com.crazydream.application.goal.dto.GoalDTO;
import com.crazydream.application.goal.dto.UpdateGoalCommand;
import com.crazydream.application.goal.service.GoalApplicationService;
import com.crazydream.utils.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 目标控制器（新架构）
 * Interface 层，负责 HTTP 请求处理
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
@RestController
@RequestMapping("/api/v2/goals")
public class GoalController {
    
    private static final Logger logger = LoggerFactory.getLogger(GoalController.class);
    
    @Autowired
    private GoalApplicationService goalApplicationService;
    
    @Value("${security.test.default-user-id:1}")
    private Long defaultUserId;
    
    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            logger.debug("未找到认证信息，使用默认用户ID: {}", defaultUserId);
            return defaultUserId;
        }
        
        Object principal = authentication.getPrincipal();
        
        // 处理anonymousUser（匹名用户）
        if ("anonymousUser".equals(principal)) {
            logger.debug("匹名用户访问，使用默认用户ID: {}", defaultUserId);
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
    
    /**
     * 创建目标
     */
    @PostMapping
    public ResponseResult<GoalDTO> createGoal(@RequestBody CreateGoalCommand command) {
        try {
            Long userId = getCurrentUserId();
            command.setUserId(userId);
            
            GoalDTO goal = goalApplicationService.createGoal(command);
            return ResponseResult.success(goal);
        } catch (IllegalArgumentException e) {
            logger.error("创建目标失败: {}", e.getMessage());
            return ResponseResult.error(400, e.getMessage());
        } catch (Exception e) {
            logger.error("创建目标失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 更新目标
     */
    @PutMapping("/{id}")
    public ResponseResult<GoalDTO> updateGoal(@PathVariable Long id, 
                                               @RequestBody UpdateGoalCommand command) {
        try {
            Long userId = getCurrentUserId();
            command.setId(id);
            command.setUserId(userId);
            
            GoalDTO goal = goalApplicationService.updateGoal(command);
            return ResponseResult.success(goal);
        } catch (IllegalArgumentException e) {
            logger.error("更新目标失败: {}", e.getMessage());
            return ResponseResult.error(400, e.getMessage());
        } catch (Exception e) {
            logger.error("更新目标失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 获取用户所有目标
     */
    @GetMapping
    public ResponseResult<List<GoalDTO>> getUserGoals() {
        try {
            Long userId = getCurrentUserId();
            List<GoalDTO> goals = goalApplicationService.getUserGoals(userId);
            return ResponseResult.success(goals);
        } catch (IllegalArgumentException e) {
            logger.error("获取目标列表参数错误: {}", e.getMessage());
            return ResponseResult.error(400, e.getMessage());
        } catch (Exception e) {
            logger.error("获取目标列表失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 根据ID获取目标
     */
    @GetMapping("/{id}")
    public ResponseResult<GoalDTO> getGoalById(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            GoalDTO goal = goalApplicationService.getGoalById(id, userId);
            return ResponseResult.success(goal);
        } catch (IllegalArgumentException e) {
            logger.error("获取目标失败: {}", e.getMessage());
            return ResponseResult.error(404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取目标失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 根据分类获取目标
     */
    @GetMapping("/category/{categoryId}")
    public ResponseResult<List<GoalDTO>> getGoalsByCategory(@PathVariable Long categoryId) {
        try {
            Long userId = getCurrentUserId();
            List<GoalDTO> goals = goalApplicationService.getGoalsByCategory(categoryId, userId);
            return ResponseResult.success(goals);
        } catch (IllegalArgumentException e) {
            logger.error("获取分类目标参数错误: {}", e.getMessage());
            return ResponseResult.error(400, e.getMessage());
        } catch (Exception e) {
            logger.error("获取分类目标失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 删除目标
     */
    @DeleteMapping("/{id}")
    public ResponseResult<Boolean> deleteGoal(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            boolean success = goalApplicationService.deleteGoal(id, userId);
            return ResponseResult.success(success);
        } catch (IllegalArgumentException e) {
            logger.error("删除目标失败: {}", e.getMessage());
            return ResponseResult.error(404, e.getMessage());
        } catch (Exception e) {
            logger.error("删除目标失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 批量删除目标
     */
    @DeleteMapping("/batch")
    public ResponseResult<Integer> batchDeleteGoals(@RequestBody List<Long> ids) {
        try {
            Long userId = getCurrentUserId();
            int count = goalApplicationService.batchDeleteGoals(ids, userId);
            return ResponseResult.success(count);
        } catch (Exception e) {
            logger.error("批量删除目标失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 更新目标进度
     */
    @PatchMapping("/{id}/progress")
    public ResponseResult<GoalDTO> updateProgress(@PathVariable Long id, 
                                                   @RequestParam int progress) {
        try {
            Long userId = getCurrentUserId();
            GoalDTO goal = goalApplicationService.updateProgress(id, userId, progress);
            return ResponseResult.success(goal);
        } catch (IllegalArgumentException e) {
            logger.error("更新进度失败: {}", e.getMessage());
            return ResponseResult.error(400, e.getMessage());
        } catch (Exception e) {
            logger.error("更新进度失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 完成目标
     */
    @PatchMapping("/{id}/complete")
    public ResponseResult<GoalDTO> completeGoal(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            GoalDTO goal = goalApplicationService.completeGoal(id, userId);
            return ResponseResult.success(goal);
        } catch (IllegalArgumentException e) {
            logger.error("完成目标失败: {}", e.getMessage());
            return ResponseResult.error(400, e.getMessage());
        } catch (Exception e) {
            logger.error("完成目标失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 获取最近更新的目标
     */
    @GetMapping("/recent")
    public ResponseResult<List<GoalDTO>> getRecentGoals(@RequestParam(defaultValue = "10") int limit) {
        try {
            Long userId = getCurrentUserId();
            List<GoalDTO> goals = goalApplicationService.getRecentGoals(userId, limit);
            return ResponseResult.success(goals);
        } catch (Exception e) {
            logger.error("获取最近目标失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 获取今日提醒的目标
     */
    @GetMapping("/today-reminders")
    public ResponseResult<List<GoalDTO>> getTodayReminderGoals(@RequestParam(required = false) String date) {
        try {
            Long userId = getCurrentUserId();
            // 如果没有传date参数，使用今天的日期
            if (date == null || date.isEmpty()) {
                date = java.time.LocalDate.now().toString();
            }
            List<GoalDTO> goals = goalApplicationService.getTodayReminderGoals(userId, date);
            return ResponseResult.success(goals);
        } catch (Exception e) {
            logger.error("获取今日提醒失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 获取目标统计
     */
    @GetMapping("/statistics")
    public ResponseResult<Map<String, Integer>> getStatistics() {
        try {
            Long userId = getCurrentUserId();
            Map<String, Integer> statistics = goalApplicationService.getGoalStatistics(userId);
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            logger.error("获取统计失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
}
