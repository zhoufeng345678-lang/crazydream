package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.file.model.aggregate.FileInfo;
import com.crazydream.domain.file.model.valueobject.FileId;
import com.crazydream.domain.file.repository.FileRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.converter.FileConverter;
import com.crazydream.infrastructure.persistence.mapper.FilePersistenceMapper;
import com.crazydream.infrastructure.persistence.po.FilePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FileRepositoryImpl implements FileRepository {
    
    @Autowired
    private FilePersistenceMapper mapper;
    
    @Override
    public FileInfo save(FileInfo file) {
        FilePO po = FileConverter.toPO(file);
        mapper.insert(po);
        file.setId(FileId.of(po.getId()));
        return file;
    }
    
    @Override
    public Optional<FileInfo> findById(FileId id) {
        FilePO po = mapper.selectById(id.getValue());
        return Optional.ofNullable(FileConverter.toDomain(po));
    }
    
    @Override
    public List<FileInfo> findByUserId(UserId userId) {
        return mapper.selectByUserId(userId.getValue()).stream()
                .map(FileConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean delete(FileId id) {
        return mapper.deleteById(id.getValue()) > 0;
    }
}
