package com.crazydream.application.family.executor;

import com.crazydream.application.family.command.FamilyMemberUpdateCmd;
import com.crazydream.application.family.convertor.FamilyMemberDTOConvertor;
import com.crazydream.application.family.dto.FamilyMemberDTO;
import com.crazydream.domain.family.model.aggregate.FamilyMember;
import com.crazydream.domain.family.model.valueobject.FamilyMemberId;
import com.crazydream.domain.family.model.valueobject.RelationType;
import com.crazydream.domain.family.repository.FamilyMemberRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 更新家庭成员命令执行器
 */
@Component
public class FamilyMemberUpdateCmdExe {
    
    @Autowired
    private FamilyMemberRepository familyMemberRepository;
    
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<FamilyMemberDTO> execute(FamilyMemberUpdateCmd cmd, Long userId) {
        // 1. 查询现有成员
        FamilyMemberId familyMemberId = FamilyMemberId.of(cmd.getId());
        FamilyMember familyMember = familyMemberRepository.findById(familyMemberId)
            .orElseThrow(() -> new IllegalArgumentException("家庭成员不存在"));
        
        // 2. 权限验证：确保成员属于当前用户
        if (!familyMember.belongsTo(UserId.of(userId))) {
            throw new IllegalArgumentException("无权限操作该家庭成员");
        }
        
        // 3. 更新领域对象
        RelationType relationType = cmd.getRelationType() != null 
            ? RelationType.fromString(cmd.getRelationType()) 
            : familyMember.getRelationType();
            
        familyMember.update(
            cmd.getName(),
            relationType,
            cmd.getBirthday(),
            cmd.getPhone(),
            cmd.getAvatar(),
            cmd.getNotes()
        );
        
        // 4. 保存
        familyMember = familyMemberRepository.save(familyMember);
        
        // 5. 转换为DTO返回
        FamilyMemberDTO dto = FamilyMemberDTOConvertor.toDTO(familyMember);
        return ResponseResult.success(dto);
    }
}
