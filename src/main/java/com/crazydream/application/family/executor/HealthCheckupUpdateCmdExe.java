package com.crazydream.application.family.executor;

import com.crazydream.application.family.command.HealthCheckupUpdateCmd;
import com.crazydream.application.family.convertor.HealthCheckupDTOConvertor;
import com.crazydream.application.family.dto.HealthCheckupDTO;
import com.crazydream.domain.family.model.entity.HealthCheckup;
import com.crazydream.domain.family.repository.HealthCheckupRepository;
import com.crazydream.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 更新健康体检记录命令执行器
 */
@Component
public class HealthCheckupUpdateCmdExe {
    
    @Autowired
    private HealthCheckupRepository healthCheckupRepository;
    
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<HealthCheckupDTO> execute(HealthCheckupUpdateCmd cmd) {
        // 1. 查询现有记录
        HealthCheckup healthCheckup = healthCheckupRepository.findById(cmd.getId())
            .orElseThrow(() -> new IllegalArgumentException("体检记录不存在"));
        
        // 2. 更新领域对象
        healthCheckup.update(
            cmd.getCheckupDate() != null ? cmd.getCheckupDate() : healthCheckup.getCheckupDate(),
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
