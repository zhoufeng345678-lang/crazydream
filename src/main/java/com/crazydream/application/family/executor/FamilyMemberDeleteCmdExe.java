package com.crazydream.application.family.executor;

import com.crazydream.application.family.command.FamilyMemberDeleteCmd;
import com.crazydream.domain.family.model.aggregate.FamilyMember;
import com.crazydream.domain.family.model.valueobject.FamilyMemberId;
import com.crazydream.domain.family.repository.FamilyMemberRepository;
import com.crazydream.domain.family.repository.HealthCheckupRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 删除家庭成员命令执行器
 */
@Component
public class FamilyMemberDeleteCmdExe {
    
    @Autowired
    private FamilyMemberRepository familyMemberRepository;
    
    @Autowired
    private HealthCheckupRepository healthCheckupRepository;
    
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Void> execute(FamilyMemberDeleteCmd cmd, Long userId) {
        // 1. 查询现有成员
        FamilyMemberId familyMemberId = FamilyMemberId.of(cmd.getId());
        FamilyMember familyMember = familyMemberRepository.findById(familyMemberId)
            .orElseThrow(() -> new IllegalArgumentException("家庭成员不存在"));
        
        // 2. 权限验证：确保成员属于当前用户
        if (!familyMember.belongsTo(UserId.of(userId))) {
            throw new IllegalArgumentException("无权限操作该家庭成员");
        }
        
        // 3. 删除关联的体检记录
        healthCheckupRepository.deleteByFamilyMemberId(familyMemberId);
        
        // 4. 删除成员
        familyMemberRepository.deleteById(familyMemberId);
        
        return ResponseResult.success();
    }
}
