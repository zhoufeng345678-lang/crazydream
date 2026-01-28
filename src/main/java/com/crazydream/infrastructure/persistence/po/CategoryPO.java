package com.crazydream.infrastructure.persistence.po;

import lombok.Data;

@Data
public class CategoryPO {
    private Long id;
    private String name;
    private String icon;
    private String color;
    private Integer sort;
    private Integer status;  // 1-启用，0-禁用
}
