package com.crazydream.interfaces.statistics;

import com.crazydream.application.statistics.dto.CategoryStatisticsDTO;
import com.crazydream.application.statistics.dto.DashboardStatisticsDTO;
import com.crazydream.application.statistics.dto.TrendsStatisticsDTO;
import com.crazydream.application.statistics.service.StatisticsApplicationService;
import com.crazydream.utils.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 统计控制器 - v2版本
 * 提供目标统计、仪表盘数据、趋势分析等功能
 */
@RestController
@RequestMapping("/api/v2/statistics")
public class StatisticsController {
    
    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);
    
    @Autowired
    private StatisticsApplicationService statisticsApplicationService;
    
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
    
    /**
     * 获取目标统计
     */
    @GetMapping("/goals")
    public ResponseResult<Map<String, Integer>> getGoalStatistics() {
        try {
            Long userId = getCurrentUserId();
            logger.info("获取用户{}的目标统计", userId);
            Map<String, Integer> statistics = statisticsApplicationService.getGoalStatistics(userId);
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            logger.error("获取目标统计失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 获取仪表盘统计（概览）
     */
    @GetMapping({"/dashboard", "/overview"})
    public ResponseResult<DashboardStatisticsDTO> getDashboardStatistics() {
        try {
            Long userId = getCurrentUserId();
            logger.info("获取用户{}的仪表盘统计", userId);
            DashboardStatisticsDTO statistics = statisticsApplicationService.getDashboardStatistics(userId);
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            logger.error("获取仪表盘统计失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 获取趋势统计
     */
    @GetMapping({"/trends", "/trend"})
    public ResponseResult<TrendsStatisticsDTO> getTrendsStatistics() {
        try {
            Long userId = getCurrentUserId();
            logger.info("获取用户{}的趋势统计", userId);
            TrendsStatisticsDTO statistics = statisticsApplicationService.getTrendsStatistics(userId);
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            logger.error("获取趋势统计失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
    
    /**
     * 获取分类统计
     */
    @GetMapping("/categories")
    public ResponseResult<List<CategoryStatisticsDTO>> getCategoryStatistics() {
        try {
            Long userId = getCurrentUserId();
            logger.info("获取用户{}的分类统计", userId);
            List<CategoryStatisticsDTO> statistics = statisticsApplicationService.getCategoryStatistics(userId);
            return ResponseResult.success(statistics);
        } catch (Exception e) {
            logger.error("获取分类统计失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "系统内部错误");
        }
    }
}
