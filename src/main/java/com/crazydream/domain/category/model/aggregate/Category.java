package com.crazydream.domain.category.model.aggregate;

import com.crazydream.domain.shared.model.CategoryId;
import com.crazydream.domain.category.model.valueobject.CategoryName;

import java.util.Objects;

/**
 * 分类聚合根（充血模型）
 */
public class Category {
    private CategoryId id;
    private CategoryName name;
    private String icon;
    private String color;
    private Integer sort;
    private boolean enabled;
    
    private Category() {}
    
    public static Category create(CategoryName name, String icon, String color, Integer sort) {
        Category category = new Category();
        category.name = name;
        category.icon = icon;
        category.color = color;
        category.sort = sort;
        category.enabled = true;
        return category;
    }
    
    public static Category rebuild(CategoryId id, CategoryName name, String icon, 
                                   String color, Integer sort, boolean enabled) {
        Category category = new Category();
        category.id = id;
        category.name = name;
        category.icon = icon;
        category.color = color;
        category.sort = sort;
        category.enabled = enabled;
        return category;
    }
    
    public void update(CategoryName name, String icon, String color, Integer sort) {
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.sort = sort;
    }
    
    public void disable() {
        this.enabled = false;
    }
    
    public void enable() {
        this.enabled = true;
    }
    
    // Getters
    public CategoryId getId() { return id; }
    public CategoryName getName() { return name; }
    public String getIcon() { return icon; }
    public String getColor() { return color; }
    public Integer getSort() { return sort; }
    public boolean isEnabled() { return enabled; }
    
    public void setId(CategoryId id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
