package com.yifei.ygd.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.AnvilBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import com.yifei.ygd.item.YgdItems;

public class AnvilListener {
    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockState state = world.getBlockState(hitResult.getBlockPos());
            if (state.getBlock() instanceof AnvilBlock) {
                return onUseAnvil(player, world, hand, hitResult);
            }
            return ActionResult.PASS;
        });
    }
    
    private static ActionResult onUseAnvil(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        // 检查玩家是否手持硬化原石衍生工具
        ItemStack heldItem = player.getStackInHand(hand);
        if (isHardenedTool(heldItem)) {
            if (!world.isClient) {
                // 打开自定义铁砧界面
                player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, playerEntity) -> {
                    return new CustomAnvilScreenHandler(syncId, inventory);
                }, Text.translatable("container.ygd.anvil")));
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        } else {
            // 使用原版铁砧功能
            return ActionResult.PASS;
        }
    }
    
    // 检查是否是硬化原石衍生工具
    private static boolean isHardenedTool(ItemStack stack) {
        Item item = stack.getItem();
        return item == YgdItems.HARDENED_ROUGH_STONE_PICKAXE ||
               item == YgdItems.HARDENED_ROUGH_STONE_AXE ||
               item == YgdItems.HARDENED_ROUGH_STONE_SHOVEL ||
               item == YgdItems.HARDENED_ROUGH_STONE_SWORD ||
               item == YgdItems.HARDENED_ROUGH_STONE_HOE;
    }
    
    // 自定义铁砧屏幕处理器
    private static class CustomAnvilScreenHandler extends ScreenHandler {
        private final Inventory input = new SimpleInventory(2);
        private final Inventory output = new SimpleInventory(1);
        
        public CustomAnvilScreenHandler(int syncId, Inventory playerInventory) {
            super(ScreenHandlerType.ANVIL, syncId);
            
            // 添加输入槽
            this.addSlot(new Slot(input, 0, 27, 47) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    // 允许放入硬化原石衍生工具
                    return isHardenedTool(stack);
                }
            });
            
            this.addSlot(new Slot(input, 1, 76, 47) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    // 允许放入硬化原石
                    return stack.getItem() == YgdItems.HARDENED_ROUGH_STONE;
                }
            });
            
            // 添加输出槽
            this.addSlot(new Slot(output, 0, 134, 47) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return false;
                }
                
                @Override
                public void onTakeItem(PlayerEntity player, ItemStack stack) {
                    // 取出物品时消耗材料
                    ItemStack toolStack = input.getStack(0);
                    ItemStack stoneStack = input.getStack(1);
                    
                    if (!toolStack.isEmpty()) {
                        // 消耗原工具
                        input.setStack(0, ItemStack.EMPTY);
                    }
                    if (!stoneStack.isEmpty() && stoneStack.getCount() > 0) {
                        stoneStack.decrement(1);
                    }
                    
                    // 清除输出槽，防止物品复制
                    output.setStack(0, ItemStack.EMPTY);
                    // 更新输出
                    updateOutput();
                }
            });
            
            // 添加玩家物品栏槽位
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
                }
            }
            
            // 添加玩家快捷栏槽位
            for (int i = 0; i < 9; ++i) {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
            }
            
            // 初始更新输出
            updateOutput();
        }
        
        @Override
        public void onContentChanged(Inventory inventory) {
            super.onContentChanged(inventory);
            if (inventory == input) {
                updateOutput();
            }
        }
        
        private void updateOutput() {
            ItemStack toolStack = input.getStack(0);
            ItemStack stoneStack = input.getStack(1);
            
            // 检查是否有足够的材料
            if (!toolStack.isEmpty() && 
                !stoneStack.isEmpty() &&
                isHardenedTool(toolStack) && 
                stoneStack.getItem() == YgdItems.HARDENED_ROUGH_STONE) {
                ItemStack result = toolStack.copy();
                NbtCompound nbt = result.getOrCreateNbt();
                nbt.putBoolean("Unbreakable", true);
                output.setStack(0, result);
            } else {
                output.setStack(0, ItemStack.EMPTY);
            }
        }
        
        @Override
        public boolean canUse(PlayerEntity player) {
            return true;
        }
        
        @Override
        public void onClosed(PlayerEntity player) {
            super.onClosed(player);
            // 当界面关闭时，将输入槽中的物品返回给玩家
            for (int i = 0; i < input.size(); ++i) {
                ItemStack stack = input.getStack(i);
                if (!stack.isEmpty()) {
                    player.getInventory().offerOrDrop(stack);
                }
            }
            // 清空输入槽
            input.clear();
            // 清空输出槽
            output.clear();
        }
        
        @Override
        public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
            super.onSlotClick(slotIndex, button, actionType, player);
            updateOutput();
        }
        
        @Override
        public ItemStack quickMove(PlayerEntity player, int slotIndex) {
            ItemStack itemStack = ItemStack.EMPTY;
            Slot slot = this.slots.get(slotIndex);
            if (slot != null && slot.hasStack()) {
                ItemStack itemStack2 = slot.getStack();
                itemStack = itemStack2.copy();
                if (slotIndex == 2) {
                    if (!this.insertItem(itemStack2, 3, 39, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickTransfer(itemStack2, itemStack);
                } else if (slotIndex >= 3 && slotIndex < 39) {
                    if (isHardenedTool(itemStack2)) {
                        if (!this.insertItem(itemStack2, 0, 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (itemStack2.getItem() == YgdItems.HARDENED_ROUGH_STONE) {
                        if (!this.insertItem(itemStack2, 1, 2, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(itemStack2, 3, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(itemStack2, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
                if (itemStack2.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                } else {
                    slot.markDirty();
                }
                if (itemStack2.getCount() == itemStack.getCount()) {
                    return ItemStack.EMPTY;
                }
                slot.onTakeItem(player, itemStack2);
            }
            return itemStack;
        }
    }
}
