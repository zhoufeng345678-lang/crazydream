package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.HealthCheckupPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 健康体检持久化Mapper
 */
@Mapper
public interface HealthCheckupPersistenceMapper {
    
    /**
     * 插入体检记录
     */
    int insert(HealthCheckupPO healthCheckupPO);
    
    /**
     * 更新体检记录
     */
    int updateById(HealthCheckupPO healthCheckupPO);
    
    /**
     * 根据ID查询
     */
    HealthCheckupPO selectById(@Param("id") Long id);
    
    /**
     * 根据家庭成员ID查询列表
     */
    List<HealthCheckupPO> selectByFamilyMemberId(@Param("familyMemberId") Long familyMemberId);
    
    /**
     * 根据ID删除
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据家庭成员ID删除所有记录
     */
    int deleteByFamilyMemberId(@Param("familyMemberId") Long familyMemberId);
    
    /**
     * 查询所有有下次体检日期的记录
     */
    List<HealthCheckupPO> selectAllWithNextCheckupDate();
}
