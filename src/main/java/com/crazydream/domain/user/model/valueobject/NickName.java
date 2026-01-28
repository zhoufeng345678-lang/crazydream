package com.crazydream.domain.user.model.valueobject;

import java.util.Objects;

/**
 * 昵称值对象
 */
public class NickName {
    private static final int MAX_LENGTH = 50;
    
    private final String value;
    
    private NickName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("昵称不能为空");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("昵称长度不能超过" + MAX_LENGTH + "个字符");
        }
        this.value = value.trim();
    }
    
    public static NickName of(String value) {
        return new NickName(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NickName nickName = (NickName) o;
        return Objects.equals(value, nickName.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
