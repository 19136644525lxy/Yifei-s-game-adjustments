package com.yifei.ygd.mixin;

import com.yifei.ygd.YgdMod;
import com.yifei.ygd.config.ConfigManager;
import com.yifei.ygd.config.YgdConfig;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    // 格挡状态
    private boolean isBlocking = false;
    // 音频播放时间戳
    private long lastSoundPlayTime = 0;
    // 受击计数器
    private int hitCount = 0;
    // 受击计数重置时间戳
    private long lastHitResetTime = 0;
    
    // 注册物品使用事件监听器
    static {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() instanceof SwordItem) {
                // 当玩家右键使用剑时，设置格挡状态为true
                ((PlayerEntityMixin)(Object)player).isBlocking = true;
            }
            return TypedActionResult.pass(stack);
        });
    }
    
    // 监听玩家攻击事件，当玩家攻击时，自动取消格挡状态
    @Inject(method = "attack", at = @At("HEAD"))
    private void onAttack(Entity target, CallbackInfo ci) {
        // 当玩家攻击时，取消格挡状态
        isBlocking = false;
    }
    
    @Inject(method = "getAttackCooldownProgress", at = @At("HEAD"), cancellable = true)
    private void onGetAttackCooldownProgress(float f, CallbackInfoReturnable<Float> cir) {
        YgdConfig config = ConfigManager.getConfig();
        if (config != null && config.attackCooldown != null && !config.attackCooldown.enabled) {
            // 禁用攻击冷却，直接返回1.0表示冷却完成
            cir.setReturnValue(1.0f);
        }
    }
    
    // 使用Inject来处理伤害事件，实现格挡和振刀功能
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        YgdConfig config = ConfigManager.getConfig();
        if (config != null && config.block != null && config.block.enabled) {
            PlayerEntity player = (PlayerEntity)(Object)this;
            // 检查玩家是否处于格挡状态
            if (isBlocking) {
                ItemStack mainHandStack = player.getStackInHand(Hand.MAIN_HAND);
                ItemStack offHandStack = player.getStackInHand(Hand.OFF_HAND);
                
                boolean holdingSword = mainHandStack.getItem() instanceof SwordItem || offHandStack.getItem() instanceof SwordItem;
                if (holdingSword) {
                    // 振刀逻辑
                    Random random = new Random();
                    int parryChance = config.block.parryChance;
                    boolean parrySuccess = random.nextInt(100) < parryChance;
                    
                    if (parrySuccess) {
                        // 振刀成功
                        if (!player.getWorld().isClient) {
                            ServerWorld serverWorld = (ServerWorld) player.getWorld();
                            // 生成粒子效果
                            for (int i = 0; i < 5; i++) {
                                double x = player.getX() + (random.nextDouble() - 0.5) * 1.0;
                                double y = player.getY() + random.nextDouble() * 1.5;
                                double z = player.getZ() + (random.nextDouble() - 0.5) * 1.0;
                                serverWorld.spawnParticles(ParticleTypes.CRIT, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
                            }
                            // 音频播放频率控制
                            long currentTime = System.currentTimeMillis();
                            long timeSinceLastSound = currentTime - lastSoundPlayTime;
                            long timeSinceLastHitReset = currentTime - lastHitResetTime;
                            
                            // 每3秒重置一次受击计数
                            if (timeSinceLastHitReset >= 3000) {
                                hitCount = 0;
                                lastHitResetTime = currentTime;
                            }
                            
                            // 受击次数不超过10次，且距离上次播放音频超过3秒
                            if (hitCount < 10 && timeSinceLastSound >= 3000) {
                                player.getWorld().playSound(null, player.getBlockPos(), YgdMod.ZHENDAO_SOUND, SoundCategory.PLAYERS, 1.0f, 1.0f);
                                lastSoundPlayTime = currentTime;
                                hitCount++;
                            }
                            
                            // 75%概率减少耐久度
                            if (random.nextInt(100) < 75) {
                                if (mainHandStack.getItem() instanceof SwordItem) {
                                    mainHandStack.damage(1, player, (p) -> p.sendToolBreakStatus(Hand.MAIN_HAND));
                                } else if (offHandStack.getItem() instanceof SwordItem) {
                                    offHandStack.damage(1, player, (p) -> p.sendToolBreakStatus(Hand.OFF_HAND));
                                }
                            }
                        }
                        // 应用伤害减免
                        float reduction = config.block.damageReductionPercentage / 100.0f;
                        float reducedDamage = amount * (1.0f - reduction);
                        if (reducedDamage <= 0.0f) {
                            // 完全格挡伤害
                            cir.setReturnValue(false);
                        } else {
                            // 重新调用damage方法，应用减少后的伤害
                            player.damage(source, reducedDamage);
                            cir.setReturnValue(false);
                        }

                    } else {
                        // 振刀失败，应用负面效果
                        if (!player.getWorld().isClient) {
                            // 失明效果
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0));
                            // 缓慢效果
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 2));
                        }
                        // 不减少伤害
                        
                        // 振刀失败后取消格挡状态
                        isBlocking = false;
                    }
                }
            }
        }
    }
}
