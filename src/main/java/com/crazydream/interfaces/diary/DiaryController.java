package com.crazydream.interfaces.diary;

import com.crazydream.application.diary.command.CreateDiaryCommand;
import com.crazydream.application.diary.command.UpdateDiaryCommand;
import com.crazydream.application.diary.dto.DiaryDTO;
import com.crazydream.application.diary.service.DiaryApplicationService;
import com.crazydream.utils.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 日记控制器
 * Interface层,负责HTTP请求处理
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@RestController
@RequestMapping("/api/v2/diaries")
public class DiaryController {
    
    private static final Logger logger = LoggerFactory.getLogger(DiaryController.class);
    
    @Autowired
    private DiaryApplicationService diaryApplicationService;
    
    @Value("${security.test.default-user-id:1}")
    private Long defaultUserId;
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || "anonymousUser".equals(authentication.getPrincipal())) {
            return defaultUserId;
        }
        try {
            return Long.parseLong(authentication.getName());
        } catch (Exception e) {
            return defaultUserId;
        }
    }
    
    /**
     * 创建日记
     */
    @PostMapping
    public ResponseResult<DiaryDTO> createDiary(@RequestBody CreateDiaryCommand command) {
        try {
            Long userId = getCurrentUserId();
            command.setUserId(userId);
            
            logger.info("创建日记请求: userId={}, title={}", userId, command.getTitle());
            DiaryDTO diary = diaryApplicationService.createDiary(command);
            
            return ResponseResult.success(diary);
        } catch (Exception e) {
            logger.error("创建日记失败", e);
            return ResponseResult.error("创建日记失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新日记
     */
    @PutMapping("/{id}")
    public ResponseResult<DiaryDTO> updateDiary(@PathVariable Long id, 
                                                  @RequestBody UpdateDiaryCommand command) {
        try {
            Long userId = getCurrentUserId();
            command.setId(id);
            command.setUserId(userId);
            
            DiaryDTO diary = diaryApplicationService.updateDiary(command);
            return ResponseResult.success(diary);
        } catch (Exception e) {
            logger.error("更新日记失败: id={}", id, e);
            return ResponseResult.error("更新日记失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除日记
     */
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteDiary(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            diaryApplicationService.deleteDiary(id, userId);
            return ResponseResult.success(null);
        } catch (Exception e) {
            logger.error("删除日记失败: id={}", id, e);
            return ResponseResult.error("删除日记失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取日记详情
     */
    @GetMapping("/{id}")
    public ResponseResult<DiaryDTO> getDiary(@PathVariable Long id) {
        try {
            DiaryDTO diary = diaryApplicationService.getDiary(id);
            return ResponseResult.success(diary);
        } catch (Exception e) {
            logger.error("获取日记详情失败: id={}", id, e);
            return ResponseResult.error("获取日记详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取当前用户所有日记
     */
    @GetMapping
    public ResponseResult<List<DiaryDTO>> getUserDiaries() {
        try {
            Long userId = getCurrentUserId();
            List<DiaryDTO> diaries = diaryApplicationService.getUserDiaries(userId);
            return ResponseResult.success(diaries);
        } catch (Exception e) {
            logger.error("获取用户日记列表失败", e);
            return ResponseResult.error("获取日记列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 按日期范围查询日记
     */
    @GetMapping("/range")
    public ResponseResult<List<DiaryDTO>> getDiariesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Long userId = getCurrentUserId();
            List<DiaryDTO> diaries = diaryApplicationService.getDiariesByDateRange(userId, startDate, endDate);
            return ResponseResult.success(diaries);
        } catch (Exception e) {
            logger.error("按日期范围查询日记失败", e);
            return ResponseResult.error("查询日记失败: " + e.getMessage());
        }
    }
    
    /**
     * 按分类查询日记
     */
    @GetMapping("/category/{category}")
    public ResponseResult<List<DiaryDTO>> getDiariesByCategory(@PathVariable String category) {
        try {
            Long userId = getCurrentUserId();
            List<DiaryDTO> diaries = diaryApplicationService.getDiariesByCategory(userId, category);
            return ResponseResult.success(diaries);
        } catch (Exception e) {
            logger.error("按分类查询日记失败: category={}", category, e);
            return ResponseResult.error("查询日记失败: " + e.getMessage());
        }
    }
}
