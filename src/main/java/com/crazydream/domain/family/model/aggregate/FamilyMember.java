package com.crazydream.domain.family.model.aggregate;

import com.crazydream.domain.family.model.valueobject.FamilyMemberId;
import com.crazydream.domain.family.model.valueobject.RelationType;
import com.crazydream.domain.shared.model.UserId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 家庭成员聚合根（充血模型）
 */
public class FamilyMember {
    private FamilyMemberId id;
    private UserId userId;  // 所属用户ID
    private String name;  // 成员姓名
    private RelationType relationType;  // 关系类型
    private LocalDate birthday;  // 生日
    private String phone;  // 手机号
    private String avatar;  // 头像URL
    private String notes;  // 备注
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private FamilyMember() {}
    
    /**
     * 创建家庭成员
     */
    public static FamilyMember create(UserId userId, String name, RelationType relationType) {
        FamilyMember member = new FamilyMember();
        member.userId = userId;
        member.name = name;
        member.relationType = relationType;
        member.createTime = LocalDateTime.now();
        member.updateTime = LocalDateTime.now();
        
        // 业务规则校验
        member.validate();
        
        return member;
    }
    
    /**
     * 重建家庭成员（从数据库加载）
     */
    public static FamilyMember rebuild(FamilyMemberId id, UserId userId, String name,
                                        RelationType relationType, LocalDate birthday,
                                        String phone, String avatar, String notes,
                                        LocalDateTime createTime, LocalDateTime updateTime) {
        FamilyMember member = new FamilyMember();
        member.id = id;
        member.userId = userId;
        member.name = name;
        member.relationType = relationType;
        member.birthday = birthday;
        member.phone = phone;
        member.avatar = avatar;
        member.notes = notes;
        member.createTime = createTime;
        member.updateTime = updateTime;
        return member;
    }
    
    // ==================== 业务行为 ====================
    
    /**
     * 更新成员信息
     */
    public void update(String name, RelationType relationType, LocalDate birthday,
                       String phone, String avatar, String notes) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (relationType != null) {
            this.relationType = relationType;
        }
        this.birthday = birthday;
        this.phone = phone;
        this.avatar = avatar;
        this.notes = notes;
        this.updateTime = LocalDateTime.now();
        
        // 业务规则校验
        validate();
    }
    
    /**
     * 业务规则校验
     */
    private void validate() {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("成员姓名不能为空");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("成员姓名不能超过50个字符");
        }
        if (relationType == null) {
            throw new IllegalArgumentException("关系类型不能为空");
        }
        if (phone != null && !phone.matches("^1[3-9]\\d{9}$")) {
            // 手机号格式校验（如果提供）
            // 这里简单校验，实际可以更严格
        }
    }
    
    /**
     * 检查是否有生日
     */
    public boolean hasBirthday() {
        return birthday != null;
    }
    
    /**
     * 检查是否属于指定用户
     */
    public boolean belongsTo(UserId userId) {
        return this.userId.equals(userId);
    }
    
    // ==================== Getters ====================
    
    public FamilyMemberId getId() {
        return id;
    }
    
    public void setId(FamilyMemberId id) {
        this.id = id;
    }
    
    public UserId getUserId() {
        return userId;
    }
    
    public String getName() {
        return name;
    }
    
    public RelationType getRelationType() {
        return relationType;
    }
    
    public LocalDate getBirthday() {
        return birthday;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyMember that = (FamilyMember) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
