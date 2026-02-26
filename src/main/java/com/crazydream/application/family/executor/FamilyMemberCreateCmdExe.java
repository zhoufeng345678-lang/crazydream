package com.crazydream.application.family.executor;

import com.crazydream.application.family.command.FamilyMemberCreateCmd;
import com.crazydream.application.family.convertor.FamilyMemberDTOConvertor;
import com.crazydream.application.family.dto.FamilyMemberDTO;
import com.crazydream.domain.family.model.aggregate.FamilyMember;
import com.crazydream.domain.family.model.valueobject.RelationType;
import com.crazydream.domain.family.repository.FamilyMemberRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建家庭成员命令执行器
 */
@Component
public class FamilyMemberCreateCmdExe {
    
    @Autowired
    private FamilyMemberRepository familyMemberRepository;
    
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<FamilyMemberDTO> execute(FamilyMemberCreateCmd cmd, Long userId) {
        // 1. 参数转换
        RelationType relationType = RelationType.fromString(cmd.getRelationType());
        
        // 2. 创建领域对象
        FamilyMember familyMember = FamilyMember.create(
            UserId.of(userId),
            cmd.getName(),
            relationType
        );
        
        // 3. 设置可选字段
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
