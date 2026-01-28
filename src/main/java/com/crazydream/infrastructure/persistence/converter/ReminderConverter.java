package com.crazydream.infrastructure.persistence.converter;

import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.reminder.model.aggregate.Reminder;
import com.crazydream.domain.reminder.model.valueobject.ReminderId;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.po.ReminderPO;

public class ReminderConverter {
    
    public static Reminder toDomain(ReminderPO po) {
        if (po == null) return null;
        
        return Reminder.rebuild(
            ReminderId.of(po.getId()),
            UserId.of(po.getUserId()),
            GoalId.of(po.getGoalId()),
            po.getTitle(),
            po.getRemindTime(),
            po.getIsRead() != null && po.getIsRead(),
            po.getCreateTime(),
            po.getUpdateTime()
        );
    }
    
    public static ReminderPO toPO(Reminder reminder) {
        if (reminder == null) return null;
        
        ReminderPO po = new ReminderPO();
        if (reminder.getId() != null) {
            po.setId(reminder.getId().getValue());
        }
        po.setUserId(reminder.getUserId().getValue());
        po.setGoalId(reminder.getGoalId().getValue());
        po.setTitle(reminder.getTitle());
        po.setRemindTime(reminder.getRemindTime());
        po.setIsRead(reminder.isRead());
        po.setCreateTime(reminder.getCreateTime());
        po.setUpdateTime(reminder.getUpdateTime());
        return po;
    }
}
