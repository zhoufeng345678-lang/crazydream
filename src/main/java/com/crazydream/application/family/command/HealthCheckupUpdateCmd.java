package com.crazydream.application.family.command;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 更新健康体检记录命令
 */
public class HealthCheckupUpdateCmd {
    
    @NotNull(message = "体检记录ID不能为空")
    private Long id;
    
    private LocalDate checkupDate;
    
    private LocalDate nextCheckupDate;
    
    private String hospital;
    
    private String notes;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
