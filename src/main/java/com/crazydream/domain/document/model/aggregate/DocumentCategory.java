package com.crazydream.domain.document.model.aggregate;

import com.crazydream.domain.document.model.valueobject.DocumentCategoryId;
import java.time.LocalDateTime;

/**
 * 资料分类聚合根
 */
public class DocumentCategory {
    
    private DocumentCategoryId id;
    private String name;
    private String description;
    private String icon;
    private String color;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private DocumentCategory() {
    }
    
    /**
     * 创建资料分类
     */
    public static DocumentCategory create(String name, String description, String icon, String color, Integer sortOrder) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("分类名称不能为空");
        }
        
        DocumentCategory category = new DocumentCategory();
        category.name = name.trim();
        category.description = description;
        category.icon = icon != null ? icon : "📁";
        category.color = color != null ? color : "#667eea";
        category.sortOrder = sortOrder != null ? sortOrder : 0;
        category.status = 1;
        category.createTime = LocalDateTime.now();
        category.updateTime = LocalDateTime.now();
        return category;
    }
    
    /**
     * 重建资料分类（从数据库加载）
     */
    public static DocumentCategory rebuild(DocumentCategoryId id, String name, String description, 
                                           String icon, String color, Integer sortOrder, Integer status,
                                           LocalDateTime createTime, LocalDateTime updateTime) {
        DocumentCategory category = new DocumentCategory();
        category.id = id;
        category.name = name;
        category.description = description;
        category.icon = icon;
        category.color = color;
        category.sortOrder = sortOrder;
        category.status = status;
        category.createTime = createTime;
        category.updateTime = updateTime;
        return category;
    }
    
    /**
     * 更新分类信息
     */
    public void update(String name, String description, String icon, String color, Integer sortOrder) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
        if (description != null) {
            this.description = description;
        }
        if (icon != null) {
            this.icon = icon;
        }
        if (color != null) {
            this.color = color;
        }
        if (sortOrder != null) {
            this.sortOrder = sortOrder;
        }
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 禁用分类
     */
    public void disable() {
        this.status = 0;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 启用分类
     */
    public void enable() {
        this.status = 1;
        this.updateTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public DocumentCategoryId getId() {
        return id;
    }
    
    public void setId(DocumentCategoryId id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public String getColor() {
        return color;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
