package com.yifei.ygd.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.SmithingTableBlock;
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

public class SmithingTableListener {
    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockState state = world.getBlockState(hitResult.getBlockPos());
            if (state.getBlock() instanceof SmithingTableBlock) {
                return onUseSmithingTable(player, world, hand, hitResult);
            }
            return ActionResult.PASS;
        });
    }
    
    private static ActionResult onUseSmithingTable(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        // 检查玩家是否手持硬化模板或硬化原石
        ItemStack heldItem = player.getStackInHand(hand);
        if (heldItem.getItem() == YgdItems.HARDENED_TEMPLATE || heldItem.getItem() == YgdItems.HARDENED_ROUGH_STONE) {
            if (!world.isClient) {
                // 打开自定义锻造台界面
                player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, playerEntity) -> {
                    return new CustomSmithingScreenHandler(syncId, inventory);
                }, Text.translatable("container.ygd.smithing")));
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        } else {
            // 使用原版锻造台功能
            return ActionResult.PASS;
        }
    }
    
    // 自定义锻造台屏幕处理器
    private static class CustomSmithingScreenHandler extends ScreenHandler {
        private final Inventory input = new SimpleInventory(3);
        private final Inventory output = new SimpleInventory(1);
        
        public CustomSmithingScreenHandler(int syncId, Inventory playerInventory) {
            super(ScreenHandlerType.SMITHING, syncId);
            
            // 添加输入槽
            this.addSlot(new Slot(input, 0, 30, 40) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    // 允许放入任何物品，包括硬化模板
                    return true;
                }
            });
            
            this.addSlot(new Slot(input, 1, 76, 40) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    // 允许放入任何可损坏物品
                    return isDamageableItem(stack);
                }
            });
            
            this.addSlot(new Slot(input, 2, 122, 40) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    // 允许放入任何物品，包括硬化原石
                    return true;
                }
            });
            
            // 添加输出槽
            this.addSlot(new Slot(output, 0, 76, 70) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return false;
                }
                
                @Override
                public void onTakeItem(PlayerEntity player, ItemStack stack) {
                    // 取出物品时消耗材料和原物品
                    ItemStack templateStack = input.getStack(0);
                    ItemStack targetStack = input.getStack(1);
                    ItemStack materialStack = input.getStack(2);
                    
                    if (!templateStack.isEmpty() && templateStack.getCount() > 0) {
                        templateStack.decrement(1);
                    }
                    if (!targetStack.isEmpty()) {
                        // 消耗原物品
                        input.setStack(1, ItemStack.EMPTY);
                    }
                    if (!materialStack.isEmpty() && materialStack.getCount() > 0) {
                        materialStack.decrement(1);
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
                    this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 126 + i * 18));
                }
            }
            
            // 添加玩家快捷栏槽位
            for (int i = 0; i < 9; ++i) {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 184));
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
            ItemStack templateStack = input.getStack(0);
            ItemStack targetStack = input.getStack(1);
            ItemStack materialStack = input.getStack(2);
            
            // 检查是否有足够的材料
            if (!templateStack.isEmpty() && 
                !targetStack.isEmpty() && 
                !materialStack.isEmpty() &&
                templateStack.getItem() == YgdItems.HARDENED_TEMPLATE && 
                materialStack.getItem() == YgdItems.HARDENED_ROUGH_STONE && 
                isDamageableItem(targetStack)) {
                ItemStack result = targetStack.copy();
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
                if (slotIndex == 3) {
                    if (!this.insertItem(itemStack2, 4, 40, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickTransfer(itemStack2, itemStack);
                } else if (slotIndex >= 4 && slotIndex < 40) {
                    if (SmithingTableListener.isDamageableItem(itemStack2)) {
                        if (!this.insertItem(itemStack2, 1, 2, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(itemStack2, 0, 1, false) && !this.insertItem(itemStack2, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(itemStack2, 4, 40, false)) {
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
    
    private static boolean isDamageableItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ToolItem || 
               item instanceof SwordItem || 
               item instanceof ArmorItem ||
               item instanceof ElytraItem ||
               item instanceof TridentItem ||
               item instanceof ShieldItem ||
               stack.isDamageable();
    }
}
