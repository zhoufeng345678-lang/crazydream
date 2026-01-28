package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.FilePO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface FilePersistenceMapper {
    int insert(FilePO file);
    FilePO selectById(Long id);
    List<FilePO> selectByUserId(Long userId);
    int deleteById(Long id);
}
