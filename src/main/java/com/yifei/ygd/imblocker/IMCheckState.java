package com.yifei.ygd.imblocker;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import com.yifei.ygd.imblocker.FabricScreenInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.WeakHashMap;
import java.util.function.BooleanSupplier;

public class IMCheckState {
    // process CLICK rules
    private static final ArrayList<BooleanSupplier> actives = new ArrayList<>();
    private static final EnumSet<IMState> state = EnumSet.copyOf(IMState.NONE);

    // check overall state
    private static void syncState() {
        // 检查功能是否启用
        if (!Config.INSTANCE.isEnabled()) {
            return; // 功能禁用时不处理
        }
        
        boolean im;
        if (state.contains(IMState.SCREEN_LIST_MASK)) im = state.contains(IMState.SCREEN_LIST);
        else if (state.contains(IMState.SPECIAL_MASK)) im = state.contains(IMState.SPECIAL);
        else im = (state.contains(IMState.TICK) ||
                   state.contains(IMState.NON_PRINTABLE) ||
                   state.contains(IMState.CLICK));
        
        // 对于创造模式搜索栏和JEI搜索栏，强制启用输入法
        Class<?> screenClass = new FabricScreenInfo().type();
        if (screenClass != null) {
            String className = screenClass.getName();
            if (className.contains("CreativeInventoryScreen") || className.contains("RecipeScreen")) {
                im = true;
            }
        }
        
        IMManager.setState(im);
    }

    // process SCREEN_LIST rules
    // notice that nonPrintable rule triggers at screen change, here.
    // notice that special rule triggers at screen change, here too.
    private static WeakReference<Object> lastScreen = new WeakReference<>(null);
    private static void checkScreenList(ScreenInfo screen) {
        if (screen.get() != lastScreen.get()) {
            state.add(IMState.NON_PRINTABLE_CHALLENGE_PENDING); // nonPrintable
            checkSpecialScreenChange(screen); // special
            lastScreen = new WeakReference<>(screen.get());
            if (Config.INSTANCE.inScreenWhitelist(screen.type())) {
                state.add(IMState.SCREEN_LIST);
                state.add(IMState.SCREEN_LIST_MASK);
            } else if (Config.INSTANCE.inScreenBlacklist(screen.type())) {
                state.remove(IMState.SCREEN_LIST);
                state.add(IMState.SCREEN_LIST_MASK);
            } else {
                state.remove(IMState.SCREEN_LIST_MASK);
                if (screen.get() != null) {
                    Config.INSTANCE.checkScreen(screen.type());
                }
            }
        }
    }

    // process SPECIAL rules
    private static void checkSpecial() { }

    private static void checkSpecialScreenChange(ScreenInfo screen) {
        if (screen.isChatScreen()) {
            // 简化实现，不再依赖 originalText 字段
            state.remove(IMState.SPECIAL_MASK);
        } else {
            state.remove(IMState.SPECIAL_MASK);
        }
    }

    // process TICK rules
    private enum InputClassRule { NOT_LISTED, WHITELIST, BLACKLIST }
    private static final WeakHashMap<Object, InputClassRule> lastInput = new WeakHashMap<>();
    private static InputClassRule checkInputClass(Object input) {
        if (!lastInput.containsKey(input)) {
            // 检查输入框是否属于创造模式搜索栏或JEI搜索栏
            String inputClassName = input.getClass().getName();
            if (inputClassName.contains("CreativeInventoryScreen") || inputClassName.contains("RecipeScreen") || 
                inputClassName.contains("TextField") || inputClassName.contains("EditBox")) {
                // 对于搜索框类型的输入，强制启用输入法
                state.add(IMState.TICK);
                state.remove(IMState.TICK_CHALLENGE);
                lastInput.put(input, InputClassRule.WHITELIST);
                return InputClassRule.WHITELIST;
            } else if (Config.INSTANCE.inInputWhitelist(input.getClass())) {
                state.remove(IMState.TICK);
                lastInput.put(input, InputClassRule.WHITELIST);
                return InputClassRule.WHITELIST;
            } else if (Config.INSTANCE.inInputBlacklist(input.getClass())) {
                state.remove(IMState.TICK);
                lastInput.put(input, InputClassRule.BLACKLIST);
                return InputClassRule.BLACKLIST;
            } else {
                lastInput.put(input, InputClassRule.NOT_LISTED);
                return InputClassRule.NOT_LISTED;
            }
        } else return lastInput.get(input);
    }

