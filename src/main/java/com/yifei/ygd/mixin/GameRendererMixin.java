package com.yifei.ygd.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yifei.ygd.game.ZoomSystem;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyReturnValue(
        method = "getFov",
        at = @At("RETURN")
    )
    private double modifyFovWithZoom(double fov, @Local(argsOnly = true) float tickDelta) {
        return fov / ZoomSystem.getInstance().getZoomDivisor(tickDelta);
    }
}