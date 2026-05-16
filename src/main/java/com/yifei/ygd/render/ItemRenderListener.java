package com.yifei.ygd.render;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import com.yifei.ygd.item.YgdItems;
import com.yifei.ygd.config.ConfigManager;

public class ItemRenderListener {
    public static void register() {
        // 为硬化原石及其衍生工具注册自定义渲染器
        registerItemRenderer(YgdItems.HARDENED_ROUGH_STONE);
        registerItemRenderer(YgdItems.HARDENED_ROUGH_STONE_PICKAXE);
        registerItemRenderer(YgdItems.HARDENED_ROUGH_STONE_AXE);
        registerItemRenderer(YgdItems.HARDENED_ROUGH_STONE_SHOVEL);
        registerItemRenderer(YgdItems.HARDENED_ROUGH_STONE_SWORD);
        registerItemRenderer(YgdItems.HARDENED_ROUGH_STONE_HOE);
        registerItemRenderer(YgdItems.HARDENED_TEMPLATE);
    }
    
    private static void registerItemRenderer(Item item) {
        BuiltinItemRendererRegistry.INSTANCE.register(item, (ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) -> {
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            BakedModel model = itemRenderer.getModel(stack, null, null, 0);
            
            // 检查是否启用了性能优化
            boolean enablePerformanceOpt = false;
            if (ConfigManager.getConfig() != null && ConfigManager.getConfig().infoDisplay != null) {
                enablePerformanceOpt = ConfigManager.getConfig().infoDisplay.enabled;
            }
            
            if (enablePerformanceOpt) {
                // 使用优化的渲染方式
                // 这里我们使用默认渲染，但可以在未来添加更高级的优化
                itemRenderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, model);
            } else {
                // 使用默认渲染
                itemRenderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, model);
            }
        });
    }
}
