package com.cecilia.heavygear.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Stops the {@code post_piercing_attack} enchantment effects (Lunge: impulse, lunge sound, extra
 * item damage, exhaustion) from firing while a riptide dash is running.
 *
 * <p>{@code Player#attack} always ends with {@code method_75125()} — the hook that applies
 * {@code post_piercing_attack} — and the riptide contact hit goes
 * {@code checkAutoSpinAttack -> doAutoAttackOnTouch -> attack}, so without this guard every mob hit
 * during the dash plays the lunge sound and pushes the player forward. It is applied to
 * {@code Player#method_75125} (not to the stab path) so a normal left-click stab keeps its lunge.</p>
 */
@Mixin(Player.class)
public abstract class RiptideLungeGuardMixin {
    @Inject(method = "method_75125()V", at = @At("HEAD"), cancellable = true)
    private void heavygear$noLungeWhileRiptiding(CallbackInfo ci) {
        if (((Player) (Object) this).isAutoSpinAttack()) {
            ci.cancel();
        }
    }
}
