package com.crazydream.domain.family.repository;

import com.crazydream.domain.family.model.aggregate.FamilyMember;
import com.crazydream.domain.family.model.valueobject.FamilyMemberId;
import com.crazydream.domain.shared.model.UserId;

import java.util.List;
import java.util.Optional;

/**
 * 家庭成员仓储接口（防腐层）
 */
public interface FamilyMemberRepository {
    
    /**
     * 保存家庭成员
     */
    FamilyMember save(FamilyMember familyMember);
    
    /**
     * 根据ID查询家庭成员
     */
    Optional<FamilyMember> findById(FamilyMemberId id);
    
    /**
     * 查询用户的所有家庭成员
     */
    List<FamilyMember> findByUserId(UserId userId);
    
    /**
     * 删除家庭成员
     */
    void deleteById(FamilyMemberId id);
    
    /**
     * 检查家庭成员是否存在
     */
    boolean existsById(FamilyMemberId id);
    
    /**
     * 查询所有有生日的家庭成员
     * 用于生日提醒任务
     */
    List<FamilyMember> findAllWithBirthday();
}
