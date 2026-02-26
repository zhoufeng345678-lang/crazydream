package com.crazydream.application.family.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 健康体检DTO
 */
public class HealthCheckupDTO {
    
    private Long id;
    private Long familyMemberId;
    private String familyMemberName;
    private LocalDate checkupDate;
    private LocalDate nextCheckupDate;
    private String hospital;
    private String notes;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFamilyMemberId() {
        return familyMemberId;
    }
    
    public void setFamilyMemberId(Long familyMemberId) {
        this.familyMemberId = familyMemberId;
    }
    
    public String getFamilyMemberName() {
        return familyMemberName;
    }
    
    public void setFamilyMemberName(String familyMemberName) {
        this.familyMemberName = familyMemberName;
    }
    
    public LocalDate getCheckupDate() {
        return checkupDate;
    }
    
    public void setCheckupDate(LocalDate checkupDate) {
        this.checkupDate = checkupDate;
    }
    
    public LocalDate getNextCheckupDate() {
        return nextCheckupDate;
    }
    
    public void setNextCheckupDate(LocalDate nextCheckupDate) {
        this.nextCheckupDate = nextCheckupDate;
    }
    
    public String getHospital() {
        return hospital;
    }
    
    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
