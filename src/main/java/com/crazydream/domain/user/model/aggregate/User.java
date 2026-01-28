package com.crazydream.domain.user.model.aggregate;

import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.user.model.valueobject.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 用户聚合根（充血模型）
 */
public class User {
    private UserId id;
    private NickName nickName;
    private Email email;
    private String password;  // 加密后的密码
    private String avatar;
    private Phone phone;  // 手机号
    private WechatOpenId wechatOpenId;  // 微信OpenID
    private Bio bio;  // 个人简介
    private UserLevel level;
    private Integer points;  // 积分
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private User() {}
    
    /**
     * 创建用户
     */
    public static User create(Email email, String encodedPassword, NickName nickName) {
        User user = new User();
        user.email = email;
        user.password = encodedPassword;
        user.nickName = nickName;
        user.level = UserLevel.BEGINNER;
        user.points = 0;
        user.createTime = LocalDateTime.now();
        user.updateTime = LocalDateTime.now();
        return user;
    }
    
    /**
     * 通过微信OpenID创建用户（自动注册）
     */
    public static User createByWechat(WechatOpenId wechatOpenId) {
        User user = new User();
        // 生成唯一邮箱：wechat_{openid}@crazydream.com
        String uniqueEmail = "wechat_" + wechatOpenId.getValue() + "@crazydream.com";
        user.email = Email.of(uniqueEmail);
        // 微信登录用户无密码
        user.password = null;
        // 默认昵称
        user.nickName = NickName.of("微信用户");
        user.wechatOpenId = wechatOpenId;
        user.level = UserLevel.BEGINNER;
        user.points = 0;
        user.createTime = LocalDateTime.now();
        user.updateTime = LocalDateTime.now();
        return user;
    }
    
    /**
     * 重建用户（从数据库加载）
     */
    public static User rebuild(UserId id, NickName nickName, Email email, String password,
                               String avatar, Phone phone, WechatOpenId wechatOpenId, Bio bio,
                               UserLevel level, Integer points,
                               LocalDateTime createTime, LocalDateTime updateTime) {
        User user = new User();
        user.id = id;
        user.nickName = nickName;
        user.email = email;
        user.password = password;
        user.avatar = avatar;
        user.phone = phone;
        user.wechatOpenId = wechatOpenId;
        user.bio = bio;
        user.level = level;
        user.points = points;
        user.createTime = createTime;
        user.updateTime = updateTime;
        return user;
    }
    
    // ==================== 业务行为 ====================
    
    public void updateProfile(NickName nickName, String avatar, Phone phone, WechatOpenId wechatOpenId, Bio bio) {
        this.nickName = nickName;
        if (avatar != null && !avatar.isEmpty()) {
            this.avatar = avatar;
        }
        this.phone = phone;
        this.wechatOpenId = wechatOpenId;
        this.bio = bio;
        this.updateTime = LocalDateTime.now();
    }
    
    public void changePassword(String newEncodedPassword) {
        this.password = newEncodedPassword;
        this.updateTime = LocalDateTime.now();
    }
    
    public void addPoints(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("积分增加量必须大于0");
        }
        this.points += amount;
        this.updateTime = LocalDateTime.now();
    }
    
    public void upgradeLevel(int completedGoals) {
        if (level.canUpgrade(completedGoals)) {
            this.level = level.nextLevel();
            this.updateTime = LocalDateTime.now();
        }
    }
    
    public boolean isPasswordMatch(String encodedPassword) {
        return this.password.equals(encodedPassword);
    }
    
    // ==================== Getters ====================
    
    public UserId getId() { return id; }
    public NickName getNickName() { return nickName; }
    public Email getEmail() { return email; }
    public String getPassword() { return password; }
    public String getAvatar() { return avatar; }
    public Phone getPhone() { return phone; }
    public WechatOpenId getWechatOpenId() { return wechatOpenId; }
    public Bio getBio() { return bio; }
    public UserLevel getLevel() { return level; }
    public Integer getPoints() { return points; }
    public LocalDateTime getCreateTime() { return createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    
    public void setId(UserId id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
