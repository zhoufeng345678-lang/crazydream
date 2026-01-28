package com.crazydream.infrastructure.persistence.converter;

import com.crazydream.domain.file.model.aggregate.FileInfo;
import com.crazydream.domain.file.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.po.FilePO;

public class FileConverter {
    
    public static FileInfo toDomain(FilePO po) {
        if (po == null) return null;
        
        return FileInfo.rebuild(
            FileId.of(po.getId()),
            UserId.of(po.getUserId()),
            po.getFileName(),
            po.getFileUrl(),
            FileType.fromCode(po.getFileType()),
            po.getFileSize(),
            po.getUploadTime()
        );
    }
    
    public static FilePO toPO(FileInfo file) {
        if (file == null) return null;
        
        FilePO po = new FilePO();
        if (file.getId() != null) {
            po.setId(file.getId().getValue());
        }
        po.setUserId(file.getUserId().getValue());
        po.setFileName(file.getFileName());
        po.setFileUrl(file.getFileUrl());
        po.setFileType(file.getFileType().getCode());
        po.setFileSize(file.getFileSize());
        po.setUploadTime(file.getUploadTime());
        return po;
    }
}
