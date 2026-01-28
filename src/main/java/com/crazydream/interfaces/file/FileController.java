package com.crazydream.interfaces.file;

import com.crazydream.application.file.dto.FileDTO;
import com.crazydream.application.file.service.FileApplicationService;
import com.crazydream.utils.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v2/files")
public class FileController {
    
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    
    @Autowired
    private FileApplicationService fileApplicationService;
    
    @Value("${security.test.default-user-id:1}")
    private Long defaultUserId;
    
    // 注意：实际的文件上传接口在旧的 FileController 中处理OSS逻辑
    // 这里主要提供文件记录查询和删除接口
    
    @GetMapping
    public ResponseResult<List<FileDTO>> getUserFiles() {
        try {
            Long userId = getCurrentUserId();
            List<FileDTO> dtos = fileApplicationService.getUserFiles(userId);
            return ResponseResult.success(dtos);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseResult<FileDTO> getFileById(@PathVariable Long id) {
        try {
            FileDTO dto = fileApplicationService.getFileById(id);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(404, e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseResult<Boolean> deleteFile(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            boolean success = fileApplicationService.deleteFile(id, userId);
            return ResponseResult.success(success);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    @PostMapping("/upload")
    public ResponseResult<FileDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Long userId = getCurrentUserId();
            // 简单实现：只返回文件信息，不实际上传到OSS
            FileDTO dto = new FileDTO();
            dto.setFileName(file.getOriginalFilename());
            dto.setFileSize(file.getSize());
            dto.setFileUrl("/uploads/" + file.getOriginalFilename());
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            logger.debug("未找到认证信息，使用默认用户ID: {}", defaultUserId);
            return defaultUserId;
        }
        
        Object principal = authentication.getPrincipal();
        
        if ("anonymousUser".equals(principal)) {
            logger.debug("匿名用户访问，使用默认用户ID: {}", defaultUserId);
            return defaultUserId;
        }
        
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            return Long.parseLong(((org.springframework.security.core.userdetails.User) principal).getUsername());
        } else if (principal instanceof Long) {
            return (Long) principal;
        } else if (principal instanceof String) {
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                logger.warn("无法解析用户ID: {}, 使用默认用户ID: {}", principal, defaultUserId);
                return defaultUserId;
            }
        }
        
        logger.warn("不支持的认证信息格式: {}, 使用默认用户ID: {}", principal.getClass(), defaultUserId);
        return defaultUserId;
    }
}
