package com.yifei.ygd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "ygd/ygd-common")
public class YgdConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public CakeConfig cake = new CakeConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public ItemInfoConfig itemInfo = new ItemInfoConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public SugarcaneConfig sugarcane = new SugarcaneConfig();

    public static class CakeConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enableCakeDrop = true;
    }

    public static class ItemInfoConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean showDurability = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler
        public DurabilityColorScheme durabilityColorScheme = DurabilityColorScheme.VANILLA;
    }

    public enum DurabilityColorScheme {
        VANILLA, // 原版颜色（绿色、黄色、红色）
        BRIGHT, // 明亮颜色（亮绿、亮黄、亮红）
        PASTEL, // 柔和颜色（粉绿、粉黄、粉红）
        MONOCHROME // 单色（白色、灰色、黑色）
    }

    public static class SugarcaneConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enableSugarcaneBoneMeal = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 256)
        public int heightLimit = 3;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public InfoDisplayConfig infoDisplay = new InfoDisplayConfig();

    public static class InfoDisplayConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showFps = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showTps = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showMspt = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showDirection = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showBiome = true;

        @ConfigEntry.Gui.Tooltip
        public boolean showRealTime = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 30)
        public int updateInterval = 10; // 更新间隔（tick）

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = -500, max = 500)
        public int horizontalOffset = 10; // 横向偏移量

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = -500, max = 500)
        public int verticalOffset = 10; // 纵向偏移量
    }

    @ConfigEntry.Gui.CollapsibleObject
    public ZoomConfig zoom = new ZoomConfig();

    public static class ZoomConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 2, max = 10)
        public int initialZoom = 4;

        @ConfigEntry.Gui.Tooltip
        public boolean linearLikeSteps = false;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
        public int zoomInTime = 100; // 毫秒

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
        public int zoomOutTime = 100; // 毫秒

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler
        public TransitionType zoomInTransition = TransitionType.EASE_OUT_EXP;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler
        public TransitionType zoomOutTransition = TransitionType.EASE_OUT_EXP;

        @ConfigEntry.Gui.Tooltip
        public boolean retainZoomSteps = false;

        @ConfigEntry.Gui.Tooltip
        public boolean scrollZoom = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 50)
        public int scrollStepCount = 10;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 50, max = 300)
        public int zoomPerStep = 150;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 10, max = 100)
        public int scrollZoomSmoothness = 70;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler
        public ZoomKeyBehaviour zoomKeyBehaviour = ZoomKeyBehaviour.HOLD;
    }

    public enum TransitionType {
        LINEAR, EASE_IN, EASE_OUT, EASE_IN_OUT, EASE_OUT_EXP
    }

    public enum ZoomKeyBehaviour {
        HOLD, TOGGLE
    }

    @ConfigEntry.Gui.CollapsibleObject
    public IMBlockerConfig imBlocker = new IMBlockerConfig();

    public static class IMBlockerConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = false; // 默认禁用
    }

    @ConfigEntry.Gui.CollapsibleObject
    public AttackCooldownConfig attackCooldown = new AttackCooldownConfig();

    public static class AttackCooldownConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = true; // 默认启用攻击冷却（原版行为）
    }

    @ConfigEntry.Gui.CollapsibleObject
    public StackSizeConfig stackSize = new StackSizeConfig();

    public static class StackSizeConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = false; // 默认禁用堆叠数量修改（原版行为）
    }

    @ConfigEntry.Gui.CollapsibleObject
    public BlockConfig block = new BlockConfig();

    public static class BlockConfig {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = false; // 默认禁用格挡功能（原版行为）

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public int damageReductionPercentage = 50; // 格挡伤害减少百分比

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public int parryChance = 50; // 振刀成功概率（百分比）
    }


}
