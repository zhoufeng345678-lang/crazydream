package com.crazydream.application.family.executor;

import com.crazydream.application.family.command.HealthCheckupCreateCmd;
import com.crazydream.application.family.convertor.HealthCheckupDTOConvertor;
import com.crazydream.application.family.dto.HealthCheckupDTO;
import com.crazydream.domain.family.model.entity.HealthCheckup;
import com.crazydream.domain.family.model.valueobject.FamilyMemberId;
import com.crazydream.domain.family.repository.HealthCheckupRepository;
import com.crazydream.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建健康体检记录命令执行器
 */
@Component
public class HealthCheckupCreateCmdExe {
    
    @Autowired
    private HealthCheckupRepository healthCheckupRepository;
    
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<HealthCheckupDTO> execute(HealthCheckupCreateCmd cmd) {
        // 1. 创建领域对象
        FamilyMemberId familyMemberId = FamilyMemberId.of(cmd.getFamilyMemberId());
        HealthCheckup healthCheckup = HealthCheckup.create(familyMemberId, cmd.getCheckupDate());
        
        // 2. 设置可选字段
        healthCheckup.update(
            cmd.getCheckupDate(),
            cmd.getNextCheckupDate(),
            cmd.getHospital(),
            cmd.getNotes()
        );
        
        // 3. 保存
        healthCheckup = healthCheckupRepository.save(healthCheckup);
        
        // 4. 转换为DTO返回
        HealthCheckupDTO dto = HealthCheckupDTOConvertor.toDTO(healthCheckup);
        return ResponseResult.success(dto);
    }
}
