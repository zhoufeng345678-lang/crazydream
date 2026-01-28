package com.crazydream.interfaces.auth;

import com.crazydream.application.user.dto.LoginCommand;
import com.crazydream.application.user.dto.LoginResponse;
import com.crazydream.application.user.dto.RegisterCommand;
import com.crazydream.application.user.dto.WechatLoginCommand;
import com.crazydream.application.user.service.UserApplicationService;
import com.crazydream.utils.ResponseResult;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器 - v2版本
 * 处理用户登录和注册
 */
@RestController
@RequestMapping("/api/v2/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private UserApplicationService userApplicationService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseResult<LoginResponse> register(@RequestBody RegisterCommand command) {
        try {
            logger.info("用户注册请求: {}", command.getEmail());
            LoginResponse response = userApplicationService.register(command);
            return ResponseResult.success(response);
        } catch (IllegalArgumentException e) {
            logger.warn("注册失败: {}", e.getMessage());
            return ResponseResult.error(400, e.getMessage());
        } catch (Exception e) {
            logger.error("注册异常", e);
            return ResponseResult.error(500, "注册失败，请稍后重试");
        }
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseResult<LoginResponse> login(@RequestBody LoginCommand command) {
        try {
            logger.info("用户登录请求: {}", command.getEmail());
            LoginResponse response = userApplicationService.login(command);
            return ResponseResult.success(response);
        } catch (IllegalArgumentException e) {
            logger.warn("登录失败: {}", e.getMessage());
            return ResponseResult.error(401, e.getMessage());
        } catch (Exception e) {
            logger.error("登录异常", e);
            return ResponseResult.error(500, "登录失败，请稍后重试");
        }
    }
    
    /**
     * 微信一键登录
     */
    @PostMapping("/wechat-login")
    public ResponseResult<LoginResponse> wechatLogin(@Valid @RequestBody WechatLoginCommand command) {
        try {
            logger.info("微信登录请求，code: {}", command.getCode());
            LoginResponse response = userApplicationService.wechatLogin(command.getCode());
            return ResponseResult.success(response);
        } catch (IllegalArgumentException e) {
            logger.warn("微信登录失败: {}", e.getMessage());
            // 根据错误消息判断返回401还是500
            if (e.getMessage().contains("授权失败") || e.getMessage().contains("授权码")) {
                return ResponseResult.error(401, e.getMessage());
            }
            return ResponseResult.error(500, e.getMessage());
        } catch (Exception e) {
            logger.error("微信登录异常", e);
            return ResponseResult.error(500, "微信登录失败，请稍后重试");
        }
    }
}
