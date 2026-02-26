package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.DocumentCategoryPO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 资料分类数据访问接口
 */
@Mapper
public interface DocumentCategoryPersistenceMapper {
    
    /**
     * 插入分类
     */
    int insert(DocumentCategoryPO category);
    
    /**
     * 更新分类
     */
    int update(DocumentCategoryPO category);
    
    /**
     * 根据ID查询
     */
    DocumentCategoryPO selectById(Long id);
    
    /**
     * 查询所有启用的分类
     */
    List<DocumentCategoryPO> selectAllEnabled();
    
    /**
     * 查询所有分类
     */
    List<DocumentCategoryPO> selectAll();
}
