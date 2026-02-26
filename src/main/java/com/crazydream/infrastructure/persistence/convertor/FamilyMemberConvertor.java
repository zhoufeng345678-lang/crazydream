package com.crazydream.infrastructure.persistence.convertor;

import com.crazydream.domain.family.model.aggregate.FamilyMember;
import com.crazydream.domain.family.model.valueobject.FamilyMemberId;
import com.crazydream.domain.family.model.valueobject.RelationType;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.po.FamilyMemberPO;

/**
 * 家庭成员转换器（PO ↔ Entity）
 */
public class FamilyMemberConvertor {
    
    /**
     * PO 转 Entity
     */
    public static FamilyMember toEntity(FamilyMemberPO po) {
        if (po == null) {
            return null;
        }
        
        return FamilyMember.rebuild(
            FamilyMemberId.of(po.getId()),
            UserId.of(po.getUserId()),
            po.getName(),
            RelationType.fromString(po.getRelationType()),
            po.getBirthday(),
            po.getPhone(),
            po.getAvatar(),
            po.getNotes(),
            po.getCreateTime(),
            po.getUpdateTime()
        );
    }
    
    /**
     * Entity 转 PO
     */
    public static FamilyMemberPO toPO(FamilyMember entity) {
        if (entity == null) {
            return null;
        }
        
        FamilyMemberPO po = new FamilyMemberPO();
        if (entity.getId() != null) {
            po.setId(entity.getId().getValue());
        }
        po.setUserId(entity.getUserId().getValue());
        po.setName(entity.getName());
        po.setRelationType(entity.getRelationType().name());
        po.setBirthday(entity.getBirthday());
        po.setPhone(entity.getPhone());
        po.setAvatar(entity.getAvatar());
        po.setNotes(entity.getNotes());
        po.setCreateTime(entity.getCreateTime());
        po.setUpdateTime(entity.getUpdateTime());
        
        return po;
    }
}
