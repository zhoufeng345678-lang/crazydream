package com.crazydream.application.diary.command;

import lombok.Data;
import java.util.List;

/**
 * 更新日记命令
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Data
public class UpdateDiaryCommand {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String category;
    private List<String> tags;
    private String mood;
    private String weather;
}
