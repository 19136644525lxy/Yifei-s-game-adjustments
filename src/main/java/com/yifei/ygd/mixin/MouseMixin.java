package com.yifei.ygd.mixin;

import com.yifei.ygd.config.ConfigManager;
import com.yifei.ygd.game.ZoomSystem;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(
        method = "onMouseScroll",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onMouseScroll(long window, double mouseX, double mouseY, CallbackInfo ci) {
        if (ZoomSystem.getInstance().isZooming() && ConfigManager.getConfig() != null && ConfigManager.getConfig().zoom != null && ConfigManager.getConfig().zoom.scrollZoom) {
            if (mouseY != 0) {
                ZoomSystem.getInstance().mouseZoom(mouseY);
                ci.cancel();
            }
        }
    }
}
