package com.crazydream.application.diary.service;

import com.crazydream.application.diary.assembler.DiaryAssembler;
import com.crazydream.application.diary.command.CreateDiaryCommand;
import com.crazydream.application.diary.command.UpdateDiaryCommand;
import com.crazydream.application.diary.dto.DiaryDTO;
import com.crazydream.application.diary.event.DiaryCreatedEvent;
import com.crazydream.domain.diary.model.aggregate.Diary;
import com.crazydream.domain.diary.model.valueobject.DiaryId;
import com.crazydream.domain.diary.model.valueobject.DiaryCategory;
import com.crazydream.domain.diary.repository.DiaryRepository;
import com.crazydream.domain.shared.model.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 日记应用服务
 * 负责业务流程编排,协调领域对象完成业务用例
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Service
public class DiaryApplicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(DiaryApplicationService.class);
    
    @Autowired
    private DiaryRepository diaryRepository;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    /**
     * 创建日记
     */
    @Transactional
    public DiaryDTO createDiary(CreateDiaryCommand command) {
        logger.info("创建日记: userId={}, title={}", command.getUserId(), command.getTitle());
        
        // 1. 组装领域对象
        Diary diary = DiaryAssembler.toDomain(command);
        
        // 2. 持久化
        diary = diaryRepository.save(diary);
        
        // 3. 发布事件,触发成就检查
        eventPublisher.publishEvent(new DiaryCreatedEvent(diary.getId().getValue(), command.getUserId()));
        
        // 4. 转换为DTO返回
        return DiaryAssembler.toDTO(diary);
    }
    
    /**
     * 更新日记
     */
    @Transactional
    public DiaryDTO updateDiary(UpdateDiaryCommand command) {
        logger.info("更新日记: diaryId={}", command.getId());
        
        // 1. 加载领域对象
        Diary diary = diaryRepository.findById(DiaryId.of(command.getId()))
                .orElseThrow(() -> new IllegalArgumentException("日记不存在: " + command.getId()));
        
        // 2. 验证权限
        if (!diary.getUserId().getValue().equals(command.getUserId())) {
            throw new IllegalArgumentException("无权限操作此日记");
        }
        
        // 3. 应用更新
        DiaryAssembler.applyUpdateCommand(diary, command);
        
        // 4. 持久化
        diary = diaryRepository.save(diary);
        
        // 5. 返回DTO
        return DiaryAssembler.toDTO(diary);
    }
    
    /**
     * 删除日记
     */
    @Transactional
    public void deleteDiary(Long diaryId, Long userId) {
        logger.info("删除日记: diaryId={}, userId={}", diaryId, userId);
        
        // 1. 加载并验证权限
        Diary diary = diaryRepository.findById(DiaryId.of(diaryId))
                .orElseThrow(() -> new IllegalArgumentException("日记不存在: " + diaryId));
        
        if (!diary.getUserId().getValue().equals(userId)) {
            throw new IllegalArgumentException("无权限删除此日记");
        }
        
        // 2. 删除
        diaryRepository.delete(DiaryId.of(diaryId));
    }
    
    /**
     * 获取日记详情
     */
    public DiaryDTO getDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(DiaryId.of(diaryId))
                .orElseThrow(() -> new IllegalArgumentException("日记不存在: " + diaryId));
        
        // 增加查看次数
        diary.incrementViewCount();
        diaryRepository.save(diary);
        
        return DiaryAssembler.toDTO(diary);
    }
    
    /**
     * 获取用户所有日记
     */
    public List<DiaryDTO> getUserDiaries(Long userId) {
        List<Diary> diaries = diaryRepository.findByUserId(UserId.of(userId));
        return DiaryAssembler.toDTOList(diaries);
    }
    
    /**
     * 按日期范围查询日记
     */
    public List<DiaryDTO> getDiariesByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Diary> diaries = diaryRepository.findByUserIdAndDateRange(
            UserId.of(userId), 
            startDate, 
            endDate
        );
        return DiaryAssembler.toDTOList(diaries);
    }
    
    /**
     * 按分类查询日记
     */
    public List<DiaryDTO> getDiariesByCategory(Long userId, String category) {
        List<Diary> diaries = diaryRepository.findByUserIdAndCategory(
            UserId.of(userId), 
            DiaryCategory.fromCode(category)
        );
        return DiaryAssembler.toDTOList(diaries);
    }
}
