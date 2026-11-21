package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.item.CompressedWindChargeImpulse;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * A compressed wind charge launch must not cause fall damage when the player comes back down to the
 * launch height - but the fall distance itself has to stay intact, because the heavy gear smash reads
 * it. Only the fall damage itself is cancelled here.
 */
@Mixin(LivingEntity.class)
public abstract class CompressedWindChargeFallMixin {
    @Inject(method = "causeFallDamage(DFLnet/minecraft/world/damagesource/DamageSource;)Z", at = @At("HEAD"), cancellable = true)
    private void heavygear$skipFallDamage(double fallDistance, float multiplier, DamageSource source,
                                            CallbackInfoReturnable<Boolean> cir) {
        if (!(((Object) this) instanceof ServerPlayer player)) {
            return;
        }
        if (CompressedWindChargeImpulse.protects(player.getUUID(), player.getY())) {
            cir.setReturnValue(false);
        }
    }
}