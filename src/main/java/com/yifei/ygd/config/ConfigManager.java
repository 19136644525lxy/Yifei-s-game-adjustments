package com.yifei.ygd.config;

import com.yifei.ygd.YgdMod;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    private static ConfigHolder<YgdConfig> configHolder;
    private static ConfigHolder<SweepConfig> sweepConfigHolder;
    
    public static void initialize() {
        YgdMod.LOGGER.info("Initializing config manager...");
        
        // 注册通用配置
        configHolder = AutoConfig.register(YgdConfig.class, GsonConfigSerializer::new);
        
        // 注册扫地功能配置
        sweepConfigHolder = AutoConfig.register(SweepConfig.class, Toml4jConfigSerializer::new);
        
        // 确保配置目录存在
        ensureConfigDirectories();
        
        YgdMod.LOGGER.info("Config manager initialized!");
    }
    
    private static void ensureConfigDirectories() {
        try {
            // 确保通用配置目录存在
            Path commonConfigDir = Paths.get(".minecraft", "config", "ygd");
            if (!Files.exists(commonConfigDir)) {
                Files.createDirectories(commonConfigDir);
            }
            
            // 确保扫地功能配置目录存在
            Path sweepConfigDir = Paths.get(".minecraft", "config", "ygd", "sweep");
            if (!Files.exists(sweepConfigDir)) {
                Files.createDirectories(sweepConfigDir);
            }
        } catch (IOException e) {
            YgdMod.LOGGER.error("Failed to create config directories: {}", e.getMessage());
        }
    }
    
    public static YgdConfig getConfig() {
        if (configHolder == null) {
            return null;
        }
        return configHolder.getConfig();
    }
    
    public static SweepConfig getSweepConfig() {
        if (sweepConfigHolder == null) {
            return null;
        }
        return sweepConfigHolder.getConfig();
    }
    
    public static void saveConfig() {
        if (configHolder != null) {
            configHolder.save();
        }
    }
    
    public static void saveSweepConfig() {
        if (sweepConfigHolder != null) {
            sweepConfigHolder.save();
        }
    }
    
    public static void loadConfig() {
        if (configHolder != null) {
            configHolder.load();
        }
    }
    
    public static void loadSweepConfig() {
        if (sweepConfigHolder != null) {
            sweepConfigHolder.load();
        }
    }
    
    public static ConfigHolder<YgdConfig> getConfigHolder() {
        return configHolder;
    }
    
    public static ConfigHolder<SweepConfig> getSweepConfigHolder() {
        return sweepConfigHolder;
    }
}