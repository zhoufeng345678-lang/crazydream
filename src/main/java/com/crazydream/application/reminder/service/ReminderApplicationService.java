package com.crazydream.application.reminder.service;

import com.crazydream.application.reminder.assembler.ReminderAssembler;
import com.crazydream.application.reminder.dto.*;
import com.crazydream.domain.reminder.model.aggregate.Reminder;
import com.crazydream.domain.reminder.model.valueobject.ReminderId;
import com.crazydream.domain.reminder.repository.ReminderRepository;
import com.crazydream.domain.shared.model.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReminderApplicationService {
    
    @Autowired
    private ReminderRepository reminderRepository;
    
    @Transactional
    public ReminderDTO createReminder(CreateReminderCommand command, Long userId) {
        Reminder reminder = ReminderAssembler.toDomain(command, UserId.of(userId));
        reminder = reminderRepository.save(reminder);
        return ReminderAssembler.toDTO(reminder);
    }
    
    public List<ReminderDTO> getUserReminders(Long userId) {
        List<Reminder> reminders = reminderRepository.findByUserId(UserId.of(userId));
        return ReminderAssembler.toDTOList(reminders);
    }
    
    public List<ReminderDTO> getUnreadReminders(Long userId) {
        List<Reminder> reminders = reminderRepository.findUnreadByUserId(UserId.of(userId));
        return ReminderAssembler.toDTOList(reminders);
    }
    
    @Transactional
    public ReminderDTO markAsRead(Long id, Long userId) {
        Reminder reminder = reminderRepository.findById(ReminderId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("提醒不存在: " + id));
        
        if (!reminder.belongsTo(UserId.of(userId))) {
            throw new IllegalArgumentException("无权限操作");
        }
        
        reminder.markAsRead();
        reminder = reminderRepository.save(reminder);
        return ReminderAssembler.toDTO(reminder);
    }
    
    @Transactional
    public boolean deleteReminder(Long id, Long userId) {
        Reminder reminder = reminderRepository.findById(ReminderId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("提醒不存在: " + id));
        
        if (!reminder.belongsTo(UserId.of(userId))) {
            throw new IllegalArgumentException("无权限操作");
        }
        
        return reminderRepository.delete(ReminderId.of(id));
    }
}
