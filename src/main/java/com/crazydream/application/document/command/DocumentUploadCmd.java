package com.crazydream.application.document.command;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

/**
 * 文档上传命令
 */
@Data
public class DocumentUploadCmd {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    @NotNull(message = "文件不能为空")
    private MultipartFile file;
    
    private String description;
    
    private String tags;
}
