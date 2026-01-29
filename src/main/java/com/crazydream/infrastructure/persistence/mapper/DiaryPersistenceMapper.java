package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.DiaryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 日记持久化Mapper
 * 仅负责数据库操作,不包含业务逻辑
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Mapper
public interface DiaryPersistenceMapper {
    
    /**
     * 插入日记
     */
    int insert(DiaryPO diary);
    
    /**
     * 更新日记
     */
    int update(DiaryPO diary);
    
    /**
     * 根据ID查询
     */
    DiaryPO selectById(Long id);
    
    /**
     * 根据用户ID查询
     */
    List<DiaryPO> selectByUserId(Long userId);
    
    /**
     * 根据用户ID和日期范围查询
     */
    List<DiaryPO> selectByUserIdAndDateRange(@Param("userId") Long userId, 
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
    
    /**
     * 根据用户ID和分类查询
     */
    List<DiaryPO> selectByUserIdAndCategory(@Param("userId") Long userId, 
                                             @Param("category") String category);
    
    /**
     * 删除
     */
    int deleteById(Long id);
    
    /**
     * 统计用户的日记总数
     */
    int countByUserId(@Param("userId") Long userId);
    
    /**
     * 统计用户连续写日记的天数
     */
    int countConsecutiveDays(@Param("userId") Long userId);
    
    /**
     * 获取用户最近的日记列表
     */
    List<DiaryPO> selectRecentByUserId(@Param("userId") Long userId, @Param("limit") int limit);
}
