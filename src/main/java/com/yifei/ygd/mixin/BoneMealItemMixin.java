package com.yifei.ygd.mixin;

import com.yifei.ygd.config.ConfigManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        com.yifei.ygd.config.YgdConfig config = ConfigManager.getConfig();
        if (config == null || !config.sugarcane.enableSugarcaneBoneMeal) {
            return;
        }

        BlockPos pos = context.getBlockPos();
        BlockState state = context.getWorld().getBlockState(pos);

        if (state.getBlock() instanceof SugarCaneBlock) {
            if (context.getWorld() instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) context.getWorld();
                int height = 1;
                BlockPos currentPos = pos.up();

                while (context.getWorld().getBlockState(currentPos).getBlock() instanceof SugarCaneBlock) {
                    height++;
                    currentPos = currentPos.up();
                }

                int heightLimit = config.sugarcane.heightLimit;
                if (height < heightLimit && context.getWorld().isAir(currentPos)) {
                    serverWorld.spawnParticles(
                        net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER,
                        pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                        10, 0.5, 0.5, 0.5, 0.1
                    );

                    context.getWorld().setBlockState(currentPos, state.getBlock().getDefaultState());

                    ItemStack stack = context.getStack();
                    if (!context.getPlayer().isCreative()) {
                        stack.decrement(1);
                    }

                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }
}