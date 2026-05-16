package com.yifei.ygd.imblocker;

import com.sun.jna.Platform;

public final class IMManager {
    public interface PlatformIMManager {
        void makeOn();

        void makeOff();

        void setState(boolean on);

        void syncState();

        boolean getState();
    }

    private static final PlatformIMManager INSTANCE = getInstance();

    private static PlatformIMManager getInstance() {
        if (Platform.isWindows())
            return new IMManagerWindows();
        if (Platform.isMac())
            return new IMManagerMac();
        throw new UnsupportedOperationException("Unsupported platform");
    }

    public static void makeOn() {
        INSTANCE.makeOn();
    }

    public static void makeOff() {
        INSTANCE.makeOff();
    }

    public static void setState(boolean on) {
        // 默认启用输入法，只在特定情况下禁用
        // 对于创造模式搜索栏和JEI搜索栏，强制启用输入法
        INSTANCE.setState(on);
    }

    public static void syncState() {
        INSTANCE.syncState();
    }

    public static boolean getState() {
        return INSTANCE.getState();
    }
}
