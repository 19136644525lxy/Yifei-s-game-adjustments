package com.yifei.ygd.imblocker;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.screen.ingame.HangingSignEditScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;

import java.util.List;
import java.util.regex.Pattern;

public interface Config {
    Config INSTANCE = new DefaultConfig();

    boolean inScreenWhitelist(Class<?> cls);

    boolean inScreenBlacklist(Class<?> cls);

    boolean inInputWhitelist(Class<?> cls);

    boolean inInputBlacklist(Class<?> cls);

    void checkScreen(Class<?> cls);

    boolean getCheckCommandChat();

    boolean getUseExperimental();

    int getCheckInterval();

    boolean isEnabled();

    class DefaultConfig implements Config {
        private static final List<String> defaultScreenWhitelist = List.of(
                BookEditScreen.class.getName(),
                SignEditScreen.class.getName(),
                HangingSignEditScreen.class.getName(),
                TitleScreen.class.getName(),
                net.minecraft.client.gui.screen.ChatScreen.class.getName(),
                "journeymap.client.ui.waypoint.WaypointEditor",
                "com.ldtteam.blockout.BOScreen",
                // 创造模式搜索栏
                "net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen",
                // JEI搜索栏
                "mezz.jei.gui.screens.RecipeScreen"
        );
        private static final List<String> defaultScreenBlacklist = List.of(
                "com.mamiyaotaru.voxelmap.persistent.GuiPersistentMap"
        );
        private static final Pattern textFieldPattern = Pattern.compile(".*(TextField|EditBox|EditText)[^.]*$", Pattern.CASE_INSENSITIVE);

        @Override
        public boolean inScreenWhitelist(Class<?> cls) {
            if (cls == null) {
                return false;
            }
            return defaultScreenWhitelist.contains(cls.getName());
        }

        @Override
        public boolean inScreenBlacklist(Class<?> cls) {
            if (cls == null) {
                return false;
            }
            return defaultScreenBlacklist.contains(cls.getName());
        }

        @Override
        public boolean inInputWhitelist(Class<?> cls) {
            return false;
        }

        @Override
        public boolean inInputBlacklist(Class<?> cls) {
            return false;
        }

        @Override
        public void checkScreen(Class<?> cls) {
            // No-op
        }

        @Override
        public boolean getCheckCommandChat() {
            return true;
        }

        @Override
        public boolean getUseExperimental() {
            return true;
        }

        @Override
        public int getCheckInterval() {
            return 100;
        }

        @Override
        public boolean isEnabled() {
            // 使用YgdConfig中的配置
            if (com.yifei.ygd.config.ConfigManager.getConfig() != null && com.yifei.ygd.config.ConfigManager.getConfig().imBlocker != null) {
                return com.yifei.ygd.config.ConfigManager.getConfig().imBlocker.enabled;
            }
            return false;
        }
    }
}
