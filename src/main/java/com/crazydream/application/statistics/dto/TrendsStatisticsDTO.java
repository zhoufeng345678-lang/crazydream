package com.crazydream.application.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrendsStatisticsDTO {
    private List<String> dates;
    private List<Integer> completed;
    private List<Integer> inProgress;
}
