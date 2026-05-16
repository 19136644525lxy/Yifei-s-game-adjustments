package com.yifei.ygd.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Items;
import com.yifei.ygd.item.YgdToolMaterials;
import com.yifei.ygd.YgdMod;

public class YgdItems {
    // 硬化模板
    public static final Item HARDENED_TEMPLATE = new Item(new FabricItemSettings());
    // 硬化原石
    public static final Item HARDENED_ROUGH_STONE = new Item(new FabricItemSettings());
    // 硬化原石镐
    public static final Item HARDENED_ROUGH_STONE_PICKAXE = new PickaxeItem(
        YgdToolMaterials.HARDENED_ROUGH_STONE,
        8, 
        1.7F, 
        new FabricItemSettings().maxDamage(520377)
    );
    // 硬化原石斧
    public static final Item HARDENED_ROUGH_STONE_AXE = new AxeItem(
        YgdToolMaterials.HARDENED_ROUGH_STONE,
        12, 
        1.5F, 
        new FabricItemSettings().maxDamage(520377)
    );
    // 硬化原石铲
    public static final Item HARDENED_ROUGH_STONE_SHOVEL = new ShovelItem(
        YgdToolMaterials.HARDENED_ROUGH_STONE,
        8.5F, 
        1.5F, 
        new FabricItemSettings().maxDamage(520377)
    );
    // 硬化原石剑
    public static final Item HARDENED_ROUGH_STONE_SWORD = new SwordItem(
        YgdToolMaterials.HARDENED_ROUGH_STONE,
        10, 
        2.1F, 
        new FabricItemSettings().maxDamage(520377)
    );
    // 硬化原石锄
    public static final Item HARDENED_ROUGH_STONE_HOE = new HoeItem(
        YgdToolMaterials.HARDENED_ROUGH_STONE,
        3, 
        4.5F, 
        new FabricItemSettings().maxDamage(520377)
    );
    
    // 自定义创造物品栏
    public static final ItemGroup YGD_TAB = FabricItemGroup.builder()
        .icon(() -> new ItemStack(HARDENED_ROUGH_STONE))
        .displayName(Text.translatable("itemGroup.ygd.ygd_group"))
        .entries((context, entries) -> {
            entries.add(HARDENED_TEMPLATE);
            entries.add(HARDENED_ROUGH_STONE);
            entries.add(HARDENED_ROUGH_STONE_PICKAXE);
            entries.add(HARDENED_ROUGH_STONE_AXE);
            entries.add(HARDENED_ROUGH_STONE_SHOVEL);
            entries.add(HARDENED_ROUGH_STONE_SWORD);
            entries.add(HARDENED_ROUGH_STONE_HOE);
        })
        .build();
    
    private static Item register(Item item, String id) {
        Identifier itemId = new Identifier(YgdMod.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemId, item);
    }
    
    public static void register() {
        // 注册物品
        register(HARDENED_TEMPLATE, "hardened_template");
        register(HARDENED_ROUGH_STONE, "hardened_rough_stone");
        register(HARDENED_ROUGH_STONE_PICKAXE, "hardened_rough_stone_pickaxe");
        register(HARDENED_ROUGH_STONE_AXE, "hardened_rough_stone_axe");
        register(HARDENED_ROUGH_STONE_SHOVEL, "hardened_rough_stone_shovel");
        register(HARDENED_ROUGH_STONE_SWORD, "hardened_rough_stone_sword");
        register(HARDENED_ROUGH_STONE_HOE, "hardened_rough_stone_hoe");
        
        // 注册物品栏
        Registry.register(Registries.ITEM_GROUP, new Identifier(YgdMod.MOD_ID, "ygd_group"), YGD_TAB);
    }
}