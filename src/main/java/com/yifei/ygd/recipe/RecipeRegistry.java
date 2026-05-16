package com.yifei.ygd.recipe;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecipeRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeRegistry.class);
    
    // 示例：注册自定义物品（如果需要）
    // public static final Item CUSTOM_ITEM = registerItem("custom_item", new Item(new FabricItemSettings()));
    
    /**
     * 注册物品
     */
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier("ygd", name), item);
    }
    
    /**
     * 初始化配方注册
     */
    public static void init() {
        // 配方注册通过JSON文件完成
        LOGGER.info("RecipeRegistry initialized!");
    }
}