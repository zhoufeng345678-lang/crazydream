package com.crazydream.domain.file.repository;

import com.crazydream.domain.file.model.aggregate.FileInfo;
import com.crazydream.domain.file.model.valueobject.FileId;
import com.crazydream.domain.shared.model.UserId;

import java.util.List;
import java.util.Optional;

public interface FileRepository {
    FileInfo save(FileInfo file);
    Optional<FileInfo> findById(FileId id);
    List<FileInfo> findByUserId(UserId userId);
    boolean delete(FileId id);
}
