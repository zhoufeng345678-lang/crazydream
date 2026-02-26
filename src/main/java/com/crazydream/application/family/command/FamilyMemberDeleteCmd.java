package com.crazydream.application.family.command;

import jakarta.validation.constraints.NotNull;

/**
 * 删除家庭成员命令
 */
public class FamilyMemberDeleteCmd {
    
    @NotNull(message = "成员ID不能为空")
    private Long id;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
}
