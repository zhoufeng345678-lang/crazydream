package com.crazydream.application.diary.assembler;

import com.crazydream.application.diary.command.CreateDiaryCommand;
import com.crazydream.application.diary.command.UpdateDiaryCommand;
import com.crazydream.application.diary.dto.DiaryDTO;
import com.crazydream.domain.diary.model.aggregate.Diary;
import com.crazydream.domain.diary.model.valueobject.DiaryCategory;
import com.crazydream.domain.diary.model.valueobject.DiaryMood;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.goal.model.valueobject.GoalId;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 日记组装器
 * 负责Command/DTO与领域对象之间的转换
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public class DiaryAssembler {
    
    /**
     * Command -> Domain
     */
    public static Diary toDomain(CreateDiaryCommand command) {
        Diary diary = Diary.create(
            UserId.of(command.getUserId()),
            command.getTitle(),
            command.getContent(),
            command.getDiaryDate() != null ? command.getDiaryDate() : LocalDate.now()
        );
        
        if (command.getCategory() != null) {
            diary.setCategory(DiaryCategory.fromCode(command.getCategory()));
        }
        
        if (command.getMood() != null) {
            diary.setMood(DiaryMood.fromCode(command.getMood()));
        }
        
        if (command.getWeather() != null) {
            diary.setWeather(command.getWeather());
        }
        
        if (command.getTags() != null) {
            command.getTags().forEach(diary::addTag);
        }
        
        if (command.getRelatedGoalId() != null) {
            diary.linkToGoal(GoalId.of(command.getRelatedGoalId()));
        }
        
        return diary;
    }
    
    /**
     * 应用更新命令到领域对象
     */
    public static void applyUpdateCommand(Diary diary, UpdateDiaryCommand command) {
        if (command.getTitle() != null || command.getContent() != null) {
            diary.updateContent(command.getTitle(), command.getContent());
        }
        
        if (command.getCategory() != null) {
            diary.setCategory(DiaryCategory.fromCode(command.getCategory()));
        }
        
        if (command.getMood() != null) {
            diary.setMood(DiaryMood.fromCode(command.getMood()));
        }
        
        if (command.getWeather() != null) {
            diary.setWeather(command.getWeather());
        }
        
        if (command.getTags() != null) {
            diary.setTags(command.getTags());
        }
    }
    
    /**
     * Domain -> DTO
     */
    public static DiaryDTO toDTO(Diary diary) {
        if (diary == null) {
            return null;
        }
        
        DiaryDTO dto = new DiaryDTO();
        dto.setId(diary.getId() != null ? diary.getId().getValue() : null);
        dto.setUserId(diary.getUserId().getValue());
        dto.setTitle(diary.getTitle());
        dto.setContent(diary.getContent());
        dto.setCategory(diary.getCategory() != null ? diary.getCategory().getCode() : null);
        dto.setTags(diary.getTags());
        dto.setMood(diary.getMood() != null ? diary.getMood().getCode() : null);
        dto.setWeather(diary.getWeather());
        dto.setRelatedGoalId(diary.getRelatedGoalId() != null ? diary.getRelatedGoalId().getValue() : null);
        dto.setImageUrls(diary.getImageUrls());
        dto.setIsPublic(diary.isPublic());
        dto.setViewCount(diary.getViewCount());
        dto.setDiaryDate(diary.getDiaryDate());
        dto.setCreateTime(diary.getCreateTime());
        dto.setUpdateTime(diary.getUpdateTime());
        
        return dto;
    }
    
    /**
     * Domain List -> DTO List
     */
    public static List<DiaryDTO> toDTOList(List<Diary> diaries) {
        return diaries.stream()
                .map(DiaryAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
