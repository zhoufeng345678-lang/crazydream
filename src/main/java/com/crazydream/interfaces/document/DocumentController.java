package com.crazydream.interfaces.document;

import com.crazydream.application.document.command.DocumentUploadCmd;
import com.crazydream.application.document.dto.DocumentCategoryDTO;
import com.crazydream.application.document.dto.DocumentDTO;
import com.crazydream.application.document.query.DocumentListQry;
import com.crazydream.application.document.service.DocumentApplicationService;
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

/**
 * 资料管理Controller
 */
@RestController
@RequestMapping("/api/v2/documents")
public class DocumentController {
    
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    
    @Autowired
    private DocumentApplicationService documentApplicationService;
    
    @Value("${security.test.default-user-id:1}")
    private Long defaultUserId;
    
    /**
     * 获取资料分类列表
     */
    @GetMapping("/categories")
    public ResponseResult<List<DocumentCategoryDTO>> getCategories() {
        try {
            List<DocumentCategoryDTO> categories = documentApplicationService.getCategories();
            return ResponseResult.success(categories);
        } catch (Exception e) {
            logger.error("获取资料分类失败", e);
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    /**
     * 上传文档
     */
    @PostMapping("/upload")
    public ResponseResult<DocumentDTO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tags", required = false) String tags) {
        try {
            Long userId = getCurrentUserId();
            
            DocumentUploadCmd cmd = new DocumentUploadCmd();
            cmd.setUserId(userId);
            cmd.setCategoryId(categoryId);
            cmd.setFile(file);
            cmd.setDescription(description);
            cmd.setTags(tags);
            
            DocumentDTO dto = documentApplicationService.uploadDocument(cmd);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            logger.error("上传文档失败", e);
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    /**
     * 获取文档列表
     */
    @GetMapping
    public ResponseResult<List<DocumentDTO>> getDocuments(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "keyword", required = false) String keyword) {
        try {
            Long userId = getCurrentUserId();
            
            DocumentListQry qry = new DocumentListQry();
            qry.setUserId(userId);
            qry.setCategoryId(categoryId);
            qry.setKeyword(keyword);
            
            List<DocumentDTO> documents = documentApplicationService.getDocuments(qry);
            return ResponseResult.success(documents);
        } catch (Exception e) {
            logger.error("获取文档列表失败", e);
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    /**
     * 获取文档详情
     */
    @GetMapping("/{id}")
    public ResponseResult<DocumentDTO> getDocumentDetail(@PathVariable Long id) {
        try {
            DocumentDTO dto = documentApplicationService.getDocumentDetail(id);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            logger.error("获取文档详情失败", e);
            return ResponseResult.error(404, e.getMessage());
        }
    }
    
    /**
     * 删除文档
     */
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteDocument(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            documentApplicationService.deleteDocument(id, userId);
            return ResponseResult.success(null);
        } catch (Exception e) {
            logger.error("删除文档失败", e);
            return ResponseResult.error(500, e.getMessage());
        }
    }
    
    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() 
            && !"anonymousUser".equals(authentication.getPrincipal())) {
            return Long.parseLong(authentication.getName());
        }
        logger.warn("未找到认证用户，使用默认用户ID: {}", defaultUserId);
        return defaultUserId;
    }
}
