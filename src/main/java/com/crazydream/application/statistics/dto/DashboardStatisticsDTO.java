package com.crazydream.application.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatisticsDTO {
    private Map<String, Integer> goalStatistics;
    private Long userId;
    private String username;
}
