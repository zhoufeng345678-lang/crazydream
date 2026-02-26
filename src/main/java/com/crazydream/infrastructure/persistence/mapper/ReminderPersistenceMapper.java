package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.ReminderPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ReminderPersistenceMapper {
    int insert(ReminderPO reminder);
    int update(ReminderPO reminder);
    ReminderPO selectById(Long id);
    List<ReminderPO> selectByUserId(Long userId);
    List<ReminderPO> selectUnreadByUserId(Long userId);
    List<ReminderPO> selectByGoalId(Long goalId);
    int deleteById(Long id);
    
    /**
     * 查询用户的特定类型和关联ID的提醒数量
     * 用于判断是否已存在提醒
     */
    int countByUserIdAndTypeAndRelatedId(
        @Param("userId") Long userId,
        @Param("reminderType") String reminderType,
        @Param("relatedEntityId") Long relatedEntityId
    );
    
    /**
     * 删除用户的特定类型和关联ID的所有提醒
     * 用于删除旧的提醒后再创建新的
     */
    int deleteByUserIdAndTypeAndRelatedId(
        @Param("userId") Long userId,
        @Param("reminderType") String reminderType,
        @Param("relatedEntityId") Long relatedEntityId
    );
}
