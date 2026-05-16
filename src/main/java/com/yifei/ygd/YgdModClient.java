package com.yifei.ygd;

import net.fabricmc.api.ClientModInitializer;
import com.yifei.ygd.render.ItemRenderListener;

public class YgdModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 注册物品渲染监听器
        ItemRenderListener.register();
        YgdMod.LOGGER.info("YgdMod client initialized with rendering optimizations!");
    }
}
