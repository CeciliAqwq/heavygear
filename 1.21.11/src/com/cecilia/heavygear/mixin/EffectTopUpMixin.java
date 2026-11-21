package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.combat.SmashTopUp;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Heavy potion smash for mob victims. */
@Mixin(LivingEntity.class)
public abstract class EffectTopUpMixin {
    @Inject(method = "method_6074(Lnet/minecraft/class_3218;Lnet/minecraft/class_1282;F)V",
            at = @At("HEAD"))
    private void heavygear$rememberHealth(ServerLevel level, DamageSource source, float amount,
                                          CallbackInfo ci) {
        SmashTopUp.onHurtStart((LivingEntity) (Object) this);
    }

    @Inject(method = "method_6074(Lnet/minecraft/class_3218;Lnet/minecraft/class_1282;F)V",
            at = @At("RETURN"))
    private void heavygear$topUp(ServerLevel level, DamageSource source, float amount, CallbackInfo ci) {
        SmashTopUp.onHurtEnd(level, (LivingEntity) (Object) this, source);
    }
}
