package com.crazydream.application.file.service;

import com.crazydream.application.file.assembler.FileAssembler;
import com.crazydream.application.file.dto.FileDTO;
import com.crazydream.domain.file.model.aggregate.FileInfo;
import com.crazydream.domain.file.model.valueobject.FileId;
import com.crazydream.domain.file.repository.FileRepository;
import com.crazydream.domain.shared.model.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileApplicationService {
    
    @Autowired
    private FileRepository fileRepository;
    
    // 注意：实际的OSS上传逻辑在 FileService 中，这里只处理领域逻辑
    @Transactional
    public FileDTO recordUpload(String fileName, String fileUrl, Long fileSize, Long userId) {
        FileInfo file = FileAssembler.toDomain(fileName, fileUrl, fileSize, UserId.of(userId));
        file = fileRepository.save(file);
        return FileAssembler.toDTO(file);
    }
    
    public List<FileDTO> getUserFiles(Long userId) {
        List<FileInfo> files = fileRepository.findByUserId(UserId.of(userId));
        return FileAssembler.toDTOList(files);
    }
    
    public FileDTO getFileById(Long id) {
        FileInfo file = fileRepository.findById(FileId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("文件不存在: " + id));
        return FileAssembler.toDTO(file);
    }
    
    @Transactional
    public boolean deleteFile(Long id, Long userId) {
        FileInfo file = fileRepository.findById(FileId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("文件不存在: " + id));
        
        if (!file.belongsTo(UserId.of(userId))) {
            throw new IllegalArgumentException("无权限操作");
        }
        
        return fileRepository.delete(FileId.of(id));
    }
}
