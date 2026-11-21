package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.combat.SmashTopUp;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Heavy potion smash for player victims: {@code Player} overrides {@code actuallyHurt}, so the
 * {@link EffectTopUpMixin} hook on {@code LivingEntity} never runs for a player.
 */
@Mixin(Player.class)
public abstract class EffectPlayerTopUpMixin {
    @Inject(method = "actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V",
            at = @At("HEAD"))
    private void heavygear$rememberHealth(ServerLevel level, DamageSource source, float amount,
                                          CallbackInfo ci) {
        SmashTopUp.onHurtStart((LivingEntity) (Object) this);
    }

    @Inject(method = "actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V",
            at = @At("RETURN"))
    private void heavygear$topUp(ServerLevel level, DamageSource source, float amount, CallbackInfo ci) {
        SmashTopUp.onHurtEnd(level, (LivingEntity) (Object) this, source);
    }
}
