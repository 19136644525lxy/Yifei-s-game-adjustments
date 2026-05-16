package com.yifei.ygd.event;

import com.yifei.ygd.config.ConfigManager;
import com.yifei.ygd.item.YgdItems;
import net.fabricmc.fabric.api.event.client.ItemTooltipCallback;
import net.minecraft.item.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.ArmorMaterials;
import java.util.ArrayList;
import java.util.List;

public class ItemTooltipListener {
    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            // 检查配置
            boolean showDurability = false;
            if (ConfigManager.getConfig() != null && ConfigManager.getConfig().itemInfo != null) {
                showDurability = ConfigManager.getConfig().itemInfo.showDurability;
            }
            
            // 只保留耐久度显示，使用原版的物品信息显示
            // 这里不清除原版属性信息，只添加耐久度显示
            
            // 显示耐久度信息
            if (showDurability && stack.isDamageable()) {
                int maxDamage = stack.getMaxDamage();
                int currentDamage = stack.getDamage();
                int remainingDamage = maxDamage - currentDamage;
                
                // 计算耐久度百分比
                float durabilityPercentage = (float) remainingDamage / maxDamage * 100;
                
                // 根据耐久度百分比设置颜色
                Formatting color;
                if (durabilityPercentage > 60) {
                    color = Formatting.GREEN;
                } else if (durabilityPercentage > 30) {
                    color = Formatting.YELLOW;
                } else {
                    color = Formatting.RED;
                }
                
                lines.add(Text.literal("耐久度: " + remainingDamage + "/" + maxDamage + " (" + String.format("%.1f", durabilityPercentage) + "%)").formatted(color));
            }
        });
    }
}