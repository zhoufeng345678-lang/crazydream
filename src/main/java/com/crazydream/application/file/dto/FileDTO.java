package com.crazydream.application.file.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileDTO {
    private Long id;
    private Long userId;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private String fileTypeDescription;
    private Long fileSize;
    private LocalDateTime uploadTime;
}
