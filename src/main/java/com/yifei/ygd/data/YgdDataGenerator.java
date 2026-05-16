package com.yifei.ygd.data;

import com.yifei.ygd.YgdMod;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class YgdDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        // 注册数据生成器
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        
        // 注册各种数据生成器
        // 例如：配方、标签、 loot表等
        
        YgdMod.LOGGER.info("Data generation initialized!");
    }
}