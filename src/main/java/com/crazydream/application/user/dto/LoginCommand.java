package com.crazydream.application.user.dto;

import lombok.Data;

@Data
public class LoginCommand {
    private String email;
    private String password;
}
