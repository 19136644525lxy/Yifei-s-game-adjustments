package com.yifei.ygd.keybinding;

import com.yifei.ygd.config.ConfigManager;
import com.yifei.ygd.YgdMod;
import com.yifei.ygd.game.ZoomSystem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final KeyBinding TOGGLE_INFO_DISPLAY = KeyBindingHelper.registerKeyBinding(
        new KeyBinding(
            "key.ygd.toggle_info_display",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.ygd.general"
        )
    );
    
    public static final KeyBinding ZOOM = KeyBindingHelper.registerKeyBinding(
        new KeyBinding(
            "key.ygd.zoom",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "category.ygd.general"
        )
    );
    
    public static void register() {
        YgdMod.LOGGER.info("Registering key bindings...");
        
        // 注册按键事件处理
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE_INFO_DISPLAY.wasPressed()) {
                // 切换信息显示的总开关
                if (ConfigManager.getConfig() != null && ConfigManager.getConfig().infoDisplay != null) {
                    boolean currentState = ConfigManager.getConfig().infoDisplay.enabled;
                    boolean newState = !currentState;
                    ConfigManager.getConfig().infoDisplay.enabled = newState;
                    ConfigManager.saveConfig();
                    
                    // 发送提示消息
                    if (client.player != null) {
                        // 使用本地化的提示文本
                        String statusKey = newState ? "info.ygd.toggle.status.enabled" : "info.ygd.toggle.status.disabled";
                        String message = net.minecraft.text.Text.translatable("info.ygd.toggle.message", net.minecraft.text.Text.translatable(statusKey)).getString();
                        client.player.sendMessage(net.minecraft.text.Text.literal(message), true);
                    }
                    
                    YgdMod.LOGGER.info("Info display toggled: " + newState);
                }
            }
        });
        
        YgdMod.LOGGER.info("Key bindings registered!");
    }
}