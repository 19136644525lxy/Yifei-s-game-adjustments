package com.yifei.ygd.mixin;

import com.yifei.ygd.config.ConfigManager;
import com.yifei.ygd.game.ZoomSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(
        method = "render",
        at = @At("TAIL")
    )
    private void renderZoomInfo(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (ZoomSystem.getInstance().isZooming() && ConfigManager.getConfig() != null && ConfigManager.getConfig().zoom != null && ConfigManager.getConfig().zoom.enabled) {
            double zoomDivisor = ZoomSystem.getInstance().getZoomDivisor(tickDelta);
            double zoomLevel = 1.0 / zoomDivisor;
            // 转换为整数，范围限制在0-100
            int clampedZoomLevel = (int) Math.max(0, Math.min(100, zoomLevel * 100));
            String zoomText = String.format("%s: %d", Text.translatable("text.ygd.zoom.level").getString(), clampedZoomLevel);
            Text text = Text.literal(zoomText);
            int screenWidth = context.getScaledWindowWidth();
            int screenHeight = context.getScaledWindowHeight();
            int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(text);
            
            // 生成彩虹色
            long time = System.currentTimeMillis();
            float hue = (time % 10000) / 10000.0f;
            int color = getRainbowColor(hue);
            
            context.drawText(
                MinecraftClient.getInstance().textRenderer,
                text,
                (screenWidth - textWidth) / 2,
                screenHeight - 80,
                color,
                true
            );
        }
        
        // 显示扫地机倒计时通知
        renderSweepInfo(context);
    }
    
    private void renderSweepInfo(DrawContext context) {
        com.yifei.ygd.game.SweepSystem sweepSystem = com.yifei.ygd.game.SweepSystem.getInstance();
        if (sweepSystem.isCountingDown() && ConfigManager.getSweepConfig() != null && ConfigManager.getSweepConfig().common != null) {
            int countdown = sweepSystem.getCurrentCountdown();
            String sweepText = ConfigManager.getSweepConfig().common.sweepNotice.replace("{0}", String.valueOf(countdown));
            Text text = Text.literal(sweepText);
            int screenWidth = context.getScaledWindowWidth();
            int screenHeight = context.getScaledWindowHeight();
            int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(text);
            
            context.drawText(
                MinecraftClient.getInstance().textRenderer,
                text,
                (screenWidth - textWidth) / 2,
                screenHeight - 120,
                0xFFFF00, // 黄色
                true
            );
        }
    }
    
    // 生成彩虹色
    private int getRainbowColor(float hue) {
        int r = (int) (Math.sin(hue * Math.PI * 2 + 0) * 127 + 128);
        int g = (int) (Math.sin(hue * Math.PI * 2 + 2) * 127 + 128);
        int b = (int) (Math.sin(hue * Math.PI * 2 + 4) * 127 + 128);
        return (r << 16) | (g << 8) | b;
    }
}