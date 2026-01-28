package com.crazydream.application.category.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String icon;
    private String color;
    private Integer sort;
    private Boolean enabled;
}
