package com.crazydream.application.diary.command;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * 创建日记命令
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Data
public class CreateDiaryCommand {
    private Long userId;
    private String title;
    private String content;
    private String category;
    private List<String> tags;
    private String mood;
    private String weather;
    private Long relatedGoalId;
    private LocalDate diaryDate;
}
