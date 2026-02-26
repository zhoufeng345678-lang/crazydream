package com.crazydream.application.document.query;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 文档列表查询
 */
@Data
public class DocumentListQry {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    private Long categoryId;
    
    private String keyword;
}
