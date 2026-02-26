package com.crazydream.application.family.command;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 创建健康体检记录命令
 */
public class HealthCheckupCreateCmd {
    
    @NotNull(message = "家庭成员ID不能为空")
    private Long familyMemberId;
    
    @NotNull(message = "体检日期不能为空")
    private LocalDate checkupDate;
    
    private LocalDate nextCheckupDate;
    
    private String hospital;
    
    private String notes;
    
    // Getters and Setters
    
    public Long getFamilyMemberId() {
        return familyMemberId;
    }
    
    public void setFamilyMemberId(Long familyMemberId) {
        this.familyMemberId = familyMemberId;
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
}
