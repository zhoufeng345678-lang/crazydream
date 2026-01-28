package com.crazydream.application.user.dto;

import lombok.Data;

@Data
public class UpdateProfileCommand {
    private String nickName;
    private String avatar;
    private String phone;
    private String wechatOpenId;
    private String bio;
}
