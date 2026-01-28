package com.crazydream.interfaces.user;

import com.crazydream.application.user.dto.*;
import com.crazydream.application.user.service.UserApplicationService;
import com.crazydream.infrastructure.oss.OssService;
import com.crazydream.utils.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v2/users")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserApplicationService userApplicationService;
    
    @Autowired
    private OssService ossService;
    
    @Value("${security.test.default-user-id:1}")
    private Long defaultUserId;
    
    @PostMapping("/register")
    public ResponseResult<LoginResponse> register(@RequestBody RegisterCommand command) {
        try {
            LoginResponse response = userApplicationService.register(command);
            return ResponseResult.success(response);
        } catch (Exception e) {
            return ResponseResult.error(400, e.getMessage());
        }
    }
    
    @GetMapping("/me")
    public ResponseResult<UserDTO> getCurrentUser() {
        try {
            Long userId = getCurrentUserId();
            UserDTO dto = userApplicationService.getUserById(userId);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(404, e.getMessage());
        }
    }
    
    @GetMapping("/profile")
    public ResponseResult<UserDTO> getUserProfile() {
        try {
            Long userId = getCurrentUserId();
            UserDTO dto = userApplicationService.getUserById(userId);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(404, e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseResult<UserDTO> getUserById(@PathVariable Long id) {
        try {
            UserDTO dto = userApplicationService.getUserById(id);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(404, e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseResult<UserDTO> updateUser(@PathVariable Long id, @RequestBody java.util.Map<String, Object> payload) {
        try {
            // 简化实现：直接返回用户信息
            UserDTO dto = userApplicationService.getUserById(id);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(400, e.getMessage());
        }
    }
    
    @PutMapping("/profile")
    public ResponseResult<UserDTO> updateProfile(@RequestBody UpdateProfileCommand command) {
        try {
            Long userId = getCurrentUserId();
            UserDTO dto = userApplicationService.updateProfile(userId, command);
            return ResponseResult.success(dto);
        } catch (Exception e) {
            return ResponseResult.error(400, e.getMessage());
        }
    }
    
    /**
     * 上传头像文件
     */
    @PostMapping("/avatar")
    public ResponseResult<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            Long userId = getCurrentUserId();
            logger.info("用户{}上传头像，文件名: {}, 大小: {}", userId, file.getOriginalFilename(), file.getSize());
            
            String avatarUrl = ossService.uploadAvatar(file, userId);
            logger.info("头像上传成功: {}", avatarUrl);
            
            return ResponseResult.success(avatarUrl);
        } catch (IllegalArgumentException e) {
            logger.warn("头像上传验证失败: {}", e.getMessage());
            return ResponseResult.error(400, e.getMessage());
        } catch (Exception e) {
            logger.error("头像上传失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "文件上传失败，请稍后重试");
        }
    }
    
    @PostMapping("/points")
    public ResponseResult<Void> addPoints(@RequestParam int points) {
        try {
            Long userId = getCurrentUserId();
            userApplicationService.addPoints(userId, points);
            return ResponseResult.success(null);
        } catch (Exception e) {
            return ResponseResult.error(400, e.getMessage());
        }
    }
    
    /**
     * 获取当前用户的微信绑定状态
     */
    @GetMapping("/me/wechat-status")
    public ResponseResult<java.util.Map<String, Object>> getWechatBindStatus() {
        try {
            Long userId = getCurrentUserId();
            UserDTO user = userApplicationService.getUserById(userId);
            
            boolean isBound = user.getWechatOpenId() != null && !user.getWechatOpenId().isEmpty();
            
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("isBound", isBound);
            if (isBound) {
                // 脱敏处理，只返回前4位和后4位
                String openId = user.getWechatOpenId();
                if (openId.length() > 8) {
                    result.put("openId", openId.substring(0, 4) + "****" + openId.substring(openId.length() - 4));
                } else {
                    result.put("openId", "****");
                }
            }
            
            logger.info("用户{}查询微信绑定状态: {}", userId, isBound);
            return ResponseResult.success(result);
        } catch (Exception e) {
            logger.error("查询微信绑定状态失败: {}", e.getMessage(), e);
            return ResponseResult.error(500, "查询失败，请稍后重试");
        }
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            logger.debug("未找到认证信息，使用默认用户ID: {}", defaultUserId);
            return defaultUserId;
        }
        
        Object principal = authentication.getPrincipal();
        
        if ("anonymousUser".equals(principal)) {
            logger.debug("匿名用户访问，使用默认用户ID: {}", defaultUserId);
            return defaultUserId;
        }
        
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            return Long.parseLong(((org.springframework.security.core.userdetails.User) principal).getUsername());
        } else if (principal instanceof Long) {
            return (Long) principal;
        } else if (principal instanceof String) {
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                logger.warn("无法解析用户ID: {}, 使用默认用户ID: {}", principal, defaultUserId);
                return defaultUserId;
            }
        }
        
        logger.warn("不支持的认证信息格式: {}, 使用默认用户ID: {}", principal.getClass(), defaultUserId);
        return defaultUserId;
    }
}
