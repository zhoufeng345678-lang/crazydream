package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.reminder.model.aggregate.Reminder;
import com.crazydream.domain.reminder.model.valueobject.ReminderId;
import com.crazydream.domain.reminder.repository.ReminderRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.converter.ReminderConverter;
import com.crazydream.infrastructure.persistence.mapper.ReminderPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.ReminderPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReminderRepositoryImpl implements ReminderRepository {
    
    @Autowired
    private ReminderPersistenceMapper mapper;
    
    @Override
    public Reminder save(Reminder reminder) {
        ReminderPO po = ReminderConverter.toPO(reminder);
        if (reminder.getId() == null) {
            mapper.insert(po);
            reminder.setId(ReminderId.of(po.getId()));
        } else {
            mapper.update(po);
        }
        return reminder;
    }
    
    @Override
    public Optional<Reminder> findById(ReminderId id) {
        ReminderPO po = mapper.selectById(id.getValue());
        return Optional.ofNullable(ReminderConverter.toDomain(po));
    }
    
    @Override
    public List<Reminder> findByUserId(UserId userId) {
        return mapper.selectByUserId(userId.getValue()).stream()
                .map(ReminderConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Reminder> findUnreadByUserId(UserId userId) {
        return mapper.selectUnreadByUserId(userId.getValue()).stream()
                .map(ReminderConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Reminder> findByGoalId(GoalId goalId) {
        return mapper.selectByGoalId(goalId.getValue()).stream()
                .map(ReminderConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean delete(ReminderId id) {
        return mapper.deleteById(id.getValue()) > 0;
    }
}
