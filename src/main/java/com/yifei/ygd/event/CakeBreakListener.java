package com.yifei.ygd.event;

import com.yifei.ygd.config.ConfigManager;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CakeBreakListener {
    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            // 检查配置是否启用蛋糕掉落功能
            if (!ConfigManager.getConfig().cake.enableCakeDrop) {
                return true;
            }
            
            // 检查是否是蛋糕方块
            if (state.getBlock() instanceof CakeBlock) {
                // 检查蛋糕是否完整
                if (state.get(CakeBlock.BITES) == 0) {
                    // 检查使用的工具
                    ItemStack stack = player.getMainHandStack();
                    if (stack.isEmpty() || stack.getItem() instanceof ShovelItem) {
                        // 掉落蛋糕物品
                        Block.dropStack(world, pos, new ItemStack(Items.CAKE));
                    }
                }
            }
            return true;
        });
    }
}