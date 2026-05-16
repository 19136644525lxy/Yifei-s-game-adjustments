package com.yifei.ygd.game;

import com.yifei.ygd.config.ConfigManager;
import com.yifei.ygd.config.YgdConfig;
import com.yifei.ygd.YgdMod;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InfoDisplayManager {
    private static final InfoDisplayManager INSTANCE = new InfoDisplayManager();
    private int tickCounter = 0;
    private int fps = 0;
    private int tps = 20;
    private double mspt = 50.0;
    private int frameCount = 0;
    private long lastFrameTime = System.currentTimeMillis();

    private InfoDisplayManager() {
    }

    public static InfoDisplayManager getInstance() {
        return INSTANCE;
    }

    public void initialize() {
        YgdMod.LOGGER.info("Initializing info display manager...");

        HudRenderCallback.EVENT.register(this::renderHud);

        YgdMod.LOGGER.info("Info display manager initialized!");
    }

    private void renderHud(DrawContext context, float tickDelta) {
        YgdConfig config = ConfigManager.getConfig();
        if (config == null || !config.infoDisplay.enabled) {
            return;
        }

        updateFps();

        tickCounter++;
        if (tickCounter >= config.infoDisplay.updateInterval) {
            updateTps();
            tickCounter = 0;
        }

        renderInfo(context, tickDelta, config);
    }

    private void updateFps() {
        frameCount++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= 1000) {
            fps = frameCount;
            frameCount = 0;
            lastFrameTime = currentTime;
        }
    }

    private long lastTickTime = System.currentTimeMillis();
    private int tickCount = 0;
    private long tickTimeSum = 0;

    private void updateTps() {
        MinecraftServer server = YgdMod.getServer();
        if (server != null) {
            long currentTime = System.currentTimeMillis();
            tickCount++;

            if (tickCount >= 60) {
                long elapsedTime = currentTime - lastTickTime;

                double calculatedTps = (60.0 * 1000.0) / elapsedTime;
                tps = (int) Math.min(20, Math.round(calculatedTps));

                mspt = elapsedTime / 60.0;

                tickCount = 0;
                lastTickTime = currentTime;
            }
        } else {
            tps = 20;
            mspt = 50.0;
        }
    }

    private void renderInfo(DrawContext context, float tickDelta, YgdConfig config) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        int baseX = config.infoDisplay.horizontalOffset;
        int baseY = config.infoDisplay.verticalOffset;
        int lineHeight = textRenderer.fontHeight + 2;

        YgdConfig.InfoDisplayConfig infoConfig = config.infoDisplay;

        if (infoConfig.showFps) {
            Text fpsText = Text.translatable("info.ygd.fps", fps).formatted(Formatting.WHITE);
            context.drawText(textRenderer, fpsText, baseX, baseY, 0xFFFFFF, true);
            baseY += lineHeight;
        }

        if (infoConfig.showTps) {
            Text tpsText = Text.translatable("info.ygd.tps", tps).formatted(Formatting.WHITE);
            context.drawText(textRenderer, tpsText, baseX, baseY, 0xFFFFFF, true);
            baseY += lineHeight;
        }

        if (infoConfig.showMspt) {
            Text msptText = Text.translatable("info.ygd.mspt", String.format("%.1f", mspt)).formatted(Formatting.WHITE);
            context.drawText(textRenderer, msptText, baseX, baseY, 0xFFFFFF, true);
            baseY += lineHeight;
        }

        if (infoConfig.showDirection && client.player != null) {
            Direction direction = client.player.getHorizontalFacing();
            Text directionName = getDirectionName(direction);
            Text directionText = Text.translatable("info.ygd.direction", directionName).formatted(Formatting.WHITE);
            context.drawText(textRenderer, directionText, baseX, baseY, 0xFFFFFF, true);
            baseY += lineHeight;
        }

        if (infoConfig.showBiome && client.player != null && client.world != null) {
            ClientWorld world = client.world;
            String biomeName = world.getBiome(client.player.getBlockPos()).getKey().map(key -> {
                try {
                    return Text.translatable("biome." + key.getValue().getNamespace() + "." + key.getValue().getPath()).getString();
                } catch (Exception e) {
                    return key.getValue().getPath();
                }
            }).orElse("Unknown");
            Text biomeText = Text.translatable("info.ygd.biome", biomeName).formatted(Formatting.WHITE);
            context.drawText(textRenderer, biomeText, baseX, baseY, 0xFFFFFF, true);
            baseY += lineHeight;
        }

        if (infoConfig.showRealTime) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String realTime = sdf.format(new Date());
            Text realTimeText = Text.translatable("info.ygd.real_time", realTime).formatted(Formatting.WHITE);
            context.drawText(textRenderer, realTimeText, baseX, baseY, 0xFFFFFF, true);
            baseY += lineHeight;
        }

    }

    private Text getDirectionName(Direction direction) {
        switch (direction) {
            case NORTH:
                return Text.translatable("info.ygd.direction.north");
            case SOUTH:
                return Text.translatable("info.ygd.direction.south");
            case EAST:
                return Text.translatable("info.ygd.direction.east");
            case WEST:
                return Text.translatable("info.ygd.direction.west");
            default:
                return Text.literal(direction.toString());
        }
    }
}
