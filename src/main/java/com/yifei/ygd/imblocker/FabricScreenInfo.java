package com.yifei.ygd.imblocker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;

public class FabricScreenInfo implements IMCheckState.ScreenInfo {

    private Screen getCurrentScreen() {
        return MinecraftClient.getInstance().currentScreen;
    }

    @Override
    public Object get() {
        return getCurrentScreen();
    }

    @Override
    public boolean isChatScreen() {
        return getCurrentScreen() instanceof ChatScreen;
    }

    @Override
    public Class<?> type() {
        Screen screen = getCurrentScreen();
        return screen == null ? null : screen.getClass();
    }

    @Override
    public String defaultText() throws Throwable {
        return "";
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {
        Screen screen = getCurrentScreen();
        if (screen != null) {
            screen.charTyped(codePoint, modifiers);
        }
    }
}
