package com.crazydream.domain.achievement.model.valueobject;

public enum AchievementType {
    // 目标完成数量系列
    FIRST_GOAL("first_goal", "首个目标", "创建第一个目标"),
    GOAL_COMPLETED_10("goal_10", "小有成就", "完成10个目标"),
    GOAL_COMPLETED_30("goal_30", "初露锋芒", "完成30个目标"),
    GOAL_COMPLETED_50("goal_50", "坚持不懈", "完成50个目标"),
    GOAL_COMPLETED_100("goal_100", "成就达人", "完成100个目标"),
    GOAL_COMPLETED_200("goal_200", "目标大师", "完成200个目标"),
    
    // 连续打卡系列
    CONSECUTIVE_3("consecutive_3", "三日坚持", "连续3天完成目标"),
    CONSECUTIVE_7("consecutive_7", "七日坚持", "连续7天完成目标"),
    CONSECUTIVE_14("consecutive_14", "两周习惯", "连续14天完成目标"),
    CONSECUTIVE_30("consecutive_30", "月度冠军", "连续30天完成目标"),
    CONSECUTIVE_100("consecutive_100", "百日传奇", "连续100天完成目标"),
    
    // 分类专注系列
    CATEGORY_MASTER_10("category_master_10", "分类达人", "在单个分类完成10个目标"),
    CATEGORY_MASTER_30("category_master_30", "分类专家", "在单个分类完成30个目标"),
    ALL_CATEGORY_EXPLORER("all_category_explorer", "全能选手", "在所有分类都至少完成1个目标"),
    
    // 效率提升系列
    EARLY_BIRD("early_bird", "早起鸟", "早上6-8点完成5个目标"),
    NIGHT_OWL("night_owl", "夜猫子", "晚上22-24点完成5个目标"),
    SPEED_MASTER("speed_master", "效率达人", "创建目标后24小时内完成，累计10次"),
    DEADLINE_KEEPER("deadline_keeper", "守时之星", "提前完成有截止日期的目标，累计20次"),
    
    // 里程碑系列
    FIRST_WEEK("first_week", "初入殿堂", "使用系统满7天"),
    FIRST_MONTH("first_month", "月度会员", "使用系统满30天"),
    ONE_YEAR("one_year", "年度坚持", "使用系统满365天"),
    HIGH_COMPLETION_RATE("high_completion_rate", "完美主义者", "目标完成率达到90%，且完成目标数>=20"),
    
    // 等级提升
    LEVEL_UP("level_up", "等级提升", "用户等级提升");
    
    private final String code;
    private final String name;
    private final String description;
    
    AchievementType(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    public static AchievementType fromCode(String code) {
        for (AchievementType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的成就类型: " + code);
    }
    
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
