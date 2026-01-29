package com.crazydream.domain.achievement.model.valueobject;

public enum AchievementType {
    // 目标完成数量系列
    FIRST_GOAL("first_goal", "首个目标", "创建第一个目标", "goal_count", "bronze", "🌟", 1, 100),
    GOAL_COMPLETED_10("goal_10", "小有成就", "完成10个目标", "goal_count", "silver", "🏆", 2, 200),
    GOAL_COMPLETED_30("goal_30", "初露锋芒", "完成30个目标", "goal_count", "gold", "🎯", 3, 300),
    GOAL_COMPLETED_50("goal_50", "坚持不懈", "完成50个目标", "goal_count", "platinum", "💪", 4, 400),
    GOAL_COMPLETED_100("goal_100", "成就达人", "完成100个目标", "goal_count", "diamond", "🏅", 5, 500),
    GOAL_COMPLETED_200("goal_200", "目标大师", "完成200个目标", "goal_count", "diamond", "👑", 6, 600),
    
    // 连续打卡系列
    CONSECUTIVE_3("consecutive_3", "三日坚持", "连续3天完成目标", "consecutive", "bronze", "🔥", 7, 110),
    CONSECUTIVE_7("consecutive_7", "七日坚持", "连续7天完成目标", "consecutive", "silver", "🔥", 8, 210),
    CONSECUTIVE_14("consecutive_14", "两周习惯", "连续14天完成目标", "consecutive", "gold", "🔥", 9, 310),
    CONSECUTIVE_30("consecutive_30", "月度冠军", "连续30天完成目标", "consecutive", "platinum", "🔥", 10, 410),
    CONSECUTIVE_100("consecutive_100", "百日传奇", "连续100天完成目标", "consecutive", "diamond", "🔥", 11, 510),
    
    // 分类专注系列
    CATEGORY_MASTER_10("category_master_10", "分类达人", "在单个分类完成10个目标", "category_focus", "silver", "🌈", 12, 220),
    CATEGORY_MASTER_30("category_master_30", "分类专家", "在单个分类完成30个目标", "category_focus", "gold", "🌈", 13, 320),
    ALL_CATEGORY_EXPLORER("all_category_explorer", "全能选手", "在所有分类都至少完成1个目标", "category_focus", "platinum", "🌈", 14, 420),
    
    // 效率提升系列
    EARLY_BIRD("early_bird", "早起鸟", "早上6-8点完成5个目标", "efficiency", "silver", "🌅", 15, 230),
    NIGHT_OWL("night_owl", "夜猫子", "晚上22-24点完成5个目标", "efficiency", "silver", "🌙", 16, 240),
    SPEED_MASTER("speed_master", "效率达人", "创建目标后24小时内完成，累计10次", "efficiency", "gold", "⚡", 17, 330),
    DEADLINE_KEEPER("deadline_keeper", "守时之星", "提前完成有截止日期的目标，累计20次", "efficiency", "gold", "🛡️", 18, 340),
    
    // 里程碑系列
    FIRST_WEEK("first_week", "初入殿堂", "使用系统数7天", "milestone", "bronze", "🎪", 19, 150),
    FIRST_MONTH("first_month", "月度会员", "使用系统数30天", "milestone", "silver", "🎪", 20, 250),
    ONE_YEAR("one_year", "年度坚持", "使用系统满365天", "milestone", "diamond", "🎪", 21, 550),
    HIGH_COMPLETION_RATE("high_completion_rate", "完美主义者", "目标完成率达到90%，且完成目标数>=20", "milestone", "platinum", "🎖️", 22, 450),
    
    // 等级提升
    LEVEL_UP("level_up", "等级提升", "用户等级提升", "milestone", "bronze", "⬆️", 23, 160),
    
    // 日记相关成就
    FIRST_DIARY("first_diary", "初次记录", "写下第一篇日记", "diary", "bronze", "📝", 30, 100),
    DIARY_WEEK("diary_week", "一周坚持", "连续7天写日记", "diary", "silver", "📖", 31, 200),
    DIARY_MONTH("diary_month", "月度笔耕", "连续30天写日记", "diary", "gold", "📚", 32, 300),
    DIARY_100("diary_100", "日记达人", "累计写100篇日记", "diary", "platinum", "🖊️", 33, 500),
    
    // 待办相关成就
    FIRST_TODO("first_todo", "计划开始", "创建第一个待办事项", "todo", "bronze", "✅", 34, 100),
    TODO_COMPLETED_10("todo_10", "行动派", "完成10个待办事项", "todo", "silver", "💪", 35, 200),
    TODO_PRIORITY("todo_priority", "高效能", "完成10个高优先级待办", "todo", "gold", "⚡", 36, 300),
    TODO_STREAK("todo_streak", "每日必达", "连续7天完成待办事项", "todo", "platinum", "🎯", 37, 500);
    
    private final String code;
    private final String name;
    private final String description;
    private final String category;      // 成就分类
    private final String tier;          // 成就等级
    private final String icon;          // 成就图标emoji
    private final int sortOrder;        // 排序权重
    private final int target;           // 目标值
    
    AchievementType(String code, String name, String description, String category, 
                    String tier, String icon, int sortOrder, int target) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.category = category;
        this.tier = tier;
        this.icon = icon;
        this.sortOrder = sortOrder;
        this.target = target;
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
    public String getCategory() { return category; }
    public String getTier() { return tier; }
    public String getIcon() { return icon; }
    public int getSortOrder() { return sortOrder; }
    public int getTarget() { return target; }
}
