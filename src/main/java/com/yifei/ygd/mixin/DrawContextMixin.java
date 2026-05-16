package com.yifei.ygd.mixin;

import com.yifei.ygd.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
    @Unique
    private static final float BASE_SCALE_FACTOR = 3.0f;
    
    @Shadow
    public abstract MatrixStack getMatrices();
    
    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    public void drawItemDurability(TextRenderer textRenderer, ItemStack stack, int x, int y, @Nullable String countOverride, CallbackInfo ci) {
        // 检查配置是否启用了耐久度显示
        if (ConfigManager.getConfig() == null || ConfigManager.getConfig().itemInfo == null || !ConfigManager.getConfig().itemInfo.showDurability) {
            return;
        }
        
        // 只对可损坏的物品显示耐久度
        if (!stack.isDamageable()) {
            return;
        }
        
        // 计算耐久度
        int maxDurability = stack.getMaxDamage();
        int currentDurability = maxDurability - stack.getDamage();
        
        float durabilityPercent = (float) currentDurability / maxDurability;
        
        // 选择颜色
        Formatting color;
        com.yifei.ygd.config.YgdConfig.DurabilityColorScheme colorScheme = ConfigManager.getConfig().itemInfo.durabilityColorScheme;
        
        if (durabilityPercent > 0.6) {
            switch (colorScheme) {
                case VANILLA:
                    color = Formatting.GREEN;
                    break;
                case BRIGHT:
                    color = Formatting.GREEN;
                    break;
                case PASTEL:
                    color = Formatting.GREEN;
                    break;
                case MONOCHROME:
                    color = Formatting.WHITE;
                    break;
                default:
                    color = Formatting.GREEN;
            }
        } else if (durabilityPercent > 0.3) {
            switch (colorScheme) {
                case VANILLA:
                    color = Formatting.YELLOW;
                    break;
                case BRIGHT:
                    color = Formatting.YELLOW;
                    break;
                case PASTEL:
                    color = Formatting.YELLOW;
                    break;
                case MONOCHROME:
                    color = Formatting.GRAY;
                    break;
                default:
                    color = Formatting.YELLOW;
            }
        } else {
            switch (colorScheme) {
                case VANILLA:
                    color = Formatting.RED;
                    break;
                case BRIGHT:
                    color = Formatting.RED;
                    break;
                case PASTEL:
                    color = Formatting.RED;
                    break;
                case MONOCHROME:
                    color = Formatting.BLACK;
                    break;
                default:
                    color = Formatting.RED;
            }
        }
        
        // 准备渲染
        MatrixStack matrices = getMatrices();
        matrices.push();
        
        // 计算自适应缩放因子
        float scaleFactor = calculateAdaptiveScaleFactor();
        
        // 缩放文本
        matrices.translate(0.0, 0.0, 200.0f);
        matrices.scale(1.0f / scaleFactor, 1.0f / scaleFactor, 1);
        
        // 创建文本，只显示当前耐久度
        Text durabilityText = Text.literal(String.valueOf(currentDurability)).formatted(color);
        
        // 计算文本位置（左上角显示）
        int xAdjusted = (int) (scaleFactor * x) + 2;
        int yAdjusted = (int) (scaleFactor * y) + 2;
        
        // 渲染文本
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        textRenderer.draw(
            durabilityText,
            xAdjusted,
            yAdjusted,
            0xFFFFFF,
            true,
            matrices.peek().getPositionMatrix(),
            immediate,
            TextRenderer.TextLayerType.NORMAL,
            0,
            LightmapTextureManager.MAX_LIGHT_COORDINATE
        );
        immediate.draw();
        
        matrices.pop();
    }
    
    @Unique
    private float calculateAdaptiveScaleFactor() {
        // 获取当前窗口信息
        Window window = MinecraftClient.getInstance().getWindow();
        if (window == null) {
            return BASE_SCALE_FACTOR;
        }
        
        // 获取窗口宽度
        int windowWidth = window.getScaledWidth();
        
        // 根据窗口宽度计算缩放因子
        // 基础宽度设为1920（全高清），根据实际宽度调整
        final int baseWidth = 1920;
        float widthRatio = (float) windowWidth / baseWidth;
        
        // 确保缩放因子在合理范围内
        float scaleFactor = BASE_SCALE_FACTOR * widthRatio;
        
        // 限制缩放范围，防止文字过小或过大
        scaleFactor = Math.max(2.0f, Math.min(4.0f, scaleFactor));
        
        return scaleFactor;
    }
}