package com.yifei.ygd.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;



public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            return new net.minecraft.client.gui.screen.Screen(Text.literal("YGD Config")) {
                private static final Identifier BACKGROUND_TEXTURE = new Identifier("ygd", "textures/gui/wendi.png");
                
                @Override
                protected void init() {
                    super.init();
                    
                    this.addDrawableChild(new CustomButtonWidget(
                        20, this.height - 100, 200, 20,
                        Text.literal("通用配置"),
                        button -> this.client.setScreen(AutoConfig.getConfigScreen(YgdConfig.class, this).get())
                    ));
                    
                    this.addDrawableChild(new CustomButtonWidget(
                        20, this.height - 60, 200, 20,
                        Text.literal("扫地娘配置"),
                        button -> this.client.setScreen(AutoConfig.getConfigScreen(SweepConfig.class, this).get())
                    ));
                    
                    this.addDrawableChild(new CustomButtonWidget(
                        20, this.height - 20, 200, 20,
                        Text.literal("返回"),
                        button -> this.client.setScreen(parent)
                    ));
                }
                
                @Override
                public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
                    context.drawTexture(BACKGROUND_TEXTURE, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
                    super.render(context, mouseX, mouseY, delta);
                }
            };
        };
    }
    
    private static class CustomButtonWidget extends ButtonWidget {
        public CustomButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
            super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
        }
        
        @Override
        public void renderButton(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
            boolean hovered = mouseX >= this.getX() && mouseX < this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY < this.getY() + this.getHeight();
            
            context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0x80000000);
            context.drawBorder(this.getX(), this.getY(), this.getWidth(), this.getHeight(), hovered ? 0xFFFFFF : 0xAAAAAA);
            
            int color = this.active ? 16777215 : 10526880;
            net.minecraft.client.MinecraftClient client = net.minecraft.client.MinecraftClient.getInstance();
            context.drawCenteredTextWithShadow(client.textRenderer, this.getMessage(), this.getX() + this.getWidth() / 2, this.getY() + (this.getHeight() - 8) / 2, color);
        }
    }
    

}