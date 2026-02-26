package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.FamilyMemberPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 家庭成员持久化Mapper
 */
@Mapper
public interface FamilyMemberPersistenceMapper {
    
    /**
     * 插入家庭成员
     */
    int insert(FamilyMemberPO familyMemberPO);
    
    /**
     * 更新家庭成员
     */
    int updateById(FamilyMemberPO familyMemberPO);
    
    /**
     * 根据ID查询
     */
    FamilyMemberPO selectById(@Param("id") Long id);
    
    /**
     * 根据用户ID查询列表
     */
    List<FamilyMemberPO> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据ID删除
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 检查是否存在
     */
    int countById(@Param("id") Long id);
    
    /**
     * 查询所有有生日的成员
     */
    List<FamilyMemberPO> selectAllWithBirthday();
}
