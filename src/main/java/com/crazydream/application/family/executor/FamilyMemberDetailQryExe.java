package com.crazydream.application.family.executor;

import com.crazydream.application.family.convertor.FamilyMemberDTOConvertor;
import com.crazydream.application.family.dto.FamilyMemberDTO;
import com.crazydream.application.family.query.FamilyMemberDetailQry;
import com.crazydream.domain.family.model.aggregate.FamilyMember;
import com.crazydream.domain.family.model.valueobject.FamilyMemberId;
import com.crazydream.domain.family.repository.FamilyMemberRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 查询家庭成员详情执行器
 */
@Component
public class FamilyMemberDetailQryExe {
    
    @Autowired
    private FamilyMemberRepository familyMemberRepository;
    
    public ResponseResult<FamilyMemberDTO> execute(FamilyMemberDetailQry qry, Long userId) {
        // 1. 查询家庭成员
        FamilyMemberId familyMemberId = FamilyMemberId.of(qry.getId());
        FamilyMember familyMember = familyMemberRepository.findById(familyMemberId)
            .orElseThrow(() -> new IllegalArgumentException("家庭成员不存在"));
        
        // 2. 权限验证：确保成员属于当前用户
        if (!familyMember.belongsTo(UserId.of(userId))) {
            throw new IllegalArgumentException("无权限访问该家庭成员");
        }
        
        // 3. 转换为DTO返回
        FamilyMemberDTO dto = FamilyMemberDTOConvertor.toDTO(familyMember);
        return ResponseResult.success(dto);
    }
}
