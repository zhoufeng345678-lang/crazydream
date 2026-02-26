package com.crazydream.domain.family.model.entity;

import com.crazydream.domain.family.model.valueobject.FamilyMemberId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 健康体检实体
 */
public class HealthCheckup {
    private Long id;
    private FamilyMemberId familyMemberId;  // 所属家庭成员ID
    private LocalDate checkupDate;  // 体检日期
    private LocalDate nextCheckupDate;  // 下次体检日期
    private String hospital;  // 体检医院
    private String notes;  // 备注
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private HealthCheckup() {}
    
    /**
     * 创建体检记录
     */
    public static HealthCheckup create(FamilyMemberId familyMemberId, LocalDate checkupDate) {
        HealthCheckup checkup = new HealthCheckup();
        checkup.familyMemberId = familyMemberId;
        checkup.checkupDate = checkupDate;
        checkup.createTime = LocalDateTime.now();
        checkup.updateTime = LocalDateTime.now();
        
        // 业务规则校验
        checkup.validate();
        
        return checkup;
    }
    
    /**
     * 重建体检记录（从数据库加载）
     */
    public static HealthCheckup rebuild(Long id, FamilyMemberId familyMemberId,
                                         LocalDate checkupDate, LocalDate nextCheckupDate,
                                         String hospital, String notes,
                                         LocalDateTime createTime, LocalDateTime updateTime) {
        HealthCheckup checkup = new HealthCheckup();
        checkup.id = id;
        checkup.familyMemberId = familyMemberId;
        checkup.checkupDate = checkupDate;
        checkup.nextCheckupDate = nextCheckupDate;
        checkup.hospital = hospital;
        checkup.notes = notes;
        checkup.createTime = createTime;
        checkup.updateTime = updateTime;
        return checkup;
    }
    
    // ==================== 业务行为 ====================
    
    /**
     * 设置下次体检日期
     */
    public void setNextCheckupDate(LocalDate nextCheckupDate) {
        this.nextCheckupDate = nextCheckupDate;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 更新体检信息
     */
    public void update(LocalDate checkupDate, LocalDate nextCheckupDate, String hospital, String notes) {
        if (checkupDate != null) {
            this.checkupDate = checkupDate;
        }
        this.nextCheckupDate = nextCheckupDate;
        this.hospital = hospital;
        this.notes = notes;
        this.updateTime = LocalDateTime.now();
        
        validate();
    }
    
    /**
     * 业务规则校验
     */
    private void validate() {
        if (familyMemberId == null) {
            throw new IllegalArgumentException("家庭成员ID不能为空");
        }
        if (checkupDate == null) {
            throw new IllegalArgumentException("体检日期不能为空");
        }
        if (checkupDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("体检日期不能是未来日期");
        }
        if (nextCheckupDate != null && nextCheckupDate.isBefore(checkupDate)) {
            throw new IllegalArgumentException("下次体检日期不能早于本次体检日期");
        }
    }
    
    /**
     * 检查是否有下次体检日期
     */
    public boolean hasNextCheckupDate() {
        return nextCheckupDate != null;
    }
    
    // ==================== Getters ====================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public FamilyMemberId getFamilyMemberId() {
        return familyMemberId;
    }
    
    public LocalDate getCheckupDate() {
        return checkupDate;
    }
    
    public LocalDate getNextCheckupDate() {
        return nextCheckupDate;
    }
    
    public String getHospital() {
        return hospital;
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
        HealthCheckup that = (HealthCheckup) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
