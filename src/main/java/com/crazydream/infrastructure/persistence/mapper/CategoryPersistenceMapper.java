package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.CategoryPO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CategoryPersistenceMapper {
    int insert(CategoryPO category);
    int update(CategoryPO category);
    CategoryPO selectById(Long id);
    List<CategoryPO> selectAll();
    List<CategoryPO> selectByStatus(Integer status);
    int deleteById(Long id);
}
