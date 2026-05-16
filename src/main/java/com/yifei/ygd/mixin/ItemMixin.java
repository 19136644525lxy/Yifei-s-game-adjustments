package com.yifei.ygd.mixin;

import com.yifei.ygd.config.ConfigManager;
import com.yifei.ygd.config.YgdConfig;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    private void onGetMaxCount(CallbackInfoReturnable<Integer> cir) {
        YgdConfig config = ConfigManager.getConfig();
        if (config != null && config.stackSize != null && config.stackSize.enabled) {
            // 启用最大堆叠数量修改，所有物品都可以堆叠到64个
            cir.setReturnValue(64);
        }
    }
}
