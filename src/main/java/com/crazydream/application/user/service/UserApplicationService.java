package com.crazydream.application.user.service;

import com.crazydream.application.user.assembler.UserAssembler;
import com.crazydream.application.user.dto.*;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.user.model.aggregate.User;
import com.crazydream.domain.user.model.valueobject.*;
import com.crazydream.domain.user.repository.UserRepository;
import com.crazydream.infrastructure.wechat.WechatService;
import com.crazydream.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserApplicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserApplicationService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private WechatService wechatService;
    
    @Transactional
    public LoginResponse register(RegisterCommand command) {
        Email email = Email.of(command.getEmail());
        
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("邮箱已被注册");
        }
        
        String encodedPassword = passwordEncoder.encode(command.getPassword());
        User user = UserAssembler.toDomain(command, encodedPassword);
        user = userRepository.save(user);
        
        UserDTO userDTO = UserAssembler.toDTO(user);
        String token = jwtUtils.generateToken(user.getId().getValue(), user.getEmail().getValue());
        
        return new LoginResponse(token, userDTO);
    }
    
    public LoginResponse login(LoginCommand command) {
        Email email = Email.of(command.getEmail());
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("邮箱或密码错误"));
        
        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("邮箱或密码错误");
        }
        
        UserDTO userDTO = UserAssembler.toDTO(user);
        String token = jwtUtils.generateToken(user.getId().getValue(), user.getEmail().getValue());
        
        return new LoginResponse(token, userDTO);
    }
    
    /**
     * 微信一键登录
     * @param code 微信授权码
     * @return 登录响应（包含token和用户信息）
     */
    @Transactional
    public LoginResponse wechatLogin(String code) {
        logger.info("微信登录开始，code: {}", code);
        
        // 1. 调用微信API换取openid
        WechatOpenId wechatOpenId = wechatService.getOpenIdByCode(code);
        
        // 2. 根据openid查询用户
        User user = userRepository.findByWechatOpenId(wechatOpenId)
                .orElseGet(() -> {
                    // 3. 用户不存在，自动注册
                    logger.info("用户首次微信登录，自动创建账号，openid: {}", wechatOpenId.getValue());
                    try {
                        User newUser = User.createByWechat(wechatOpenId);
                        return userRepository.save(newUser);
                    } catch (DuplicateKeyException e) {
                        // 并发场景：另一个请求已创建该用户，重新查询
                        logger.warn("用户创建时发生唯一性冲突，重新查询，openid: {}", wechatOpenId.getValue());
                        return userRepository.findByWechatOpenId(wechatOpenId)
                                .orElseThrow(() -> new IllegalArgumentException("用户创建失败"));
                    }
                });
        
        // 4. 生成JWT token
        UserDTO userDTO = UserAssembler.toDTO(user);
        String token = jwtUtils.generateToken(user.getId().getValue(), user.getEmail().getValue());
        
        logger.info("微信登录成功，userId: {}", user.getId().getValue());
        return new LoginResponse(token, userDTO);
    }
    
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(UserId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + id));
        return UserAssembler.toDTO(user);
    }
    
    @Transactional
    public UserDTO updateProfile(Long id, UpdateProfileCommand command) {
        User user = userRepository.findById(UserId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + id));
        
        user.updateProfile(
            NickName.of(command.getNickName()),
            command.getAvatar(),
            Phone.ofNullable(command.getPhone()),
            WechatOpenId.ofNullable(command.getWechatOpenId()),
            Bio.of(command.getBio())
        );
        user = userRepository.save(user);
        
        return UserAssembler.toDTO(user);
    }
    
    @Transactional
    public void addPoints(Long id, int points) {
        User user = userRepository.findById(UserId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + id));
        user.addPoints(points);
        userRepository.save(user);
    }
    
    @Transactional
    public UserDTO upgradeLevel(Long id, int completedGoals) {
        User user = userRepository.findById(UserId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + id));
        user.upgradeLevel(completedGoals);
        user = userRepository.save(user);
        return UserAssembler.toDTO(user);
    }
}
