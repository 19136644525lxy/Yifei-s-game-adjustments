package com.yifei.ygd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "ygd/sweep/ygdsweepinggirl")
public class SweepConfig implements ConfigData {
    // 通用
    @ConfigEntry.Gui.CollapsibleObject
    public CommonConfig common = new CommonConfig();
    
    public static class CommonConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean isSweepEnable = false; // 扫地娘功能总开关
        
        @ConfigEntry.Gui.Tooltip
        public int sweepPeriod = 5; // 扫地周期（分钟）
        
        @ConfigEntry.Gui.Tooltip
        public int sweepNotify = 20; // 提前通知时间（秒）
        
        @ConfigEntry.Gui.Tooltip
        public int sweepDiscount = 10; // 倒计时时间（秒）
        
        @ConfigEntry.Gui.Tooltip
        public String sweepNotice = "<大扫除> 注意：还有 {0} 秒就要开始大扫除了喵~"; // 通知提示
        
        @ConfigEntry.Gui.Tooltip
        public String sweepNoticeComplete = "<大扫除> 这次一共扫掉了 {0} 个杂鱼的掉落物， {1} 个杂鱼 {2} 个经验杂鱼和 {3} 个其他杂鱼~ ， Ciallo(∠・ω< )⌒☆"; // 清扫完通知提示
        
        @ConfigEntry.Gui.Tooltip
        public String noticeColor = "white"; // 通知文本颜色
        
        @ConfigEntry.Gui.Tooltip
        public String completeColor = "white"; // 完成通知文本颜色
    }
    
    // 物品
    @ConfigEntry.Gui.CollapsibleObject
    public ItemConfig item = new ItemConfig();
    
    public static class ItemConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean isItemEntityCleanupEnable = true; // 物品实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean itemWhiteMode = true; // 白名单模式
        
        @ConfigEntry.Gui.Tooltip
        public boolean itemBlackMode = true; // 黑名单模式
        
        @ConfigEntry.Gui.Tooltip
        public String[] itemEntitiesWhitelist = {"minecraft:diamond", "minecraft:emerald"}; // 白名单
        
        @ConfigEntry.Gui.Tooltip
        public String[] itemEntitiesBlacklist = {}; // 黑名单
    }
    
    // 生物
    @ConfigEntry.Gui.CollapsibleObject
    public MobConfig mob = new MobConfig();
    
    public static class MobConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean isMobEntityCleanupEnable = true; // 生物实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean isExpOn = false; // 生物清理是否掉落经验
        
        @ConfigEntry.Gui.Tooltip
        public boolean isAnimalEntitiesCleanupEnable = true; // 动物实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean isMonsterEntitiesCleanupEnable = true; // 怪物实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean mobWhiteMode = true; // 白名单模式
        
        @ConfigEntry.Gui.Tooltip
        public boolean mobBlackMode = true; // 黑名单模式
        
        @ConfigEntry.Gui.Tooltip
        public String[] mobEntitiesWhitelist = {"minecraft:chicken", "minecraft:cat", "minecraft:mule", "minecraft:wolf", "minecraft:horse", "minecraft:donkey", "minecraft:wither", "minecraft:guardian", "minecraft:villager", "minecraft:iron_golem", "minecraft:snow_golem", "minecraft:vindicator", "minecraft:ender_dragon", "minecraft:elder_guardian"}; // 生物白名单
        
        @ConfigEntry.Gui.Tooltip
        public String[] mobEntitiesBlacklist = {}; // 生物黑名单
    }
    
    // 其他
    @ConfigEntry.Gui.CollapsibleObject
    public OtherConfig other = new OtherConfig();
    
    public static class OtherConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean isExperienceOrbEntityCleanupEnable = true; // 经验球实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean isFallingBlocksEntityCleanupEnable = true; // 下落方块实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean isArrowEntityCleanupEnable = true; // 箭头实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean isTridentEntityCleanupEnable = false; // 三叉戟实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean isDamagingProjectileEntityCleanupEnable = false; // 投射物实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean isShulkerBulletEntityCleanupEnable = true; // 子弹实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean isFireworkRocketEntityCleanupEnable = false; // 烟花火箭实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean isItemFrameEntityCleanupEnable = false; // 物品框实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean isPaintingEntityCleanupEnable = false; // 画实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean isBoatEntityCleanupEnable = false; // 船实体清理功能
        
        @ConfigEntry.Gui.Tooltip
        public boolean isTNTEntityCleanupEnable = true; // TNT实体清理功能
    }
}