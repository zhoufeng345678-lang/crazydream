package com.crazydream.domain.reminder.repository;

import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.reminder.model.aggregate.Reminder;
import com.crazydream.domain.reminder.model.valueobject.ReminderId;
import com.crazydream.domain.shared.model.UserId;

import java.util.List;
import java.util.Optional;

public interface ReminderRepository {
    Reminder save(Reminder reminder);
    Optional<Reminder> findById(ReminderId id);
    List<Reminder> findByUserId(UserId userId);
    List<Reminder> findUnreadByUserId(UserId userId);
    List<Reminder> findByGoalId(GoalId goalId);
    boolean delete(ReminderId id);
}
