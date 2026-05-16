package com.yifei.ygd;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ClientModInitializer;

public class YgdMod implements ModInitializer {
    public static final String MOD_ID = "ygd";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // 振刀音效事件
    public static final SoundEvent ZHENDAO_SOUND = Registry.register(
        Registries.SOUND_EVENT,
        new Identifier(MOD_ID, "zhendao"),
        SoundEvent.of(new Identifier(MOD_ID, "zhendao"))
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Yifei's game adjustments initialized!");
        // 注册物品
        com.yifei.ygd.item.YgdItems.register();
        // 初始化配置管理器
        com.yifei.ygd.config.ConfigManager.initialize();
        // 注册事件监听器
        com.yifei.ygd.event.CakeBreakListener.register();
        com.yifei.ygd.event.ItemTooltipListener.register();
        com.yifei.ygd.event.SmithingTableListener.register();
        com.yifei.ygd.event.AnvilListener.register();
        com.yifei.ygd.event.LootTableListener.register();
        // 注册命令
        com.yifei.ygd.command.CommandRegistry.register();
        // 初始化 IMBlocker
        initializeIMBlocker();
        // 初始化游戏机制管理器
        com.yifei.ygd.game.GameMechanismManager.getInstance().initialize();
        // 注册按键绑定
        com.yifei.ygd.keybinding.KeyBindings.register();
        // 初始化配方注册
        com.yifei.ygd.recipe.RecipeRegistry.init();
        // 初始化配方修改
        new com.yifei.ygd.recipe.RecipeModifications().onInitialize();
        // 注册服务器启动事件
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            YgdMod.setServer(server);
            LOGGER.info("MinecraftServer instance set!");
        });
    }



    private void initializeIMBlocker() {
        LOGGER.info("Initializing IMBlocker...");
        // 注册客户端 tick 事件监听器
        ClientTickEvents.START_CLIENT_TICK.register(tick -> {
            com.yifei.ygd.imblocker.IMCheckState.clientTick(new com.yifei.ygd.imblocker.FabricScreenInfo());
        });
        LOGGER.info("IMBlocker initialized!");
    }
    
    private static net.minecraft.server.MinecraftServer server;
    
    public static void setServer(net.minecraft.server.MinecraftServer minecraftServer) {
        server = minecraftServer;
    }
    
    public static net.minecraft.server.MinecraftServer getServer() {
        return server;
    }
}
