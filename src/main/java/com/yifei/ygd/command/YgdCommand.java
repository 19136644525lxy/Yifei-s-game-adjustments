package com.yifei.ygd.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.yifei.ygd.config.ConfigManager;
import com.yifei.ygd.config.YgdConfig;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class YgdCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
            literal("ygd")
                .executes(YgdCommand::showHelp)
                .then(literal("help").executes(YgdCommand::showHelp))
                .then(literal("cake")
                    .then(literal("drop")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setCakeDrop)
                        )
                    )
                )
                .then(literal("item")
                    .then(literal("durability")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setShowDurability)
                        )
                    )
                    .then(literal("durabilitycolor")
                        .then(argument("scheme", StringArgumentType.word())
                            .executes(YgdCommand::setDurabilityColorScheme)
                        )
                    )
                )
                .then(literal("sugarcane")
                    .then(literal("bonemeal")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setSugarcaneBoneMeal)
                        )
                    )
                    .then(literal("height")
                        .then(argument("limit", IntegerArgumentType.integer(1, 256))
                            .executes(YgdCommand::setSugarcaneHeightLimit)
                        )
                    )
                )
                .then(literal("info")
                    .then(literal("enable")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setInfoDisplayEnabled)
                        )
                    )
                    .then(literal("fps")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setInfoDisplayFps)
                        )
                    )
                    .then(literal("tps")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setInfoDisplayTps)
                        )
                    )
                    .then(literal("mspt")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setInfoDisplayMspt)
                        )
                    )
                    .then(literal("direction")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setInfoDisplayDirection)
                        )
                    )
                    .then(literal("biome")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setInfoDisplayBiome)
                        )
                    )
                    .then(literal("realtime")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setInfoDisplayRealTime)
                        )
                    )
                    .then(literal("interval")
                        .then(argument("ticks", IntegerArgumentType.integer(1, 30))
                            .executes(YgdCommand::setInfoDisplayInterval)
                        )
                    )
                )
                .then(literal("zoom")
                    .then(literal("enable")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setZoomEnabled)
                        )
                    )
                    .then(literal("initial")
                        .then(argument("level", IntegerArgumentType.integer(2, 10))
                            .executes(YgdCommand::setZoomInitialLevel)
                        )
                    )
                    .then(literal("time")
                        .then(literal("in")
                            .then(argument("milliseconds", IntegerArgumentType.integer(1, 1000))
                                .executes(YgdCommand::setZoomInTime)
                            )
                        )
                        .then(literal("out")
                            .then(argument("milliseconds", IntegerArgumentType.integer(1, 1000))
                                .executes(YgdCommand::setZoomOutTime)
                            )
                        )
                    )
                    .then(literal("transition")
                        .then(literal("in")
                            .then(argument("type", StringArgumentType.word())
                                .executes(YgdCommand::setZoomInTransition)
                            )
                        )
                        .then(literal("out")
                            .then(argument("type", StringArgumentType.word())
                                .executes(YgdCommand::setZoomOutTransition)
                            )
                        )
                    )
                    .then(literal("linear")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setZoomLinearLikeSteps)
                        )
                    )
                    .then(literal("retainsteps")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setZoomRetainZoomSteps)
                        )
                    )
                    .then(literal("scrollzoom")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setZoomScrollZoom)
                        )
                    )
                    .then(literal("scrollsteps")
                        .then(argument("count", IntegerArgumentType.integer(1, 50))
                            .executes(YgdCommand::setZoomScrollStepCount)
                        )
                    )
                    .then(literal("zoomperstep")
                        .then(argument("amount", IntegerArgumentType.integer(50, 300))
                            .executes(YgdCommand::setZoomZoomPerStep)
                        )
                    )
                    .then(literal("scrollsmoothness")
                        .then(argument("value", IntegerArgumentType.integer(10, 100))
                            .executes(YgdCommand::setZoomScrollZoomSmoothness)
                        )
                    )
                    .then(literal("keybehaviour")
                        .then(argument("behaviour", StringArgumentType.word())
                            .executes(YgdCommand::setZoomKeyBehaviour)
                        )
                    )
                )
                .then(literal("reload")
                    .executes(YgdCommand::reloadConfig)
                )
                .then(literal("status")
                    .executes(YgdCommand::showStatus)
                )
                .then(literal("time")
                    .then(literal("add")
                        .then(argument("value", IntegerArgumentType.integer(1))
                            .then(literal("秒").executes(YgdCommand::addTimeSeconds)
                                .then(literal("钟").executes(YgdCommand::addTimeMinutes)
                                    .then(literal("时").executes(YgdCommand::addTimeHours)
                                    )
                                )
                            )
                            .then(literal("s").executes(YgdCommand::addTimeSeconds)
                                .then(literal("min").executes(YgdCommand::addTimeMinutes)
                                    .then(literal("h").executes(YgdCommand::addTimeHours)
                                    )
                                )
                            )
                        )
                    )
                    .then(literal("set")
                        .then(literal("白天").executes(YgdCommand::setTimeDay)
                            .then(literal("day").executes(YgdCommand::setTimeDay)
                            )
                        )
                        .then(literal("中午").executes(YgdCommand::setTimeNoon)
                            .then(literal("noon").executes(YgdCommand::setTimeNoon)
                            )
                        )
                        .then(literal("傍晚").executes(YgdCommand::setTimeEvening)
                            .then(literal("evening").executes(YgdCommand::setTimeEvening)
                            )
                        )
                        .then(literal("午夜").executes(YgdCommand::setTimeMidnight)
                            .then(literal("midnight").executes(YgdCommand::setTimeMidnight)
                            )
                        )
                    )
                )
                .then(literal("sweep")
                    .then(literal("enable")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setSweepEnable)
                        )
                    )
                    .then(literal("period")
                        .then(argument("minutes", IntegerArgumentType.integer(1, 60))
                            .executes(YgdCommand::setSweepPeriod)
                        )
                    )
                    .then(literal("notify")
                        .then(argument("seconds", IntegerArgumentType.integer(1, 60))
                            .executes(YgdCommand::setSweepNotify)
                        )
                    )
                    .then(literal("discount")
                        .then(argument("seconds", IntegerArgumentType.integer(1, 30))
                            .executes(YgdCommand::setSweepDiscount)
                        )
                    )
                    .then(literal("notice")
                        .then(argument("message", StringArgumentType.greedyString())
                            .executes(YgdCommand::setSweepNotice)
                        )
                    )
                    .then(literal("noticecomplete")
                        .then(argument("message", StringArgumentType.greedyString())
                            .executes(YgdCommand::setSweepNoticeComplete)
                        )
                    )
                    .then(literal("noticecolor")
                        .then(argument("color", StringArgumentType.word())
                            .executes(YgdCommand::setSweepNoticeColor)
                        )
                    )
                    .then(literal("completecolor")
                        .then(argument("color", StringArgumentType.word())
                            .executes(YgdCommand::setSweepCompleteColor)
                        )
                    )
                    .then(literal("force")
                        .executes(YgdCommand::forceSweep)
                    )
                    .then(literal("item")
                        .then(literal("enable")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepItemEnable)
                            )
                        )
                        .then(literal("whitemode")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepItemWhiteMode)
                            )
                        )
                        .then(literal("blackmode")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepItemBlackMode)
                            )
                        )
                        .then(literal("whitelist")
                            .then(argument("items", StringArgumentType.greedyString())
                                .executes(YgdCommand::setSweepItemWhitelist)
                            )
                        )
                        .then(literal("blacklist")
                            .then(argument("items", StringArgumentType.greedyString())
                                .executes(YgdCommand::setSweepItemBlacklist)
                            )
                        )
                    )
                    .then(literal("mob")
                        .then(literal("enable")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepMobEnable)
                            )
                        )
                        .then(literal("exp")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepMobExp)
                            )
                        )
                        .then(literal("animal")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepMobAnimal)
                            )
                        )
                        .then(literal("monster")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepMobMonster)
                            )
                        )
                        .then(literal("whitemode")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepMobWhiteMode)
                            )
                        )
                        .then(literal("blackmode")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepMobBlackMode)
                            )
                        )
                        .then(literal("whitelist")
                            .then(argument("mobs", StringArgumentType.greedyString())
                                .executes(YgdCommand::setSweepMobWhitelist)
                            )
                        )
                        .then(literal("blacklist")
                            .then(argument("mobs", StringArgumentType.greedyString())
                                .executes(YgdCommand::setSweepMobBlacklist)
                            )
                        )
                    )
                    .then(literal("other")
                        .then(literal("experience")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepOtherExperience)
                            )
                        )
                        .then(literal("fallingblocks")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepOtherFallingBlocks)
                            )
                        )
                        .then(literal("arrow")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepOtherArrow)
                            )
                        )
                        .then(literal("trident")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepOtherTrident)
                            )
                        )
                        .then(literal("projectile")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepOtherProjectile)
                            )
                        )
                        .then(literal("shulkerbullet")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepOtherShulkerBullet)
                            )
                        )
                        .then(literal("firework")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepOtherFirework)
                            )
                        )
                        .then(literal("itemframe")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepOtherItemFrame)
                            )
                        )
                        .then(literal("painting")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepOtherPainting)
                            )
                        )
                        .then(literal("boat")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepOtherBoat)
                            )
                        )
                        .then(literal("tnt")
                            .then(argument("enabled", BoolArgumentType.bool())
                                .executes(YgdCommand::setSweepOtherTNT)
                            )
                        )
                    )
                )
                .then(literal("imblocker")
                    .then(literal("enable")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setIMBlockerEnabled)
                        )
                    )
                )
                .then(literal("attackcooldown")
                    .then(literal("enable")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setAttackCooldownEnabled)
                        )
                    )
                )
                .then(literal("stacksize")
                    .then(literal("enable")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setStackSizeEnabled)
                        )
                    )
                )
                .then(literal("block")
                    .then(literal("enable")
                        .then(argument("enabled", BoolArgumentType.bool())
                            .executes(YgdCommand::setBlockEnabled)
                        )
                    )
                    .then(literal("reduction")
                        .then(argument("percentage", IntegerArgumentType.integer(1, 100))
                            .executes(YgdCommand::setBlockReductionPercentage)
                        )
                    )
                    .then(literal("parry")
                        .then(argument("chance", IntegerArgumentType.integer(1, 100))
                            .executes(YgdCommand::setBlockParryChance)
                        )
                    )
                )
        );
    }
    
    private static int showHelp(CommandContext<ServerCommandSource> context) {
        context.getSource().sendMessage(Text.translatable("command.ygd.help.header").formatted(Formatting.GOLD));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.cake").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.item").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.item.durabilitycolor").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.sugarcane").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.info").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.info.interval").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.zoom").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.zoom.transition").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.zoom.scroll").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.zoom.keybehaviour").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.zoom.spyglass").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.time").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.time.set").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.sweep").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.sweep.item").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.sweep.mob").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.sweep.other").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.imblocker").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.attackcooldown").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.stacksize").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.block").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.reload").formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.help.status").formatted(Formatting.GRAY));
        return 1;
    }
    
    private static int setCakeDrop(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.cake.enableCakeDrop = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.cake.drop", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setShowDurability(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.itemInfo.showDurability = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.item.durability", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    

    
    private static int setDurabilityColorScheme(CommandContext<ServerCommandSource> context) {
        String scheme = StringArgumentType.getString(context, "scheme").toUpperCase();
        try {
            YgdConfig.DurabilityColorScheme colorScheme = YgdConfig.DurabilityColorScheme.valueOf(scheme);
            YgdConfig config = ConfigManager.getConfig();
            config.itemInfo.durabilityColorScheme = colorScheme;
            ConfigManager.saveConfig();
            context.getSource().sendMessage(Text.translatable("command.ygd.item.durabilitycolor", Text.translatable(scheme)).formatted(Formatting.GREEN));
        } catch (IllegalArgumentException e) {
            context.getSource().sendMessage(Text.translatable("command.ygd.item.durabilitycolor.invalid").formatted(Formatting.RED));
        }
        return 1;
    }
    
    private static int setSugarcaneBoneMeal(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.sugarcane.enableSugarcaneBoneMeal = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sugarcane.bonemeal", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSugarcaneHeightLimit(CommandContext<ServerCommandSource> context) {
        int limit = IntegerArgumentType.getInteger(context, "limit");
        YgdConfig config = ConfigManager.getConfig();
        config.sugarcane.heightLimit = limit;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sugarcane.height", limit).formatted(Formatting.GREEN));
        return 1;
    }
    
    // 信息显示相关命令
    private static int setInfoDisplayEnabled(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.infoDisplay.enabled = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.info.enable", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setInfoDisplayFps(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.infoDisplay.showFps = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.info.fps", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setInfoDisplayTps(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.infoDisplay.showTps = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.info.tps", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setInfoDisplayDirection(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.infoDisplay.showDirection = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.info.direction", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setInfoDisplayBiome(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.infoDisplay.showBiome = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.info.biome", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setInfoDisplayRealTime(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.infoDisplay.showRealTime = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.info.realtime", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setInfoDisplayMspt(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.infoDisplay.showMspt = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.info.mspt", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setInfoDisplayInterval(CommandContext<ServerCommandSource> context) {
        int ticks = IntegerArgumentType.getInteger(context, "ticks");
        YgdConfig config = ConfigManager.getConfig();
        config.infoDisplay.updateInterval = ticks;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.info.interval", ticks).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int reloadConfig(CommandContext<ServerCommandSource> context) {
        ConfigManager.loadConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.reload").formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int showStatus(CommandContext<ServerCommandSource> context) {
        YgdConfig config = ConfigManager.getConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.status.header").formatted(Formatting.GOLD));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.cake", config.cake.enableCakeDrop ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.item.durability", config.itemInfo.showDurability ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        String colorSchemeKey = config.itemInfo.durabilityColorScheme.toString();
        context.getSource().sendMessage(Text.translatable("command.ygd.status.item.durabilitycolor", Text.translatable(colorSchemeKey)).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sugarcane.bonemeal", config.sugarcane.enableSugarcaneBoneMeal ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sugarcane.height", config.sugarcane.heightLimit).formatted(Formatting.GRAY));
        // 信息显示状态
        context.getSource().sendMessage(Text.translatable("command.ygd.status.info.enable", config.infoDisplay.enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.info.fps", config.infoDisplay.showFps ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.info.tps", config.infoDisplay.showTps ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.info.mspt", config.infoDisplay.showMspt ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.info.direction", config.infoDisplay.showDirection ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.info.biome", config.infoDisplay.showBiome ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.info.realtime", config.infoDisplay.showRealTime ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.info.interval", config.infoDisplay.updateInterval).formatted(Formatting.GRAY));
        // 缩放状态
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.enable", config.zoom.enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.initial", config.zoom.initialZoom).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.time.in", config.zoom.zoomInTime).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.time.out", config.zoom.zoomOutTime).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.transition.in", config.zoom.zoomInTransition.toString()).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.transition.out", config.zoom.zoomOutTransition.toString()).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.linear", config.zoom.linearLikeSteps ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.retainsteps", config.zoom.retainZoomSteps ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.scrollzoom", config.zoom.scrollZoom ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.scrollsteps", config.zoom.scrollStepCount).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.zoomperstep", config.zoom.zoomPerStep).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.scrollsmoothness", config.zoom.scrollZoomSmoothness).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.zoom.keybehaviour", config.zoom.zoomKeyBehaviour.toString()).formatted(Formatting.GRAY));
        // IMBlocker状态
        context.getSource().sendMessage(Text.translatable("command.ygd.status.imblocker.enable", config.imBlocker.enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        // 攻击冷却状态
        context.getSource().sendMessage(Text.translatable("command.ygd.status.attackcooldown.enable", config.attackCooldown.enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        // 物品堆叠状态
        context.getSource().sendMessage(Text.translatable("command.ygd.status.stacksize.enable", config.stackSize.enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        // 格挡功能状态
        context.getSource().sendMessage(Text.translatable("command.ygd.status.block.enable", config.block.enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.block.reduction", config.block.damageReductionPercentage).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.block.parry", config.block.parryChance).formatted(Formatting.GRAY));
        // 清扫功能状态
        com.yifei.ygd.config.SweepConfig sweepConfig = ConfigManager.getSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.header").formatted(Formatting.GOLD));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.enable", sweepConfig.common.isSweepEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.period", sweepConfig.common.sweepPeriod).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.notify", sweepConfig.common.sweepNotify).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.discount", sweepConfig.common.sweepDiscount).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.noticeColor", sweepConfig.common.noticeColor).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.completeColor", sweepConfig.common.completeColor).formatted(Formatting.GRAY));
        // 物品清理状态
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.item.enable", sweepConfig.item.isItemEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.item.whitemode", sweepConfig.item.itemWhiteMode ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.item.blackmode", sweepConfig.item.itemBlackMode ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        // 生物清理状态
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.mob.enable", sweepConfig.mob.isMobEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.mob.exp", sweepConfig.mob.isExpOn ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.mob.animal", sweepConfig.mob.isAnimalEntitiesCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.mob.monster", sweepConfig.mob.isMonsterEntitiesCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        // 其他实体清理状态
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.other.experience", sweepConfig.other.isExperienceOrbEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.other.fallingblocks", sweepConfig.other.isFallingBlocksEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.other.arrow", sweepConfig.other.isArrowEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.other.trident", sweepConfig.other.isTridentEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.other.projectile", sweepConfig.other.isDamagingProjectileEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.other.shulkerbullet", sweepConfig.other.isShulkerBulletEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.other.firework", sweepConfig.other.isFireworkRocketEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.other.itemframe", sweepConfig.other.isItemFrameEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.other.painting", sweepConfig.other.isPaintingEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.other.boat", sweepConfig.other.isBoatEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        context.getSource().sendMessage(Text.translatable("command.ygd.status.sweep.other.tnt", sweepConfig.other.isTNTEntityCleanupEnable ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GRAY));
        return 1;
    }
    
    // 缩放相关命令
    private static int setZoomEnabled(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.zoom.enabled = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.zoom.enable", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setZoomInitialLevel(CommandContext<ServerCommandSource> context) {
        int level = IntegerArgumentType.getInteger(context, "level");
        YgdConfig config = ConfigManager.getConfig();
        config.zoom.initialZoom = level;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.zoom.initial", level).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setZoomLinearLikeSteps(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.zoom.linearLikeSteps = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.zoom.linear", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setZoomInTime(CommandContext<ServerCommandSource> context) {
        int milliseconds = IntegerArgumentType.getInteger(context, "milliseconds");
        YgdConfig config = ConfigManager.getConfig();
        config.zoom.zoomInTime = milliseconds;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.zoom.time.in", milliseconds).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setZoomOutTime(CommandContext<ServerCommandSource> context) {
        int milliseconds = IntegerArgumentType.getInteger(context, "milliseconds");
        YgdConfig config = ConfigManager.getConfig();
        config.zoom.zoomOutTime = milliseconds;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.zoom.time.out", milliseconds).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setZoomRetainZoomSteps(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.zoom.retainZoomSteps = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.zoom.retainsteps", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setZoomScrollZoom(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.zoom.scrollZoom = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.zoom.scrollzoom", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setZoomScrollStepCount(CommandContext<ServerCommandSource> context) {
        int count = IntegerArgumentType.getInteger(context, "count");
        YgdConfig config = ConfigManager.getConfig();
        config.zoom.scrollStepCount = count;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.zoom.scrollsteps", count).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setZoomZoomPerStep(CommandContext<ServerCommandSource> context) {
        int amount = IntegerArgumentType.getInteger(context, "amount");
        YgdConfig config = ConfigManager.getConfig();
        config.zoom.zoomPerStep = amount;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.zoom.zoomperstep", amount).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setZoomScrollZoomSmoothness(CommandContext<ServerCommandSource> context) {
        int value = IntegerArgumentType.getInteger(context, "value");
        YgdConfig config = ConfigManager.getConfig();
        config.zoom.scrollZoomSmoothness = value;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.zoom.scrollsmoothness", value).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setZoomKeyBehaviour(CommandContext<ServerCommandSource> context) {
        String behaviour = StringArgumentType.getString(context, "behaviour").toUpperCase();
        try {
            YgdConfig.ZoomKeyBehaviour keyBehaviour = YgdConfig.ZoomKeyBehaviour.valueOf(behaviour);
            YgdConfig config = ConfigManager.getConfig();
            config.zoom.zoomKeyBehaviour = keyBehaviour;
            ConfigManager.saveConfig();
            context.getSource().sendMessage(Text.translatable("command.ygd.zoom.keybehaviour", Text.translatable(behaviour)).formatted(Formatting.GREEN));
        } catch (IllegalArgumentException e) {
            context.getSource().sendMessage(Text.translatable("command.ygd.zoom.keybehaviour.invalid").formatted(Formatting.RED));
        }
        return 1;
    }
    
    private static int setIMBlockerEnabled(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.imBlocker.enabled = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.imblocker.enable", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setAttackCooldownEnabled(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.attackCooldown.enabled = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.attackcooldown.enable", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setStackSizeEnabled(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.stackSize.enabled = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.stacksize.enable", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setBlockEnabled(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        YgdConfig config = ConfigManager.getConfig();
        config.block.enabled = enabled;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.block.enable", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setBlockReductionPercentage(CommandContext<ServerCommandSource> context) {
        int percentage = IntegerArgumentType.getInteger(context, "percentage");
        YgdConfig config = ConfigManager.getConfig();
        config.block.damageReductionPercentage = percentage;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.block.reduction", percentage).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setBlockParryChance(CommandContext<ServerCommandSource> context) {
        int chance = IntegerArgumentType.getInteger(context, "chance");
        YgdConfig config = ConfigManager.getConfig();
        config.block.parryChance = chance;
        ConfigManager.saveConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.block.parry", chance).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setZoomInTransition(CommandContext<ServerCommandSource> context) {
        String type = StringArgumentType.getString(context, "type").toUpperCase();
        try {
            YgdConfig.TransitionType transitionType = YgdConfig.TransitionType.valueOf(type);
            YgdConfig config = ConfigManager.getConfig();
            config.zoom.zoomInTransition = transitionType;
            ConfigManager.saveConfig();
            context.getSource().sendMessage(Text.translatable("command.ygd.zoom.transition.in", Text.translatable(type)).formatted(Formatting.GREEN));
        } catch (IllegalArgumentException e) {
            context.getSource().sendMessage(Text.translatable("command.ygd.zoom.transition.invalid").formatted(Formatting.RED));
        }
        return 1;
    }
    
    private static int setZoomOutTransition(CommandContext<ServerCommandSource> context) {
        String type = StringArgumentType.getString(context, "type").toUpperCase();
        try {
            YgdConfig.TransitionType transitionType = YgdConfig.TransitionType.valueOf(type);
            YgdConfig config = ConfigManager.getConfig();
            config.zoom.zoomOutTransition = transitionType;
            ConfigManager.saveConfig();
            context.getSource().sendMessage(Text.translatable("command.ygd.zoom.transition.out", Text.translatable(type)).formatted(Formatting.GREEN));
        } catch (IllegalArgumentException e) {
            context.getSource().sendMessage(Text.translatable("command.ygd.zoom.transition.invalid").formatted(Formatting.RED));
        }
        return 1;
    }
    
    // 时间添加方法
    private static int addTimeSeconds(CommandContext<ServerCommandSource> context) {
        int value = IntegerArgumentType.getInteger(context, "value");
        long gameTimeToAdd = value * 20L; // 1秒 = 20游戏刻
        return addTimeInternal(context, gameTimeToAdd, value, "text.ygd.time.second");
    }
    
    private static int addTimeMinutes(CommandContext<ServerCommandSource> context) {
        int value = IntegerArgumentType.getInteger(context, "value");
        long gameTimeToAdd = value * 60 * 20L; // 1分钟 = 60秒 = 1200游戏刻
        return addTimeInternal(context, gameTimeToAdd, value, "text.ygd.time.minute");
    }
    
    private static int addTimeHours(CommandContext<ServerCommandSource> context) {
        int value = IntegerArgumentType.getInteger(context, "value");
        long gameTimeToAdd = value * 60 * 60 * 20L; // 1小时 = 60分钟 = 3600秒 = 72000游戏刻
        return addTimeInternal(context, gameTimeToAdd, value, "text.ygd.time.hour");
    }
    
    private static int addTimeInternal(CommandContext<ServerCommandSource> context, long gameTimeToAdd, int value, String timeKey) {
        if (context.getSource().getWorld() != null) {
            long currentTime = context.getSource().getWorld().getTimeOfDay();
            context.getSource().getWorld().setTimeOfDay(currentTime + gameTimeToAdd);
            context.getSource().sendMessage(Text.translatable("command.ygd.time.add", value + " " + Text.translatable(timeKey).getString()).formatted(Formatting.GREEN));
        } else {
            context.getSource().sendMessage(Text.translatable("command.ygd.time.no_world").formatted(Formatting.RED));
            return 0;
        }
        return 1;
    }
    
    // 时间设置方法
    private static int setTimeDay(CommandContext<ServerCommandSource> context) {
        return setTimeInternal(context, 1000L, "text.ygd.time.day");
    }
    
    private static int setTimeNoon(CommandContext<ServerCommandSource> context) {
        return setTimeInternal(context, 6000L, "text.ygd.time.noon");
    }
    
    private static int setTimeEvening(CommandContext<ServerCommandSource> context) {
        return setTimeInternal(context, 12000L, "text.ygd.time.evening");
    }
    
    private static int setTimeMidnight(CommandContext<ServerCommandSource> context) {
        return setTimeInternal(context, 18000L, "text.ygd.time.midnight");
    }
    
    private static int setTimeInternal(CommandContext<ServerCommandSource> context, long targetTime, String timeKey) {
        if (context.getSource().getWorld() != null) {
            context.getSource().getWorld().setTimeOfDay(targetTime);
            context.getSource().sendMessage(Text.translatable("command.ygd.time.set", Text.translatable(timeKey)).formatted(Formatting.GREEN));
        } else {
            context.getSource().sendMessage(Text.translatable("command.ygd.time.no_world").formatted(Formatting.RED));
            return 0;
        }
        return 1;
    }
    
    // 扫地机命令
    private static int setSweepPeriod(CommandContext<ServerCommandSource> context) {
        int minutes = IntegerArgumentType.getInteger(context, "minutes");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.common.sweepPeriod = minutes;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.period", minutes).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepNotify(CommandContext<ServerCommandSource> context) {
        int seconds = IntegerArgumentType.getInteger(context, "seconds");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.common.sweepNotify = seconds;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.notify", seconds).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepDiscount(CommandContext<ServerCommandSource> context) {
        int seconds = IntegerArgumentType.getInteger(context, "seconds");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.common.sweepDiscount = seconds;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.discount", seconds).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepNotice(CommandContext<ServerCommandSource> context) {
        String message = StringArgumentType.getString(context, "message");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.common.sweepNotice = message;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.notice", message).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepNoticeComplete(CommandContext<ServerCommandSource> context) {
        String message = StringArgumentType.getString(context, "message");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.common.sweepNoticeComplete = message;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.noticecomplete", message).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepNoticeColor(CommandContext<ServerCommandSource> context) {
        String color = StringArgumentType.getString(context, "color");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.common.noticeColor = color;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.noticecolor", color).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepCompleteColor(CommandContext<ServerCommandSource> context) {
        String color = StringArgumentType.getString(context, "color");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.common.completeColor = color;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.completecolor", color).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int forceSweep(CommandContext<ServerCommandSource> context) {
        // 强制执行清扫
        com.yifei.ygd.game.SweepSystem.getInstance().performSweep();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.force").formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepEnable(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.common.isSweepEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.enable", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    // 物品清理相关命令
    private static int setSweepItemEnable(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.item.isItemEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.item.enable", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepItemWhiteMode(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.item.itemWhiteMode = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.item.whitemode", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepItemBlackMode(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.item.itemBlackMode = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.item.blackmode", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepItemWhitelist(CommandContext<ServerCommandSource> context) {
        String items = StringArgumentType.getString(context, "items");
        String[] itemArray = items.split(",");
        for (int i = 0; i < itemArray.length; i++) {
            itemArray[i] = itemArray[i].trim();
        }
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.item.itemEntitiesWhitelist = itemArray;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.item.whitelist", items).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepItemBlacklist(CommandContext<ServerCommandSource> context) {
        String items = StringArgumentType.getString(context, "items");
        String[] itemArray = items.split(",");
        for (int i = 0; i < itemArray.length; i++) {
            itemArray[i] = itemArray[i].trim();
        }
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.item.itemEntitiesBlacklist = itemArray;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.item.blacklist", items).formatted(Formatting.GREEN));
        return 1;
    }
    
    // 生物清理相关命令
    private static int setSweepMobEnable(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.mob.isMobEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.mob.enable", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepMobExp(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.mob.isExpOn = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.mob.exp", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepMobAnimal(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.mob.isAnimalEntitiesCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.mob.animal", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepMobMonster(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.mob.isMonsterEntitiesCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.mob.monster", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepMobWhiteMode(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.mob.mobWhiteMode = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.mob.whitemode", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepMobBlackMode(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.mob.mobBlackMode = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.mob.blackmode", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepMobWhitelist(CommandContext<ServerCommandSource> context) {
        String mobs = StringArgumentType.getString(context, "mobs");
        String[] mobArray = mobs.split(",");
        for (int i = 0; i < mobArray.length; i++) {
            mobArray[i] = mobArray[i].trim();
        }
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.mob.mobEntitiesWhitelist = mobArray;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.mob.whitelist", mobs).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepMobBlacklist(CommandContext<ServerCommandSource> context) {
        String mobs = StringArgumentType.getString(context, "mobs");
        String[] mobArray = mobs.split(",");
        for (int i = 0; i < mobArray.length; i++) {
            mobArray[i] = mobArray[i].trim();
        }
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.mob.mobEntitiesBlacklist = mobArray;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.mob.blacklist", mobs).formatted(Formatting.GREEN));
        return 1;
    }
    
    // 其他实体清理相关命令
    private static int setSweepOtherExperience(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.other.isExperienceOrbEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.other.experience", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepOtherFallingBlocks(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.other.isFallingBlocksEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.other.fallingblocks", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepOtherArrow(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.other.isArrowEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.other.arrow", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepOtherTrident(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.other.isTridentEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.other.trident", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepOtherProjectile(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.other.isDamagingProjectileEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.other.projectile", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepOtherShulkerBullet(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.other.isShulkerBulletEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.other.shulkerbullet", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepOtherFirework(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.other.isFireworkRocketEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.other.firework", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepOtherItemFrame(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.other.isItemFrameEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.other.itemframe", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepOtherPainting(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.other.isPaintingEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.other.painting", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepOtherBoat(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.other.isBoatEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.other.boat", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
    
    private static int setSweepOtherTNT(CommandContext<ServerCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        com.yifei.ygd.config.SweepConfig config = ConfigManager.getSweepConfig();
        config.other.isTNTEntityCleanupEnable = enabled;
        ConfigManager.saveSweepConfig();
        context.getSource().sendMessage(Text.translatable("command.ygd.sweep.other.tnt", enabled ? Text.translatable("command.ygd.enabled") : Text.translatable("command.ygd.disabled")).formatted(Formatting.GREEN));
        return 1;
    }
}