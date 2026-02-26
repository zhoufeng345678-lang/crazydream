package com.crazydream.application.family.executor;

import com.crazydream.application.family.convertor.FamilyMemberDTOConvertor;
import com.crazydream.application.family.dto.FamilyMemberDTO;
import com.crazydream.application.family.query.FamilyMemberListQry;
import com.crazydream.domain.family.model.aggregate.FamilyMember;
import com.crazydream.domain.family.repository.FamilyMemberRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询家庭成员列表执行器
 */
@Component
public class FamilyMemberListQryExe {
    
    @Autowired
    private FamilyMemberRepository familyMemberRepository;
    
    public ResponseResult<List<FamilyMemberDTO>> execute(FamilyMemberListQry qry, Long userId) {
        // 1. 查询用户的所有家庭成员
        List<FamilyMember> familyMembers = familyMemberRepository.findByUserId(UserId.of(userId));
        
        // 2. 转换为DTO
        List<FamilyMemberDTO> dtoList = familyMembers.stream()
            .map(FamilyMemberDTOConvertor::toDTO)
            .collect(Collectors.toList());
        
        return ResponseResult.success(dtoList);
    }
}
