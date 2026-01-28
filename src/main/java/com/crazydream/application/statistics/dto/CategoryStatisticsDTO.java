package com.crazydream.application.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStatisticsDTO {
    private Long categoryId;
    private String categoryName;
    private Integer count;
    private Integer completed;
    private Integer inProgress;
}
