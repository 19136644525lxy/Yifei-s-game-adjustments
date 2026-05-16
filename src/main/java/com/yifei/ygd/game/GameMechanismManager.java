package com.yifei.ygd.game;

import com.yifei.ygd.YgdMod;

public class GameMechanismManager {
    private static final GameMechanismManager INSTANCE = new GameMechanismManager();
    
    private ItemSystem itemSystem;
    private ToolSystem toolSystem;
    
    private GameMechanismManager() {
        itemSystem = new ItemSystem();
        toolSystem = new ToolSystem();
    }
    
    public static GameMechanismManager getInstance() {
        return INSTANCE;
    }
    
    public void initialize() {
        YgdMod.LOGGER.info("Initializing game mechanism manager...");
        
        // 检查配置
        com.yifei.ygd.config.YgdConfig config = com.yifei.ygd.config.ConfigManager.getConfig();
        
        if (config != null && config.itemInfo != null && config.itemInfo.showDurability) {
            itemSystem.initialize();
            toolSystem.initialize();
        }
        
        // 初始化信息显示管理器
        InfoDisplayManager.getInstance().initialize();
        
        // 初始化缩放系统
        ZoomSystem.getInstance().initialize();
        
        // 初始化扫地系统
        SweepSystem.getInstance().initialize();
        
        YgdMod.LOGGER.info("Game mechanism manager initialized!");
    }
    
    public ItemSystem getItemSystem() {
        return itemSystem;
    }
    
    public ToolSystem getToolSystem() {
        return toolSystem;
    }
}