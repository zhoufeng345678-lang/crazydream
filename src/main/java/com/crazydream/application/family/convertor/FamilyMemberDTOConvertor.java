package com.crazydream.application.family.convertor;

import com.crazydream.application.family.dto.FamilyMemberDTO;
import com.crazydream.domain.family.model.aggregate.FamilyMember;

/**
 * 家庭成员应用层转换器
 */
public class FamilyMemberDTOConvertor {
    
    /**
     * Entity 转 DTO
     */
    public static FamilyMemberDTO toDTO(FamilyMember entity) {
        if (entity == null) {
            return null;
        }
        
        FamilyMemberDTO dto = new FamilyMemberDTO();
        if (entity.getId() != null) {
            dto.setId(entity.getId().getValue());
        }
        dto.setName(entity.getName());
        dto.setRelationType(entity.getRelationType().name());
        dto.setRelationTypeDesc(entity.getRelationType().getDescription());
        dto.setBirthday(entity.getBirthday());
        dto.setPhone(entity.getPhone());
        dto.setAvatar(entity.getAvatar());
        dto.setNotes(entity.getNotes());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        
        return dto;
    }
}
