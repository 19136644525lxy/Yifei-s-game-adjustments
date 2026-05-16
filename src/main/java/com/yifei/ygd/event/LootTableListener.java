package com.yifei.ygd.event;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import com.yifei.ygd.item.YgdItems;

public class LootTableListener {
    public static void register() {
        // 监听战利品表修改事件
        LootTableEvents.MODIFY.register((resourceManager, manager, id, supplier, setter) -> {
            // 处理末影龙战利品表
            if (id.equals(new Identifier("minecraft", "entities/ender_dragon"))) {
                // 添加硬化原石掉落
                supplier.pool(LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .with(ItemEntry.builder(YgdItems.HARDENED_ROUGH_STONE)
                        .weight(5) // 5% 概率
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2)))
                    )
                    .with(EmptyEntry.builder() // 空条目
                        .weight(95) // 95% 概率不掉落
                    )
                );
            }
            
            // 处理末地城宝藏箱战利品表
            if (id.equals(new Identifier("minecraft", "chests/end_city_treasure"))) {
                // 添加硬化原石掉落
                supplier.pool(LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .with(ItemEntry.builder(YgdItems.HARDENED_ROUGH_STONE)
                        .weight(15) // 15% 概率
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3)))
                    )
                    .with(EmptyEntry.builder() // 空条目
                        .weight(85) // 85% 概率不掉落
                    )
                );
                
                // 添加硬化模板掉落
                supplier.pool(LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .with(ItemEntry.builder(YgdItems.HARDENED_TEMPLATE)
                        .weight(10) // 10% 概率
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1)))
                    )
                    .with(EmptyEntry.builder() // 空条目
                        .weight(90) // 90% 概率不掉落
                    )
                );
            }
        });
    }
}
