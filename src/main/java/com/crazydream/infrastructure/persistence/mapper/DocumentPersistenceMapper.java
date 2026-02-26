package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.DocumentPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 资料文档数据访问接口
 */
@Mapper
public interface DocumentPersistenceMapper {
    
    /**
     * 插入文档
     */
    int insert(DocumentPO document);
    
    /**
     * 更新文档
     */
    int update(DocumentPO document);
    
    /**
     * 根据ID查询
     */
    DocumentPO selectById(Long id);
    
    /**
     * 根据用户ID和分类ID查询
     */
    List<DocumentPO> selectByUserIdAndCategoryId(@Param("userId") Long userId, 
                                                   @Param("categoryId") Long categoryId);
    
    /**
     * 根据用户ID查询所有文档
     */
    List<DocumentPO> selectByUserId(Long userId);
    
    /**
     * 搜索文档（按文件名）
     */
    List<DocumentPO> searchByFileName(@Param("userId") Long userId, 
                                       @Param("keyword") String keyword);
    
    /**
     * 删除文档
     */
    int deleteById(Long id);
    
    /**
     * 增加下载次数
     */
    int incrementDownloadCount(Long id);
    
    /**
     * 增加查看次数
     */
    int incrementViewCount(Long id);
}
