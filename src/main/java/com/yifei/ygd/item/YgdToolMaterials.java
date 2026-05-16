package com.yifei.ygd.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.item.Items;

public class YgdToolMaterials {
    public static final ToolMaterial HARDENED_ROUGH_STONE = new ToolMaterial() {
        @Override
        public int getDurability() {
            return 520377; // 与工具中的maxDamage保持一致
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 12.0F; // 比下界合金的9.0更高，提高挖掘速度
        }

        @Override
        public float getAttackDamage() {
            return 4.0F; // 基础攻击伤害
        }

        @Override
        public int getMiningLevel() {
            return 4; // 与下界合金相同，可以挖掘黑曜石等方块
        }

        @Override
        public int getEnchantability() {
            return 15; // 与下界合金相同的附魔能力
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(YgdItems.HARDENED_ROUGH_STONE); // 使用硬化原石作为修复材料
        }
    };
}
