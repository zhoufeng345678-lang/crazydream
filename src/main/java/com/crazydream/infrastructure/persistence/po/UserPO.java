package com.crazydream.infrastructure.persistence.po;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserPO {
    private Long id;
    private String nickName;
    private String email;
    private String password;
    private String avatar;
    private String phone;
    private String openid;
    private String bio;
    private Integer level;
    private Integer points;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
