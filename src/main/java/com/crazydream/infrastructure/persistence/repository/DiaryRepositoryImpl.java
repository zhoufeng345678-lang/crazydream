package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.diary.model.aggregate.Diary;
import com.crazydream.domain.diary.model.valueobject.DiaryId;
import com.crazydream.domain.diary.model.valueobject.DiaryCategory;
import com.crazydream.domain.diary.repository.DiaryRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.converter.DiaryConverter;
import com.crazydream.infrastructure.persistence.mapper.DiaryPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.DiaryPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 日记仓储实现(Infrastructure层)
 * 桥接领域模型与持久化
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Repository
public class DiaryRepositoryImpl implements DiaryRepository {
    
    @Autowired
    private DiaryPersistenceMapper mapper;
    
    @Override
    public Diary save(Diary diary) {
        DiaryPO po = DiaryConverter.toPO(diary);
        
        if (diary.getId() == null) {
            // 新增
            mapper.insert(po);
            // 设置生成的ID回到领域对象
            diary.setId(DiaryId.of(po.getId()));
        } else {
            // 更新
            mapper.update(po);
        }
        
        return diary;
    }
    
    @Override
    public Optional<Diary> findById(DiaryId id) {
        DiaryPO po = mapper.selectById(id.getValue());
        return Optional.ofNullable(DiaryConverter.toDomain(po));
    }
    
    @Override
    public List<Diary> findByUserId(UserId userId) {
        List<DiaryPO> poList = mapper.selectByUserId(userId.getValue());
        return poList.stream()
                .map(DiaryConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Diary> findByUserIdAndDateRange(UserId userId, LocalDate startDate, LocalDate endDate) {
        List<DiaryPO> poList = mapper.selectByUserIdAndDateRange(
                userId.getValue(), 
                startDate, 
                endDate
        );
        return poList.stream()
                .map(DiaryConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Diary> findByUserIdAndCategory(UserId userId, DiaryCategory category) {
        List<DiaryPO> poList = mapper.selectByUserIdAndCategory(
                userId.getValue(), 
                category.getCode()
        );
        return poList.stream()
                .map(DiaryConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean delete(DiaryId id) {
        return mapper.deleteById(id.getValue()) > 0;
    }
    
    @Override
    public int countByUserId(UserId userId) {
        return mapper.countByUserId(userId.getValue());
    }
    
    @Override
    public int countConsecutiveDays(UserId userId) {
        return mapper.countConsecutiveDays(userId.getValue());
    }
    
    @Override
    public List<Diary> findRecentByUserId(UserId userId, int limit) {
        List<DiaryPO> poList = mapper.selectRecentByUserId(userId.getValue(), limit);
        return poList.stream()
                .map(DiaryConverter::toDomain)
                .collect(Collectors.toList());
    }
}
