package com.crazydream.application.user.dto;

import lombok.Data;

@Data
public class RegisterCommand {
    private String email;
    private String password;
    private String nickName;
}
