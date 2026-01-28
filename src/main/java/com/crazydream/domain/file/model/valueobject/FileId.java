package com.crazydream.domain.file.model.valueobject;

import java.util.Objects;

public class FileId {
    private final Long value;
    
    private FileId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("文件ID不能为空或非正数");
        }
        this.value = value;
    }
    
    public static FileId of(Long value) {
        return new FileId(value);
    }
    
    public Long getValue() { return value; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileId fileId = (FileId) o;
        return Objects.equals(value, fileId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
