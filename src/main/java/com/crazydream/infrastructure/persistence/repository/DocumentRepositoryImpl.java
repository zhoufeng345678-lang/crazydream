package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.document.model.aggregate.Document;
import com.crazydream.domain.document.model.valueobject.DocumentCategoryId;
import com.crazydream.domain.document.model.valueobject.DocumentId;
import com.crazydream.domain.document.repository.DocumentRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.converter.DocumentConverter;
import com.crazydream.infrastructure.persistence.mapper.DocumentPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.DocumentPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 文档仓储实现
 */
@Repository
public class DocumentRepositoryImpl implements DocumentRepository {
    
    @Autowired
    private DocumentPersistenceMapper mapper;
    
    @Override
    public Document save(Document document) {
        DocumentPO po = DocumentConverter.toPO(document);
        if (document.getId() == null) {
            mapper.insert(po);
            document.setId(DocumentId.of(po.getId()));
        } else {
            mapper.update(po);
        }
        return document;
    }
    
    @Override
    public Optional<Document> findById(DocumentId id) {
        DocumentPO po = mapper.selectById(id.getValue());
        return Optional.ofNullable(DocumentConverter.toDomain(po));
    }
    
    @Override
    public List<Document> findByUserIdAndCategoryId(UserId userId, DocumentCategoryId categoryId) {
        List<DocumentPO> poList = mapper.selectByUserIdAndCategoryId(
            userId.getValue(), 
            categoryId.getValue()
        );
        return poList.stream()
                .map(DocumentConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Document> findByUserId(UserId userId) {
        List<DocumentPO> poList = mapper.selectByUserId(userId.getValue());
        return poList.stream()
                .map(DocumentConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Document> searchByFileName(UserId userId, String keyword) {
        List<DocumentPO> poList = mapper.searchByFileName(userId.getValue(), keyword);
        return poList.stream()
                .map(DocumentConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void delete(DocumentId id) {
        mapper.deleteById(id.getValue());
    }
}
