package com.crazydream.infrastructure.persistence.convertor;

import com.crazydream.domain.family.model.entity.HealthCheckup;
import com.crazydream.domain.family.model.valueobject.FamilyMemberId;
import com.crazydream.infrastructure.persistence.po.HealthCheckupPO;

/**
 * 健康体检转换器（PO ↔ Entity）
 */
public class HealthCheckupConvertor {
    
    /**
     * PO 转 Entity
     */
    public static HealthCheckup toEntity(HealthCheckupPO po) {
        if (po == null) {
            return null;
        }
        
        return HealthCheckup.rebuild(
            po.getId(),
            FamilyMemberId.of(po.getFamilyMemberId()),
            po.getCheckupDate(),
            po.getNextCheckupDate(),
            po.getHospital(),
            po.getNotes(),
            po.getCreateTime(),
            po.getUpdateTime()
        );
    }
    
    /**
     * Entity 转 PO
     */
    public static HealthCheckupPO toPO(HealthCheckup entity) {
        if (entity == null) {
            return null;
        }
        
        HealthCheckupPO po = new HealthCheckupPO();
        po.setId(entity.getId());
        po.setFamilyMemberId(entity.getFamilyMemberId().getValue());
        po.setCheckupDate(entity.getCheckupDate());
        po.setNextCheckupDate(entity.getNextCheckupDate());
        po.setHospital(entity.getHospital());
        po.setNotes(entity.getNotes());
        po.setCreateTime(entity.getCreateTime());
        po.setUpdateTime(entity.getUpdateTime());
        
        return po;
    }
}
