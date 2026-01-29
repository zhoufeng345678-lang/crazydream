package com.crazydream.domain.diary.repository;

import com.crazydream.domain.diary.model.aggregate.Diary;
import com.crazydream.domain.diary.model.valueobject.DiaryId;
import com.crazydream.domain.diary.model.valueobject.DiaryCategory;
import com.crazydream.domain.shared.model.UserId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 日记仓储接口(防腐层)
 * 定义在Domain层,实现在Infrastructure层
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public interface DiaryRepository {
    
    /**
     * 保存日记(新增或更新)
     */
    Diary save(Diary diary);
    
    /**
     * 根据ID查找日记
     */
    Optional<Diary> findById(DiaryId id);
    
    /**
     * 根据用户ID查找所有日记
     */
    List<Diary> findByUserId(UserId userId);
    
    /**
     * 根据用户ID和日期范围查找日记
     */
    List<Diary> findByUserIdAndDateRange(UserId userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据用户ID和分类查找日记
     */
    List<Diary> findByUserIdAndCategory(UserId userId, DiaryCategory category);
    
    /**
     * 删除日记
     */
    boolean delete(DiaryId id);
    
    /**
     * 统计用户的日记总数
     */
    int countByUserId(UserId userId);
    
    /**
     * 统计用户连续写日记的天数
     */
    int countConsecutiveDays(UserId userId);
    
    /**
     * 获取用户最近的日记列表
     */
    List<Diary> findRecentByUserId(UserId userId, int limit);
}
