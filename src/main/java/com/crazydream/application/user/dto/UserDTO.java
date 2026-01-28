package com.crazydream.application.user.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String nickName;
    private String email;
    private String avatar;
    private String phone;
    private String wechatOpenId;
    private String bio;
    private Integer level;
    private String levelDescription;
    private Integer points;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
