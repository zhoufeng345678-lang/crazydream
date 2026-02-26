package com.crazydream.interfaces.family;

import com.crazydream.application.family.command.HealthCheckupCreateCmd;
import com.crazydream.application.family.command.HealthCheckupUpdateCmd;
import com.crazydream.application.family.dto.HealthCheckupDTO;
import com.crazydream.application.family.executor.HealthCheckupCreateCmdExe;
import com.crazydream.application.family.executor.HealthCheckupListQryExe;
import com.crazydream.application.family.executor.HealthCheckupUpdateCmdExe;
import com.crazydream.application.family.query.HealthCheckupListQry;
import com.crazydream.utils.ResponseResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 健康体检管理接口
 */
@RestController
@RequestMapping("/api/v2/family/health-checkups")
public class HealthCheckupController {
    
    @Autowired
    private HealthCheckupCreateCmdExe healthCheckupCreateCmdExe;
    
    @Autowired
    private HealthCheckupUpdateCmdExe healthCheckupUpdateCmdExe;
    
    @Autowired
    private HealthCheckupListQryExe healthCheckupListQryExe;
    
    /**
     * 创建健康体检记录
     */
    @PostMapping
    public ResponseResult<HealthCheckupDTO> create(@RequestBody @Valid HealthCheckupCreateCmd cmd) {
        return healthCheckupCreateCmdExe.execute(cmd);
    }
    
    /**
     * 更新健康体检记录
     */
    @PutMapping("/{id}")
    public ResponseResult<HealthCheckupDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid HealthCheckupUpdateCmd cmd) {
        cmd.setId(id);
        return healthCheckupUpdateCmdExe.execute(cmd);
    }
    
    /**
     * 查询家庭成员的体检记录列表
     */
    @GetMapping
    public ResponseResult<List<HealthCheckupDTO>> list(@RequestParam Long familyMemberId) {
        HealthCheckupListQry qry = new HealthCheckupListQry();
        qry.setFamilyMemberId(familyMemberId);
        return healthCheckupListQryExe.execute(qry);
    }
}
