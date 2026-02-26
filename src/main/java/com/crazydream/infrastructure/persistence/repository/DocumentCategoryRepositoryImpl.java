package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.document.model.aggregate.DocumentCategory;
import com.crazydream.domain.document.model.valueobject.DocumentCategoryId;
import com.crazydream.domain.document.repository.DocumentCategoryRepository;
import com.crazydream.infrastructure.persistence.converter.DocumentCategoryConverter;
import com.crazydream.infrastructure.persistence.mapper.DocumentCategoryPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.DocumentCategoryPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 资料分类仓储实现
 */
@Repository
public class DocumentCategoryRepositoryImpl implements DocumentCategoryRepository {
    
    @Autowired
    private DocumentCategoryPersistenceMapper mapper;
    
    @Override
    public DocumentCategory save(DocumentCategory category) {
        DocumentCategoryPO po = DocumentCategoryConverter.toPO(category);
        if (category.getId() == null) {
            mapper.insert(po);
            category.setId(DocumentCategoryId.of(po.getId()));
        } else {
            mapper.update(po);
        }
        return category;
    }
    
    @Override
    public Optional<DocumentCategory> findById(DocumentCategoryId id) {
        DocumentCategoryPO po = mapper.selectById(id.getValue());
        return Optional.ofNullable(DocumentCategoryConverter.toDomain(po));
    }
    
    @Override
    public List<DocumentCategory> findAllEnabled() {
        List<DocumentCategoryPO> poList = mapper.selectAllEnabled();
        return poList.stream()
                .map(DocumentCategoryConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DocumentCategory> findAll() {
        List<DocumentCategoryPO> poList = mapper.selectAll();
        return poList.stream()
                .map(DocumentCategoryConverter::toDomain)
                .collect(Collectors.toList());
    }
}
