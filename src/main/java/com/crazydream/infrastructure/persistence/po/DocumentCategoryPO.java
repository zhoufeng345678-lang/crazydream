package com.crazydream.infrastructure.persistence.po;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 资料分类持久化对象
 */
@Data
public class DocumentCategoryPO {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private String color;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
