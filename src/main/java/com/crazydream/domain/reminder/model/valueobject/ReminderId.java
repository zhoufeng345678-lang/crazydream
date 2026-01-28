package com.crazydream.domain.reminder.model.valueobject;

import java.util.Objects;

public class ReminderId {
    private final Long value;
    
    private ReminderId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("提醒ID不能为空或非正数");
        }
        this.value = value;
    }
    
    public static ReminderId of(Long value) {
        return new ReminderId(value);
    }
    
    public Long getValue() { return value; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReminderId that = (ReminderId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
