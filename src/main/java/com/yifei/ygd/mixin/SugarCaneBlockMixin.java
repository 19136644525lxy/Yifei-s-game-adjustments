package com.yifei.ygd.mixin;

import com.yifei.ygd.config.ConfigManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {
    @Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
    public void canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        com.yifei.ygd.config.YgdConfig config = ConfigManager.getConfig();
        if (config == null) {
            return;
        }

        int heightLimit = config.sugarcane.heightLimit;
        int height = 1;
        BlockPos currentPos = pos.down();

        while (world.getBlockState(currentPos).getBlock() instanceof SugarCaneBlock) {
            height++;
            currentPos = currentPos.down();
        }

        if (height >= heightLimit) {
            cir.setReturnValue(false);
        }
    }
}
