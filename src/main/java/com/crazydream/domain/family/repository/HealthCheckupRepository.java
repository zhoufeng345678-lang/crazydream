package com.crazydream.domain.family.repository;

import com.crazydream.domain.family.model.entity.HealthCheckup;
import com.crazydream.domain.family.model.valueobject.FamilyMemberId;

import java.util.List;
import java.util.Optional;

/**
 * 健康体检仓储接口
 */
public interface HealthCheckupRepository {
    
    /**
     * 保存体检记录
     */
    HealthCheckup save(HealthCheckup healthCheckup);
    
    /**
     * 根据ID查询体检记录
     */
    Optional<HealthCheckup> findById(Long id);
    
    /**
     * 查询家庭成员的所有体检记录
     */
    List<HealthCheckup> findByFamilyMemberId(FamilyMemberId familyMemberId);
    
    /**
     * 删除体检记录
     */
    void deleteById(Long id);
    
    /**
     * 删除家庭成员的所有体检记录
     */
    void deleteByFamilyMemberId(FamilyMemberId familyMemberId);
    
    /**
     * 查询所有有下次体检日期的记录
     * 用于体检提醒任务
     */
    List<HealthCheckup> findAllWithNextCheckupDate();
}
