package com.crazydream.application.family.convertor;

import com.crazydream.application.family.dto.HealthCheckupDTO;
import com.crazydream.domain.family.model.entity.HealthCheckup;

/**
 * 健康体检应用层转换器
 */
public class HealthCheckupDTOConvertor {
    
    /**
     * Entity 转 DTO
     */
    public static HealthCheckupDTO toDTO(HealthCheckup entity) {
        if (entity == null) {
            return null;
        }
        
        HealthCheckupDTO dto = new HealthCheckupDTO();
        dto.setId(entity.getId());
        dto.setFamilyMemberId(entity.getFamilyMemberId().getValue());
        dto.setCheckupDate(entity.getCheckupDate());
        dto.setNextCheckupDate(entity.getNextCheckupDate());
        dto.setHospital(entity.getHospital());
        dto.setNotes(entity.getNotes());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        
        return dto;
    }
}
