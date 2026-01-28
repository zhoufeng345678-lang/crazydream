package com.crazydream.security;

import com.crazydream.domain.user.model.aggregate.User;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * 自定义UserDetailsService实现
 * 用于从数据库中加载用户信息
 * 
 * @author CrazyDream Team
 * @since 2025-12-10
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("加载用户信息，username: {}", username);
        // 这里的username实际上是用户ID
        try {
            Long userId = Long.parseLong(username);
            logger.debug("解析到的用户ID: {}", userId);
            
            Optional<User> userOpt = userRepository.findById(UserId.of(userId));
            
            if (userOpt.isEmpty()) {
                logger.warn("未查询到用户信息，用户ID: {}", userId);
                throw new UsernameNotFoundException("用户不存在");
            }
            
            User user = userOpt.get();
            logger.debug("查询到用户信息: {}", user);
            
            // 创建并返回UserDetails对象
            logger.info("成功加载用户信息，用户ID: {}", userId);
            return new org.springframework.security.core.userdetails.User(
                    user.getId().getValue().toString(),
                    "", // 密码可以为空，因为我们使用JWT认证
                    Collections.emptyList() // 暂时不处理权限
            );
        } catch (NumberFormatException e) {
            logger.error("无效的用户ID，username: {}, 错误信息: {}", username, e.getMessage());
            throw new UsernameNotFoundException("无效的用户ID");
        }
    }
}
