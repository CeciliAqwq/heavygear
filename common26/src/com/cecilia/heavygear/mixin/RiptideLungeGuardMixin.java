package com.cecilia.heavygear.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stops the {@code post_piercing_attack} enchantment effects (Lunge: impulse, lunge sound, extra item
 * damage, exhaustion) from firing while a riptide dash is running: the riptide contact hit goes
 * {@code checkAutoSpinAttack -> doAutoAttackOnTouch -> attack}, and {@code attack} ends with
 * {@code postPiercingAttack}, so without this guard every mob hit during the dash plays the lunge sound
 * and pushes the player forward. Only players are affected; mobs keep their behaviour.
 */
@Mixin(LivingEntity.class)
public abstract class RiptideLungeGuardMixin {
    @Inject(method = "postPiercingAttack", at = @At("HEAD"), cancellable = true)
    private void heavygear$noLungeWhileRiptiding(CallbackInfo ci) {
        if (((LivingEntity) (Object) this) instanceof Player player && player.isAutoSpinAttack()) {
            ci.cancel();
        }
    }
}