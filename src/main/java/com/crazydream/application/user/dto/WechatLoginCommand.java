package com.crazydream.application.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信登录命令
 */
@Data
public class WechatLoginCommand {
    
    @NotBlank(message = "微信授权码不能为空")
    private String code;
}
