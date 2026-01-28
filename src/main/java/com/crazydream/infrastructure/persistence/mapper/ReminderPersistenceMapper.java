package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.ReminderPO;
import org.apache.ibatis.annotations.Mapper;
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
}
