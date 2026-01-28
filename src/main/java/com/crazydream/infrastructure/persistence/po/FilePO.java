package com.crazydream.infrastructure.persistence.po;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FilePO {
    private Long id;
    private Long userId;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private LocalDateTime uploadTime;
}
