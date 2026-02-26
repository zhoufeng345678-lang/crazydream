package com.crazydream.domain.family.model.valueobject;

/**
 * 家庭关系类型枚举
 */
public enum RelationType {
    FATHER("父亲"),
    MOTHER("母亲"),
    SPOUSE("配偶"),
    SON("儿子"),
    DAUGHTER("女儿"),
    BROTHER("兄弟"),
    SISTER("姐妹"),
    GRANDFATHER("祖父/外祖父"),
    GRANDMOTHER("祖母/外祖母"),
    OTHER("其他");
    
    private final String description;
    
    RelationType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据字符串获取枚举
     */
    public static RelationType fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("关系类型不能为空");
        }
        try {
            return RelationType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("不支持的关系类型: " + value);
        }
    }
}
