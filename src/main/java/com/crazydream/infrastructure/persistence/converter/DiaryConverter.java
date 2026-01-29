package com.crazydream.infrastructure.persistence.converter;

import com.crazydream.domain.diary.model.aggregate.Diary;
import com.crazydream.domain.diary.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.infrastructure.persistence.po.DiaryPO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Diary领域模型与持久化对象转换器
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public class DiaryConverter {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * PO -> Domain Entity
     */
    public static Diary toDomain(DiaryPO po) {
        if (po == null) {
            return null;
        }
        
        // 解析JSON字段
        List<String> tags = parseJsonArray(po.getTags());
        List<String> imageUrls = parseJsonArray(po.getImageUrls());
        
        return Diary.rebuild(
            DiaryId.of(po.getId()),
            UserId.of(po.getUserId()),
            po.getTitle(),
            po.getContent(),
            DiaryCategory.fromCode(po.getCategory()),
            tags,
            DiaryMood.fromCode(po.getMood()),
            po.getWeather(),
            po.getRelatedGoalId() != null ? GoalId.of(po.getRelatedGoalId()) : null,
            imageUrls,
            po.getIsPublic() != null && po.getIsPublic() == 1,
            po.getViewCount() != null ? po.getViewCount() : 0,
            po.getDiaryDate(),
            po.getCreateTime(),
            po.getUpdateTime()
        );
    }
    
    /**
     * Domain Entity -> PO
     */
    public static DiaryPO toPO(Diary diary) {
        if (diary == null) {
            return null;
        }
        
        DiaryPO po = new DiaryPO();
        if (diary.getId() != null) {
            po.setId(diary.getId().getValue());
        }
        po.setUserId(diary.getUserId().getValue());
        po.setTitle(diary.getTitle());
        po.setContent(diary.getContent());
        po.setCategory(diary.getCategory() != null ? diary.getCategory().getCode() : null);
        po.setTags(toJsonArray(diary.getTags()));
        po.setMood(diary.getMood() != null ? diary.getMood().getCode() : null);
        po.setWeather(diary.getWeather());
        po.setRelatedGoalId(diary.getRelatedGoalId() != null ? diary.getRelatedGoalId().getValue() : null);
        po.setImageUrls(toJsonArray(diary.getImageUrls()));
        po.setIsPublic(diary.isPublic() ? 1 : 0);
        po.setViewCount(diary.getViewCount());
        po.setDiaryDate(diary.getDiaryDate());
        po.setCreateTime(diary.getCreateTime());
        po.setUpdateTime(diary.getUpdateTime());
        
        return po;
    }
    
    /**
     * 解析JSON数组
     */
    private static List<String> parseJsonArray(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * 转换为JSON数组
     */
    private static String toJsonArray(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            return null;
        }
    }
}
