package com.crazydream.application.family.scheduler;

import com.crazydream.domain.family.model.aggregate.FamilyMember;
import com.crazydream.domain.family.model.entity.HealthCheckup;
import com.crazydream.domain.family.repository.FamilyMemberRepository;
import com.crazydream.domain.family.repository.HealthCheckupRepository;
import com.crazydream.infrastructure.persistence.mapper.ReminderPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.ReminderPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 家庭提醒定时任务
 * 负责自动创建生日提醒和体检提醒
 */
@Component
public class FamilyReminderScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(FamilyReminderScheduler.class);
    
    // 生日提前提醒天数
    private static final int BIRTHDAY_ADVANCE_DAYS = 7;
    
    // 体检提前提醒天数
    private static final int HEALTH_CHECKUP_ADVANCE_DAYS = 30;
    
    @Autowired
    private FamilyMemberRepository familyMemberRepository;
    
    @Autowired
    private HealthCheckupRepository healthCheckupRepository;
    
    @Autowired
    private ReminderPersistenceMapper reminderPersistenceMapper;
    
    /**
     * 每天凌晨2点执行，创建生日提醒
     * 提前7天创建提醒
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void createBirthdayReminders() {
        logger.info("========== 开始执行生日提醒创建任务 ==========");
        
        try {
            // 1. 获取所有有生日的家庭成员
            List<FamilyMember> members = familyMemberRepository.findAllWithBirthday();
            logger.info("查询到 {} 个有生日信息的家庭成员", members.size());
            
            int createdCount = 0;
            int skippedCount = 0;
            
            // 2. 遍历每个成员，检查是否需要创建生日提醒
            for (FamilyMember member : members) {
                try {
                    boolean created = createBirthdayReminder(member);
                    if (created) {
                        createdCount++;
                    } else {
                        skippedCount++;
                    }
                } catch (Exception e) {
                    logger.error("为成员 {} 创建生日提醒失败", member.getName(), e);
                }
            }
            
            logger.info("生日提醒创建任务执行完成：创建 {} 个，跳过 {} 个", createdCount, skippedCount);
        } catch (Exception e) {
            logger.error("生日提醒创建任务执行失败", e);
        }
        
        logger.info("========== 生日提醒创建任务结束 ==========");
    }
    
    /**
     * 每天凌晨3点执行，创建体检提醒
     * 提前30天创建提醒
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void createHealthCheckupReminders() {
        logger.info("========== 开始执行体检提醒创建任务 ==========");
        
        try {
            // 1. 获取所有有下次体检日期的记录
            List<HealthCheckup> checkups = healthCheckupRepository.findAllWithNextCheckupDate();
            logger.info("查询到 {} 个有下次体检日期的体检记录", checkups.size());
            
            int createdCount = 0;
            int skippedCount = 0;
            
            // 2. 遍历每个体检记录，检查是否需要创建提醒
            for (HealthCheckup checkup : checkups) {
                try {
                    boolean created = createHealthCheckupReminder(checkup);
                    if (created) {
                        createdCount++;
                    } else {
                        skippedCount++;
                    }
                } catch (Exception e) {
                    logger.error("为体检记录 {} 创建提醒失败", checkup.getId(), e);
                }
            }
            
            logger.info("体检提醒创建任务执行完成：创建 {} 个，跳过 {} 个", createdCount, skippedCount);
        } catch (Exception e) {
            logger.error("体检提醒创建任务执行失败", e);
        }
        
        logger.info("========== 体检提醒创建任务结束 ==========");
    }
    
    /**
     * 创建生日提醒
     * @return true 如果创建了新提醒，false 如果跳过
     */
    private boolean createBirthdayReminder(FamilyMember member) {
        if (!member.hasBirthday()) {
            logger.debug("成员 {} 没有生日信息，跳过", member.getName());
            return false;
        }
        
        LocalDate birthday = member.getBirthday();
        LocalDate today = LocalDate.now();
        
        // 计算今年的生日
        LocalDate thisYearBirthday = LocalDate.of(today.getYear(), birthday.getMonth(), birthday.getDayOfMonth());
        
        // 如果今年生日已过，计算明年的生日
        if (thisYearBirthday.isBefore(today)) {
            thisYearBirthday = thisYearBirthday.plusYears(1);
        }
        
        // 计算提醒日期（提前N天）
        LocalDate reminderDate = thisYearBirthday.minusDays(BIRTHDAY_ADVANCE_DAYS);
        
        // 只有在提醒日期是今天时，才创建提醒
        if (!reminderDate.equals(today)) {
            logger.debug("成员 {} 的生日提醒日期不是今天（提醒日期：{}），跳过", member.getName(), reminderDate);
            return false;
        }
        
        // 检查是否已存在今年的生日提醒
        int existingCount = reminderPersistenceMapper.countByUserIdAndTypeAndRelatedId(
            member.getUserId().getValue(),
            "BIRTHDAY",
            member.getId().getValue()
        );
        
        if (existingCount > 0) {
            logger.debug("成员 {} 的生日提醒已存在，跳过", member.getName());
            return false;
        }
        
        // 创建新提醒
        ReminderPO reminder = new ReminderPO();
        reminder.setUserId(member.getUserId().getValue());
        reminder.setTitle(member.getName() + "的生日提醒");
        
        // 计算年龄
        int age = today.getYear() - birthday.getYear();
        if (thisYearBirthday.equals(today.plusDays(BIRTHDAY_ADVANCE_DAYS))) {
            age++; // 如果是即将到来的生日
        }
        
        reminder.setRemindTime(thisYearBirthday.atTime(9, 0)); // 生日当天9点提醒
        reminder.setIsRead(false);
        reminder.setRelatedEntityType("FAMILY_MEMBER");
        reminder.setRelatedEntityId(member.getId().getValue());
        reminder.setReminderType("BIRTHDAY");
        reminder.setCreateTime(LocalDateTime.now());
        reminder.setUpdateTime(LocalDateTime.now());
        
        reminderPersistenceMapper.insert(reminder);
        
        logger.info("✓ 成功为用户 {} 创建家庭成员 {} 的生日提醒（生日：{}，年龄：{}岁）", 
            member.getUserId().getValue(), member.getName(), thisYearBirthday, age);
        
        return true;
    }
    
    /**
     * 创建体检提醒
     * @return true 如果创建了新提醒，false 如果跳过
     */
    private boolean createHealthCheckupReminder(HealthCheckup checkup) {
        if (!checkup.hasNextCheckupDate()) {
            logger.debug("体检记录 {} 没有下次体检日期，跳过", checkup.getId());
            return false;
        }
        
        LocalDate nextCheckupDate = checkup.getNextCheckupDate();
        LocalDate today = LocalDate.now();
        
        // 如果下次体检日期已过期，跳过
        if (nextCheckupDate.isBefore(today)) {
            logger.debug("体检记录 {} 的下次体检日期已过期（{}），跳过", checkup.getId(), nextCheckupDate);
            return false;
        }
        
        // 计算提醒日期（提前N天）
        LocalDate reminderDate = nextCheckupDate.minusDays(HEALTH_CHECKUP_ADVANCE_DAYS);
        
        // 只有在提醒日期是今天时，才创建提醒
        if (!reminderDate.equals(today)) {
            logger.debug("体检记录 {} 的提醒日期不是今天（提醒日期：{}），跳过", checkup.getId(), reminderDate);
            return false;
        }
        
        // 需要查询家庭成员信息来获取userId和成员姓名
        FamilyMember member = familyMemberRepository.findById(checkup.getFamilyMemberId()).orElse(null);
        if (member == null) {
            logger.warn("体检记录 {} 关联的家庭成员不存在，跳过", checkup.getId());
            return false;
        }
        
        // 检查是否已存在该体检的提醒
        int existingCount = reminderPersistenceMapper.countByUserIdAndTypeAndRelatedId(
            member.getUserId().getValue(),
            "HEALTH_CHECKUP",
            checkup.getId()
        );
        
        if (existingCount > 0) {
            logger.debug("体检记录 {} 的提醒已存在，跳过", checkup.getId());
            return false;
        }
        
        // 创建新提醒
        ReminderPO reminder = new ReminderPO();
        reminder.setUserId(member.getUserId().getValue());
        reminder.setTitle(member.getName() + "的体检提醒");
        reminder.setRemindTime(nextCheckupDate.atTime(9, 0)); // 体检当天9点提醒
        reminder.setIsRead(false);
        reminder.setRelatedEntityType("HEALTH_CHECKUP");
        reminder.setRelatedEntityId(checkup.getId());
        reminder.setReminderType("HEALTH_CHECKUP");
        reminder.setCreateTime(LocalDateTime.now());
        reminder.setUpdateTime(LocalDateTime.now());
        
        reminderPersistenceMapper.insert(reminder);
        
        logger.info("✓ 成功为用户 {} 创建家庭成员 {} 的体检提醒（体检日期：{}）", 
            member.getUserId().getValue(), member.getName(), nextCheckupDate);
        
        return true;
    }
}
