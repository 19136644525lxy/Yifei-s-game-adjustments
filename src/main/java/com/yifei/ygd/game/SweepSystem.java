package com.yifei.ygd.game;

import com.yifei.ygd.YgdMod;
import com.yifei.ygd.config.ConfigManager;
import com.yifei.ygd.config.SweepConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.Identifier;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SweepSystem {
    private static SweepSystem instance;
    private Timer timer;
    private int currentCountdown = 0;
    private boolean isCountingDown = false;
    
    public static SweepSystem getInstance() {
        if (instance == null) {
            instance = new SweepSystem();
        }
        return instance;
    }
    
    public void initialize() {
        YgdMod.LOGGER.info("Initializing sweep system...");
        startSweepTimer();
        YgdMod.LOGGER.info("Sweep system initialized!");
    }
    
    private void startSweepTimer() {
        SweepConfig config = ConfigManager.getSweepConfig();
        if (!config.common.isSweepEnable) {
            YgdMod.LOGGER.info("Sweep system is disabled in config");
            return;
        }
        
        timer = new Timer();
        long period = config.common.sweepPeriod * 60 * 1000; // 转换为毫秒
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                startCountdown();
            }
        }, 0, period);
    }
    
    private void startCountdown() {
        SweepConfig config = ConfigManager.getSweepConfig();
        
        // 发送第一次通知到ActionBar
        sendSweepNoticeToActionBar(config.common.sweepNotice, config.common.sweepNotify);
        
        // 计算第二次通知的延迟时间（sweepNotify - sweepDiscount秒后）
        long secondNoticeDelay = (config.common.sweepNotify - config.common.sweepDiscount) * 1000L;
        
        // 安排第二次通知
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // 发送第二次通知到ActionBar
                sendSweepNoticeToActionBar(config.common.sweepNotice, config.common.sweepDiscount);
            }
        }, secondNoticeDelay);
        
        // 计算总延迟时间
        long totalDelay = config.common.sweepNotify * 1000L;
        
        // 安排清理任务
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // 执行清理
                performSweep();
            }
        }, totalDelay);
        
        // 发送倒计时通知到聊天框
        new Timer().scheduleAtFixedRate(new TimerTask() {
            int chatCountdown = config.common.sweepDiscount;
            
            @Override
            public void run() {
                if (chatCountdown > 0) {
                    // 发送倒计时通知到聊天框
                    sendSweepNotice(config.common.sweepNotice, chatCountdown);
                    chatCountdown--;
                } else {
                    cancel();
                }
            }
        }, secondNoticeDelay, 1000); // 每秒执行一次
    }
    
    private void sendSweepNotice(String message, int seconds) {
        MinecraftServer server = YgdMod.getServer();
        if (server != null) {
            String formattedMessage = message.replace("{0}", String.valueOf(seconds));
            SweepConfig config = ConfigManager.getSweepConfig();
            Formatting color = getFormattingFromString(config.common.noticeColor);
            Text text = Text.literal(formattedMessage).formatted(color);
            // 发送到聊天框
            server.getPlayerManager().broadcast(text, false);
        }
    }
    
    private void sendSweepNoticeToActionBar(String message, int seconds) {
        MinecraftServer server = YgdMod.getServer();
        if (server != null) {
            String formattedMessage = message.replace("{0}", String.valueOf(seconds));
            SweepConfig config = ConfigManager.getSweepConfig();
            Formatting color = getFormattingFromString(config.common.noticeColor);
            Text text = Text.literal(formattedMessage).formatted(color);
            // 发送到物品栏上方（ActionBar）
            for (net.minecraft.server.network.ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                player.sendMessage(text, true);
            }
        }
    }
    
    public void performSweep() {
        MinecraftServer server = YgdMod.getServer();
        if (server != null) {
            int itemCount = 0;
            int mobCount = 0;
            int expCount = 0;
            int otherCount = 0;
            
            for (World world : server.getWorlds()) {
                // 使用一个非常大的边界框来覆盖整个世界
                Box box = new Box(-30000000, -64, -30000000, 30000000, 320, 30000000);
                List<Entity> entities = world.getEntitiesByClass(Entity.class, box, entity -> true);
                // 创建临时列表存储需要清理的实体
                List<Entity> entitiesToCleanup = new ArrayList<>();
                
                for (Entity entity : entities) {
                    if (shouldCleanup(entity)) {
                        entitiesToCleanup.add(entity);
                    }
                }
                
                // 统一清理实体
                for (Entity entity : entitiesToCleanup) {
                    if (entity instanceof ItemEntity) {
                        itemCount++;
                    } else if (entity instanceof LivingEntity) {
                        mobCount++;
                    } else if (entity instanceof ExperienceOrbEntity) {
                        expCount++;
                    } else {
                        otherCount++;
                    }
                    entity.kill();
                }
            }
            
            // 发送清理完成通知
            SweepConfig config = ConfigManager.getSweepConfig();
            String message = config.common.sweepNoticeComplete
                .replace("{0}", String.valueOf(itemCount))
                .replace("{1}", String.valueOf(mobCount))
                .replace("{2}", String.valueOf(expCount))
                .replace("{3}", String.valueOf(otherCount));
            Formatting color = getFormattingFromString(config.common.noticeColor);
            Text text = Text.literal(message).formatted(color);
            
            // 发送到物品栏上方（ActionBar）
            for (net.minecraft.server.network.ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                player.sendMessage(text, true);
            }
        }
    }
    
    private boolean shouldCleanup(Entity entity) {
        SweepConfig config = ConfigManager.getSweepConfig();
        
        // 检查物品实体
        if (entity instanceof ItemEntity && config.item.isItemEntityCleanupEnable) {
            return shouldCleanupItem((ItemEntity) entity, config);
        }
        
        // 检查生物实体
        if (entity instanceof LivingEntity && config.mob.isMobEntityCleanupEnable) {
            return shouldCleanupMob((LivingEntity) entity, config);
        }
        
        // 检查经验球
        if (entity instanceof ExperienceOrbEntity && config.other.isExperienceOrbEntityCleanupEnable) {
            return true;
        }
        
        // 检查下落方块
        if (entity instanceof FallingBlockEntity && config.other.isFallingBlocksEntityCleanupEnable) {
            return true;
        }
        
        // 检查箭头
        if (entity instanceof ArrowEntity && config.other.isArrowEntityCleanupEnable) {
            return true;
        }
        
        // 检查三叉戟
        if (entity instanceof TridentEntity && config.other.isTridentEntityCleanupEnable) {
            return true;
        }
        
        // 检查子弹
        if (entity instanceof ShulkerBulletEntity && config.other.isShulkerBulletEntityCleanupEnable) {
            return true;
        }
        
        // 检查烟花火箭
        if (entity instanceof FireworkRocketEntity && config.other.isFireworkRocketEntityCleanupEnable) {
            return true;
        }
        
        // 检查物品框
        if (entity instanceof ItemFrameEntity && config.other.isItemFrameEntityCleanupEnable) {
            return true;
        }
        
        // 检查画
        if (entity instanceof PaintingEntity && config.other.isPaintingEntityCleanupEnable) {
            return true;
        }
        
        // 检查船
        if (entity instanceof BoatEntity && config.other.isBoatEntityCleanupEnable) {
            return true;
        }
        
        // 检查TNT
        if (entity instanceof TntEntity && config.other.isTNTEntityCleanupEnable) {
            return true;
        }
        
        return false;
    }
    
    private boolean shouldCleanupItem(ItemEntity item, SweepConfig config) {
        // 检查是否有自定义名称，如果有则不清理
        if (item.getCustomName() != null) {
            return false;
        }
        
        Identifier itemId = Registries.ITEM.getId(item.getStack().getItem());
        
        // 检查白名单
        if (config.item.itemWhiteMode) {
            for (String whitelistItem : config.item.itemEntitiesWhitelist) {
                if (itemMatch(whitelistItem, itemId)) {
                    return false; // 白名单中的物品不清理
                }
            }
            return true; // 白名单外的物品清理
        }
        
        // 检查黑名单
        if (config.item.itemBlackMode) {
            for (String blacklistItem : config.item.itemEntitiesBlacklist) {
                if (itemMatch(blacklistItem, itemId)) {
                    return true; // 黑名单中的物品清理
                }
            }
            return false; // 黑名单外的物品不清理
        }
        
        // 都没启用，默认清理所有物品
        return true;
    }
    
    private boolean itemMatch(String pattern, Identifier itemId) {
        if (pattern.equals(itemId.toString())) {
            return true;
        } else if (pattern.endsWith("*")) {
            String namespace = pattern.substring(0, pattern.length() - 1);
            return itemId.getNamespace().equals(namespace);
        }
        return false;
    }
    
    private boolean shouldCleanupMob(LivingEntity mob, SweepConfig config) {
        // 检查是否是玩家，如果是玩家则不清理
        if (mob instanceof PlayerEntity) {
            return false;
        }
        
        // 检查是否有自定义名称，如果有则不清理
        if (mob.getCustomName() != null) {
            return false;
        }
        
        // 检查是否是幼年动物，如果是幼年动物则不清理
        if (config.mob.isAnimalEntitiesCleanupEnable && mob.isAlive() && mob.isBaby()) {
            return false; // 不清理幼年动物
        }
        
        Identifier mobId = EntityType.getId(mob.getType());
        
        // 检查白名单
        if (config.mob.mobWhiteMode) {
            for (String whitelistMob : config.mob.mobEntitiesWhitelist) {
                if (mobMatch(whitelistMob, mobId)) {
                    return false; // 白名单中的生物不清理
                }
            }
            return true; // 白名单外的生物清理
        }
        
        // 检查黑名单
        if (config.mob.mobBlackMode) {
            for (String blacklistMob : config.mob.mobEntitiesBlacklist) {
                if (mobMatch(blacklistMob, mobId)) {
                    return true; // 黑名单中的生物清理
                }
            }
            return false; // 黑名单外的生物不清理
        }
        
        // 都没启用，默认清理所有生物
        return true;
    }
    
    private boolean mobMatch(String pattern, Identifier mobId) {
        if (pattern.equals(mobId.toString())) {
            return true;
        } else if (pattern.endsWith("*")) {
            String namespace = pattern.substring(0, pattern.length() - 1);
            return mobId.getNamespace().equals(namespace);
        }
        return false;
    }
    
    /**
     * 将字符串转换为 Formatting 枚举值
     * @param colorString 颜色字符串
     * @return Formatting 枚举值
     */
    private Formatting getFormattingFromString(String colorString) {
        try {
            return Formatting.valueOf(colorString.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 如果颜色字符串无效，默认使用白色
            return Formatting.WHITE;
        }
    }
    
    public void shutdown() {
        if (timer != null) {
            timer.cancel();
        }
    }
    
    public boolean isCountingDown() {
        return isCountingDown;
    }
    
    public int getCurrentCountdown() {
        return currentCountdown;
    }
}