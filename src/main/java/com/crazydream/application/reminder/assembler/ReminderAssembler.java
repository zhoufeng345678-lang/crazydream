package com.crazydream.application.reminder.assembler;

import com.crazydream.application.reminder.dto.*;
import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.reminder.model.aggregate.Reminder;
import com.crazydream.domain.shared.model.UserId;

import java.util.List;
import java.util.stream.Collectors;

public class ReminderAssembler {
    
    public static Reminder toDomain(CreateReminderCommand command, UserId userId) {
        return Reminder.create(
            userId,
            GoalId.of(command.getGoalId()),
            command.getTitle(),
            command.getRemindTime()
        );
    }
    
    public static ReminderDTO toDTO(Reminder reminder) {
        if (reminder == null) return null;
        
        ReminderDTO dto = new ReminderDTO();
        if (reminder.getId() != null) {
            dto.setId(reminder.getId().getValue());
        }
        dto.setUserId(reminder.getUserId().getValue());
        dto.setGoalId(reminder.getGoalId().getValue());
        dto.setTitle(reminder.getTitle());
        dto.setRemindTime(reminder.getRemindTime());
        dto.setRead(reminder.isRead());
        dto.setOverdue(reminder.isOverdue());
        dto.setCreateTime(reminder.getCreateTime());
        return dto;
    }
    
    public static List<ReminderDTO> toDTOList(List<Reminder> reminders) {
        return reminders.stream()
                .map(ReminderAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
