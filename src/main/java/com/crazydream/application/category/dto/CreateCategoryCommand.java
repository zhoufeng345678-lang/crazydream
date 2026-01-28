package com.crazydream.application.category.dto;

import lombok.Data;

@Data
public class CreateCategoryCommand {
    private String name;
    private String icon;
    private String color;
    private Integer sort;
}
