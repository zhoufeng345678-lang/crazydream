package com.crazydream.application.document.dto;

import lombok.Data;

/**
 * 资料分类DTO
 */
@Data
public class DocumentCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private String color;
    private Integer sortOrder;
}
