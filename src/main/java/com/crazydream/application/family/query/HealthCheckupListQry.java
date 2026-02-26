package com.crazydream.application.family.query;

import jakarta.validation.constraints.NotNull;

/**
 * 查询健康体检记录列表
 */
public class HealthCheckupListQry {
    
    @NotNull(message = "家庭成员ID不能为空")
    private Long familyMemberId;
    
    public Long getFamilyMemberId() {
        return familyMemberId;
    }
    
    public void setFamilyMemberId(Long familyMemberId) {
        this.familyMemberId = familyMemberId;
    }
}
