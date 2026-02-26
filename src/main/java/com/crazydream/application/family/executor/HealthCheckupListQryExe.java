package com.crazydream.application.family.executor;

import com.crazydream.application.family.convertor.HealthCheckupDTOConvertor;
import com.crazydream.application.family.dto.HealthCheckupDTO;
import com.crazydream.application.family.query.HealthCheckupListQry;
import com.crazydream.domain.family.model.entity.HealthCheckup;
import com.crazydream.domain.family.model.valueobject.FamilyMemberId;
import com.crazydream.domain.family.repository.HealthCheckupRepository;
import com.crazydream.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询健康体检记录列表执行器
 */
@Component
public class HealthCheckupListQryExe {
    
    @Autowired
    private HealthCheckupRepository healthCheckupRepository;
    
    public ResponseResult<List<HealthCheckupDTO>> execute(HealthCheckupListQry qry) {
        // 1. 查询体检记录列表
        FamilyMemberId familyMemberId = FamilyMemberId.of(qry.getFamilyMemberId());
        List<HealthCheckup> healthCheckups = healthCheckupRepository.findByFamilyMemberId(familyMemberId);
        
        // 2. 转换为DTO
        List<HealthCheckupDTO> dtoList = healthCheckups.stream()
            .map(HealthCheckupDTOConvertor::toDTO)
            .collect(Collectors.toList());
        
        return ResponseResult.success(dtoList);
    }
}
