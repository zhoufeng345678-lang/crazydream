package com.crazydream.security;

import com.crazydream.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 用于拦截请求并验证JWT令牌
 * 
 * @author CrazyDream Team
 * @since 2025-12-10
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    // 是否禁用安全认证（仅用于测试环境）
    @Value("${security.auth.disabled:false}")
    private boolean authDisabled;
    
    // 测试环境默认用户ID
    @Value("${security.test.default-user-id:1}")
    private Long defaultTestUserId;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        // 对于登录和注册接口，不需要JWT验证
        String requestURI = request.getRequestURI();
        logger.info("处理请求: {}", requestURI);
        
        if (requestURI.equals("/api/auth/register") || requestURI.equals("/api/auth/login") || 
            requestURI.equals("/auth/register") || requestURI.equals("/auth/login") ||
            requestURI.equals("/api/v2/auth/register") || requestURI.equals("/api/v2/auth/login") ||
            requestURI.equals("/api/v2/auth/wechat-login")) {
            logger.info("登录/注册接口，跳过JWT验证");
            // 直接通过过滤器链
            filterChain.doFilter(request, response);
            return;
        }
        
        // 获取Authorization头
        String authorizationHeader = request.getHeader(jwtUtils.getHeader());
        logger.debug("Authorization头: {}", authorizationHeader);
        
        // 提取JWT令牌
        String token = jwtUtils.extractTokenFromHeader(authorizationHeader);
        
        // 验证令牌
        if (token != null) {
            logger.info("发现JWT令牌，开始验证");
            if (jwtUtils.validateToken(token)) {
                try {
                    // 从令牌中获取用户ID
                    Long userId = jwtUtils.getUserIdFromToken(token);
                    logger.info("JWT令牌验证成功，用户ID: {}", userId);
                    
                    // 使用UserDetailsService加载用户信息
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());
                    
                    // 创建认证令牌
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 设置认证信息到Spring Security上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("用户认证成功，设置认证信息到上下文");
                } catch (Exception e) {
                    logger.error("JWT令牌处理失败: {}", e.getMessage(), e);
                    // 认证失败，清除上下文
                    SecurityContextHolder.clearContext();
                }
            } else {
                logger.warn("JWT令牌验证失败");
            }
        } else {
            logger.info("未发现JWT令牌");
            
            // 如果是测试模式且没有token，使用默认测试用户
            if (authDisabled) {
                try {
                    logger.info("测试模式：使用默认测试用户ID: {}", defaultTestUserId);
                    
                    // 使用UserDetailsService加载用户信息
                    UserDetails userDetails = userDetailsService.loadUserByUsername(defaultTestUserId.toString());
                    
                    // 创建认证令牌，使用userId作为principal
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            defaultTestUserId, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 设置认证信息到Spring Security上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("测试模式：默认用户认证成功");
                } catch (Exception e) {
                    logger.error("测试模式：设置默认用户失败: {}", e.getMessage(), e);
                }
            }
        }
        
        // 继续处理请求
        filterChain.doFilter(request, response);
    }
}
