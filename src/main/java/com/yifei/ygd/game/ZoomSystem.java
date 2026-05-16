package com.yifei.ygd.game;

import com.yifei.ygd.YgdMod;
import com.yifei.ygd.config.ConfigManager;
import com.yifei.ygd.config.YgdConfig;
import com.yifei.ygd.config.YgdConfig.TransitionType;
import com.yifei.ygd.config.YgdConfig.ZoomKeyBehaviour;
import com.yifei.ygd.keybinding.KeyBindings;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class ZoomSystem {
    private static final ZoomSystem INSTANCE = new ZoomSystem();
    
    private boolean isZooming = false;
    private double initialInterpolation = 0.0;
    private double prevInitialInterpolation = 0.0;
    private double scrollInterpolation = 0.0;
    private double prevScrollInterpolation = 0.0;
    private int scrollSteps = 0;
    private int lastScrollTier = 0;
    private boolean zoomingLastTick = false;
    private int savedScrollSteps = 0; // 用于保留缩放步骤
    
    private ZoomSystem() {
    }
    
    // 线性插值方法
    private double lerp(double delta, double start, double end) {
        return start + delta * (end - start);
    }
    
    // 限制值在指定范围内
    private int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
    
    // 限制值在指定范围内（双精度）
    private double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
    
    public static ZoomSystem getInstance() {
        return INSTANCE;
    }
    
    public void initialize() {
        YgdMod.LOGGER.info("Initializing zoom system...");
        
        // 注册tick事件，用于更新缩放状态
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        
        YgdMod.LOGGER.info("Zoom system initialized!");
    }
    
    private void tick(MinecraftClient client) {
        YgdConfig config = ConfigManager.getConfig();
        if (config == null || config.zoom == null || !config.zoom.enabled) {
            reset();
            return;
        }
        
        boolean prevZooming = isZooming;
        
        // 处理缩放按键行为
        if (config.zoom.zoomKeyBehaviour == ZoomKeyBehaviour.HOLD) {
            isZooming = KeyBindings.ZOOM.isPressed();
        } else {
            if (KeyBindings.ZOOM.wasPressed()) {
                isZooming = !isZooming;
            }
        }
        
        // 更新初始缩放插值
        updateInitialInterpolation(prevZooming);
        
        // 更新滚动缩放插值
        updateScrollInterpolation();
        
        zoomingLastTick = isZooming;
    }
    
    private void updateInitialInterpolation(boolean prevZooming) {
        YgdConfig config = ConfigManager.getConfig();
        if (config == null || config.zoom == null) {
            return;
        }
        
        double targetZoom = isZooming ? 1.0 : 0.0;
        prevInitialInterpolation = initialInterpolation;
        
        // 计算缩放速度，确保动画时间符合配置
        double zoomSpeed;
        if (isZooming) {
            // 放大速度
            zoomSpeed = 1.0 / (config.zoom.zoomInTime / 16.67);
        } else {
            // 缩小速度
            zoomSpeed = 1.0 / (config.zoom.zoomOutTime / 16.67);
        }
        
        // 限制速度，避免动画过快
        zoomSpeed = clamp(zoomSpeed, 0.01, 0.5);
        
        // 线性插值
        initialInterpolation = lerp(zoomSpeed, initialInterpolation, targetZoom);
        
        // 处理缩放步骤的保留
        if (!isZooming && prevZooming) {
            if (config.zoom.retainZoomSteps) {
                savedScrollSteps = scrollSteps;
            } else {
                scrollSteps = 0;
            }
        } else if (isZooming && !prevZooming) {
            if (config.zoom.retainZoomSteps) {
                scrollSteps = savedScrollSteps;
            }
        }
    }
    

    
    private double applyTransition(double progress, TransitionType type) {
        switch (type) {
            case LINEAR:
                return progress;
            case EASE_IN:
                return progress * progress;
            case EASE_OUT:
                return 1.0 - Math.pow(1.0 - progress, 2);
            case EASE_IN_OUT:
                return progress < 0.5 ? 2 * progress * progress : 1.0 - Math.pow(-2 * progress + 2, 2) / 2;
            case EASE_OUT_EXP:
                return 1.0 - Math.pow(2, -10 * progress);
            default:
                return progress;
        }
    }
    
    private void updateScrollInterpolation() {
        YgdConfig config = ConfigManager.getConfig();
        if (config == null || config.zoom == null) {
            return;
        }
        
        int maxScrollTiers = config.zoom.scrollStepCount;
        double targetZoom = scrollSteps / (double) maxScrollTiers;
        
        if (config.zoom.linearLikeSteps) {
            double curvature = 0.3;
            double exp = 1 / (1 - curvature);
            targetZoom = 2 * (Math.pow(targetZoom, exp) / (Math.pow(targetZoom, exp) + Math.pow(2 - targetZoom, exp)));
        }
        
        prevScrollInterpolation = scrollInterpolation;
        double scrollSpeed = config.zoom.scrollZoomSmoothness / 100.0;
        scrollInterpolation = lerp(scrollSpeed, scrollInterpolation, targetZoom);
        
        lastScrollTier = scrollSteps;
    }
    
    public double getZoomDivisor(float tickDelta) {
        YgdConfig config = ConfigManager.getConfig();
        if (config == null || config.zoom == null || !config.zoom.enabled) {
            return 1.0;
        }
        
        double initialMultiplier = lerp(
            lerp(tickDelta, prevInitialInterpolation, initialInterpolation),
            1.0,
            1.0 / config.zoom.initialZoom
        );
        
        int maxScrollTiers = config.zoom.scrollStepCount;
        double scrollDivisor = lerp(
            lerp(tickDelta, prevScrollInterpolation, scrollInterpolation),
            0.0,
            maxScrollTiers * (config.zoom.zoomPerStep / 100.0)
        );
        
        double zoomDivisor = 1.0 / initialMultiplier + scrollDivisor;
        
        return zoomDivisor;
    }
    
    public void mouseZoom(double mouseDelta) {
        YgdConfig config = ConfigManager.getConfig();
        if (config == null || config.zoom == null || !config.zoom.enabled || !config.zoom.scrollZoom) {
            return;
        }
        
        if (mouseDelta > 0) {
            scrollSteps++;
        } else if (mouseDelta < 0) {
            scrollSteps--;
        }
        
        int maxScrollTiers = config.zoom.scrollStepCount;
        scrollSteps = clamp(scrollSteps, 0, maxScrollTiers);
    }
    
    public double getPreviousZoomDivisor() {
        return getZoomDivisor(1.0f);
    }
    
    public void reset() {
        isZooming = false;
        initialInterpolation = 0.0;
        prevInitialInterpolation = 0.0;
        scrollInterpolation = 0.0;
        prevScrollInterpolation = 0.0;
        scrollSteps = 0;
        savedScrollSteps = 0;
        lastScrollTier = 0;
        zoomingLastTick = false;
    }
    
    public boolean isZooming() {
        return isZooming;
    }
    
    public int getScrollSteps() {
        return scrollSteps;
    }
}