package com.crazydream.application.family.query;

import jakarta.validation.constraints.NotNull;

/**
 * 查询家庭成员详情
 */
public class FamilyMemberDetailQry {
    
    @NotNull(message = "成员ID不能为空")
    private Long id;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
}
