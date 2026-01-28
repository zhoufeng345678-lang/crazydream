package com.crazydream.application.file.assembler;

import com.crazydream.application.file.dto.FileDTO;
import com.crazydream.domain.file.model.aggregate.FileInfo;
import com.crazydream.domain.shared.model.UserId;

import java.util.List;
import java.util.stream.Collectors;

public class FileAssembler {
    
    public static FileInfo toDomain(String fileName, String fileUrl, Long fileSize, UserId userId) {
        return FileInfo.create(userId, fileName, fileUrl, fileSize);
    }
    
    public static FileDTO toDTO(FileInfo file) {
        if (file == null) return null;
        
        FileDTO dto = new FileDTO();
        if (file.getId() != null) {
            dto.setId(file.getId().getValue());
        }
        dto.setUserId(file.getUserId().getValue());
        dto.setFileName(file.getFileName());
        dto.setFileUrl(file.getFileUrl());
        dto.setFileType(file.getFileType().getCode());
        dto.setFileTypeDescription(file.getFileType().getDescription());
        dto.setFileSize(file.getFileSize());
        dto.setUploadTime(file.getUploadTime());
        return dto;
    }
    
    public static List<FileDTO> toDTOList(List<FileInfo> files) {
        return files.stream()
                .map(FileAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
