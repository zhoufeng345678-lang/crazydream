package com.crazydream.domain.diary.model.aggregate;

import com.crazydream.domain.diary.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.domain.goal.model.valueobject.GoalId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 日记聚合根(充血模型)
 * 封装日记的所有业务逻辑和行为
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public class Diary {
    // 聚合根ID
    private DiaryId id;
    
    // 所属用户
    private UserId userId;
    
    // 日记标题
    private String title;
    
    // 日记内容(支持基础富文本HTML)
    private String content;
    
    // 日记分类
    private DiaryCategory category;
    
    // 标签列表
    private List<String> tags;
    
    // 心情
    private DiaryMood mood;
    
    // 天气
    private String weather;
    
    // 关联的目标ID
    private GoalId relatedGoalId;
    
    // 图片URL列表
    private List<String> imageUrls;
    
    // 是否公开
    private boolean isPublic;
    
    // 查看次数
    private int viewCount;
    
    // 日记日期
    private LocalDate diaryDate;
    
    // 创建时间
    private LocalDateTime createTime;
    
    // 更新时间
    private LocalDateTime updateTime;
    
    // ==================== 构造函数 ====================
    
    /**
     * 私有构造函数,通过工厂方法创建
     */
    private Diary() {
        this.tags = new ArrayList<>();
        this.imageUrls = new ArrayList<>();
    }
    
    /**
     * 创建新日记(工厂方法)
     */
    public static Diary create(UserId userId, String title, String content, LocalDate diaryDate) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("日记标题不能为空");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("日记内容不能为空");
        }
        
        Diary diary = new Diary();
        diary.userId = userId;
        diary.title = title;
        diary.content = content;
        diary.diaryDate = diaryDate != null ? diaryDate : LocalDate.now();
        diary.category = DiaryCategory.GENERAL;
        diary.isPublic = false;
        diary.viewCount = 0;
        diary.createTime = LocalDateTime.now();
        diary.updateTime = LocalDateTime.now();
        
        return diary;
    }
    
    /**
     * 重建日记(从数据库加载)
     */
    public static Diary rebuild(DiaryId id, UserId userId, String title, String content,
                                 DiaryCategory category, List<String> tags, DiaryMood mood,
                                 String weather, GoalId relatedGoalId, List<String> imageUrls,
                                 boolean isPublic, int viewCount, LocalDate diaryDate,
                                 LocalDateTime createTime, LocalDateTime updateTime) {
        Diary diary = new Diary();
        diary.id = id;
        diary.userId = userId;
        diary.title = title;
        diary.content = content;
        diary.category = category;
        diary.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        diary.mood = mood;
        diary.weather = weather;
        diary.relatedGoalId = relatedGoalId;
        diary.imageUrls = imageUrls != null ? new ArrayList<>(imageUrls) : new ArrayList<>();
        diary.isPublic = isPublic;
        diary.viewCount = viewCount;
        diary.diaryDate = diaryDate;
        diary.createTime = createTime;
        diary.updateTime = updateTime;
        
        return diary;
    }
    
    // ==================== 业务方法 ====================
    
    /**
     * 更新日记内容
     */
    public void updateContent(String title, String content) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
        }
        if (content != null && !content.trim().isEmpty()) {
            this.content = content;
        }
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 设置分类
     */
    public void setCategory(DiaryCategory category) {
        this.category = category;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 添加标签
     */
    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty() && !this.tags.contains(tag)) {
            this.tags.add(tag);
            this.updateTime = LocalDateTime.now();
        }
    }
    
    /**
     * 移除标签
     */
    public void removeTag(String tag) {
        this.tags.remove(tag);
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 设置心情
     */
    public void setMood(DiaryMood mood) {
        this.mood = mood;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 设置天气
     */
    public void setWeather(String weather) {
        this.weather = weather;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 关联到目标
     */
    public void linkToGoal(GoalId goalId) {
        this.relatedGoalId = goalId;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 取消关联目标
     */
    public void unlinkGoal() {
        this.relatedGoalId = null;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 添加图片
     */
    public void addImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            this.imageUrls.add(imageUrl);
            this.updateTime = LocalDateTime.now();
        }
    }
    
    /**
     * 公开日记
     */
    public void publish() {
        this.isPublic = true;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 设为私密
     */
    public void makePrivate() {
        this.isPublic = false;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 增加查看次数
     */
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    // ==================== Getters and Setters ====================
    
    public DiaryId getId() {
        return id;
    }
    
    public void setId(DiaryId id) {
        this.id = id;
    }
    
    public UserId getUserId() {
        return userId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getContent() {
        return content;
    }
    
    public DiaryCategory getCategory() {
        return category;
    }
    
    public List<String> getTags() {
        return new ArrayList<>(tags);
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
    }
    
    public DiaryMood getMood() {
        return mood;
    }
    
    public String getWeather() {
        return weather;
    }
    
    public GoalId getRelatedGoalId() {
        return relatedGoalId;
    }
    
    public List<String> getImageUrls() {
        return new ArrayList<>(imageUrls);
    }
    
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls != null ? new ArrayList<>(imageUrls) : new ArrayList<>();
    }
    
    public boolean isPublic() {
        return isPublic;
    }
    
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    public int getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    
    public LocalDate getDiaryDate() {
        return diaryDate;
    }
    
    public void setDiaryDate(LocalDate diaryDate) {
        this.diaryDate = diaryDate;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Diary diary = (Diary) o;
        return Objects.equals(id, diary.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Diary{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", diaryDate=" + diaryDate +
                '}';
    }
}