    private static void checkTick() {
        if (state.contains(IMState.TICK_CHALLENGE)) {
            state.remove(IMState.TICK_CHALLENGE);
            state.remove(IMState.TICK);
        } else if (state.contains(IMState.TICK)) {
            state.add(IMState.TICK_CHALLENGE);
        }
    }

    public static void captureTick(Object input, boolean canWrite) {
        // 检查输入框是否属于创造模式搜索栏或JEI搜索栏
        String inputClassName = input.getClass().getName();
        if (inputClassName.contains("CreativeInventoryScreen") || inputClassName.contains("RecipeScreen") || 
            inputClassName.contains("TextField") || inputClassName.contains("EditBox")) {
            // 对于搜索框类型的输入，强制启用输入法
            state.add(IMState.TICK);
            state.remove(IMState.TICK_CHALLENGE);
        } else if (canWrite && checkInputClass(input) != InputClassRule.BLACKLIST) {
            state.add(IMState.TICK);
            state.remove(IMState.TICK_CHALLENGE);
        }
    }

    // process NON_PRINTABLE rules
    public static final char nonPrintable = '\0';

    private static void checkNonPrintable(ScreenInfo screen) {
        if (Config.INSTANCE.getUseExperimental())
        {
            if (state.contains(IMState.NON_PRINTABLE_CHALLENGE)) {
                state.remove(IMState.NON_PRINTABLE_CHALLENGE);
                state.remove(IMState.NON_PRINTABLE);
            } else if (state.contains(IMState.NON_PRINTABLE_CHALLENGE_PENDING)) {
                state.add(IMState.NON_PRINTABLE_CHALLENGE);
                if (screen.get() != null) {
                    try {
                        screen.charTyped(nonPrintable, 0); // charTyped
                    } catch (Exception e) {
                        // Ignore exceptions
                    }
                }
                state.remove(IMState.NON_PRINTABLE_CHALLENGE_PENDING);
            }
        } else {
            state.remove(IMState.NON_PRINTABLE);
        }
    }

    public static boolean captureNonPrintable(Object tfw, char ch, boolean canWrite) {
        if (Config.INSTANCE.getUseExperimental()
                && checkInputClass(tfw) != InputClassRule.BLACKLIST
                && ch == nonPrintable
                && canWrite)
        {
            state.remove(IMState.NON_PRINTABLE_CHALLENGE);
            state.add(IMState.NON_PRINTABLE);
            return true;
        }
        return false;
    }

    public static void checkClick() {
        for (BooleanSupplier active: actives) {
            if (active.getAsBoolean()) {
                state.add(IMState.CLICK);
                break;
            }
        }
        actives.clear();
    }

    public static void captureClick(BooleanSupplier active) {
        actives.add(active);
    }

    // connect rules above
    private static long nextCheck = System.currentTimeMillis() + Config.INSTANCE.getCheckInterval();
    private static boolean scheduled = false;

    public static void clientTick(ScreenInfo screen) {
        checkScreenList(screen);
        checkSpecial();
        long now = System.currentTimeMillis();
        if (nextCheck < now && (scheduled || screen.get() == null)) {
            nextCheck = now + Config.INSTANCE.getCheckInterval();
            scheduled = false;
            checkTick();
        }
        checkNonPrintable(screen);
        checkClick();
        syncState();
    }

    public static void scheduleTickCheck() {
        scheduled = true;
    }

    public static void mouseEvent() {
        state.add(IMState.NON_PRINTABLE_CHALLENGE_PENDING);
        state.remove(IMState.CLICK);
    }

    private enum IMState {
        SCREEN_LIST, SPECIAL, TICK, NON_PRINTABLE, CLICK,
        SCREEN_LIST_MASK, SPECIAL_MASK,
        TICK_CHALLENGE, NON_PRINTABLE_CHALLENGE,
        NON_PRINTABLE_CHALLENGE_PENDING;
        public static final EnumSet<IMState> NONE = EnumSet.noneOf(IMState.class);
    }

    public interface ScreenInfo {
        Object get();
        boolean isChatScreen();
        Class<?> type();
        String defaultText() throws Throwable;
        void charTyped(char codePoint, int modifiers);
    }
}
