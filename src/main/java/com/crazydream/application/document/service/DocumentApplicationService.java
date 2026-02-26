package com.crazydream.application.document.service;

import com.crazydream.application.document.assembler.DocumentAssembler;
import com.crazydream.application.document.command.DocumentUploadCmd;
import com.crazydream.application.document.dto.DocumentCategoryDTO;
import com.crazydream.application.document.dto.DocumentDTO;
import com.crazydream.application.document.query.DocumentListQry;
import com.crazydream.domain.document.model.aggregate.Document;
import com.crazydream.domain.document.model.aggregate.DocumentCategory;
import com.crazydream.domain.document.model.valueobject.*;
import com.crazydream.domain.document.repository.DocumentCategoryRepository;
import com.crazydream.domain.document.repository.DocumentRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.oss.OssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 资料管理应用服务
 */
@Service
public class DocumentApplicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(DocumentApplicationService.class);
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private DocumentCategoryRepository categoryRepository;
    
    @Autowired
    private OssService ossService;
    
    /**
     * 获取所有资料分类
     */
    public List<DocumentCategoryDTO> getCategories() {
        List<DocumentCategory> categories = categoryRepository.findAllEnabled();
        return DocumentAssembler.toCategoryDTOList(categories);
    }
    
    /**
     * 上传文档
     */
    @Transactional(rollbackFor = Exception.class)
    public DocumentDTO uploadDocument(DocumentUploadCmd cmd) throws IOException {
        MultipartFile file = cmd.getFile();
        
        // 验证文件
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        // 获取文件信息
        String originalName = file.getOriginalFilename();
        String extension = getFileExtension(originalName);
        FileType fileType = determineFileType(extension);
        
        // 上传到OSS
        String fileUrl = ossService.uploadDocument(file, cmd.getUserId(), "documents");
        
        // 创建文档领域对象
        Document document = Document.create(
            UserId.of(cmd.getUserId()),
            DocumentCategoryId.of(cmd.getCategoryId()),
            originalName,
            fileType,
            extension,
            file.getSize(),
            fileUrl,
            fileUrl,
            file.getContentType()
        );
        
        // 设置描述和标签
        if (cmd.getDescription() != null || cmd.getTags() != null) {
            document.update(null, cmd.getDescription(), cmd.getTags());
        }
        
        // 保存到数据库
        document = documentRepository.save(document);
        
        logger.info("文档上传成功，documentId: {}, fileName: {}", 
            document.getId().getValue(), originalName);
        
        return DocumentAssembler.toDTO(document);
    }
    
    /**
     * 获取文档列表
     */
    public List<DocumentDTO> getDocuments(DocumentListQry qry) {
        UserId userId = UserId.of(qry.getUserId());
        List<Document> documents;
        
        if (qry.getKeyword() != null && !qry.getKeyword().trim().isEmpty()) {
            // 搜索文档
            documents = documentRepository.searchByFileName(userId, qry.getKeyword());
        } else if (qry.getCategoryId() != null) {
            // 按分类查询
            documents = documentRepository.findByUserIdAndCategoryId(
                userId, 
                DocumentCategoryId.of(qry.getCategoryId())
            );
        } else {
            // 查询所有文档
            documents = documentRepository.findByUserId(userId);
        }
        
        return DocumentAssembler.toDTOList(documents);
    }
    
    /**
     * 删除文档
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocument(Long documentId, Long userId) {
        DocumentId docId = DocumentId.of(documentId);
        Document document = documentRepository.findById(docId)
            .orElseThrow(() -> new IllegalArgumentException("文档不存在"));
        
        // 验证权限
        if (!document.getUserId().getValue().equals(userId)) {
            throw new IllegalArgumentException("无权限删除该文档");
        }
        
        // 删除文档
        document.delete();
        documentRepository.save(document);
        
        logger.info("文档删除成功，documentId: {}", documentId);
    }
    
    /**
     * 获取文档详情
     */
    public DocumentDTO getDocumentDetail(Long documentId) {
        Document document = documentRepository.findById(DocumentId.of(documentId))
            .orElseThrow(() -> new IllegalArgumentException("文档不存在"));
        
        // 增加查看次数
        document.incrementViewCount();
        documentRepository.save(document);
        
        return DocumentAssembler.toDTO(document);
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
    
    /**
     * 判断文件类型
     */
    private FileType determineFileType(String extension) {
        if (extension == null) {
            return FileType.IMAGE;
        }
        
        extension = extension.toLowerCase();
        if (extension.equals(".jpg") || extension.equals(".jpeg") || 
            extension.equals(".png") || extension.equals(".gif") ||
            extension.equals(".bmp") || extension.equals(".webp")) {
            return FileType.IMAGE;
        } else if (extension.equals(".pdf")) {
            return FileType.PDF;
        } else if (extension.equals(".doc")) {
            return FileType.DOC;
        } else if (extension.equals(".docx")) {
            return FileType.DOCX;
        } else if (extension.equals(".xls")) {
            return FileType.XLS;
        } else if (extension.equals(".xlsx")) {
            return FileType.XLSX;
        } else if (extension.equals(".ppt")) {
            return FileType.PPT;
        } else if (extension.equals(".pptx")) {
            return FileType.PPTX;
        } else if (extension.equals(".txt")) {
            return FileType.TXT;
        } else {
            return FileType.IMAGE; // 默认图片类型
        }
    }
}
