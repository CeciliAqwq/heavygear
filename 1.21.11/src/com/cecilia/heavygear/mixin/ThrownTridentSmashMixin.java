package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.entity.ThrownLonginus;
import net.minecraft.world.entity.projectile.ThrownTrident;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Adds the mace style smash bonus to the single trident hit when the projectile
 * is a thrown Spear of Longinus and it has fallen before hitting.
 */
@Mixin(ThrownTrident.class)
public class ThrownTridentSmashMixin {
    @ModifyArg(method = "method_7454",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/class_1297;method_64420(Lnet/minecraft/class_1282;F)Z"),
            index = 1)
    private float heavygear$addSmashDamage(float damage) {
        if ((Object) this instanceof ThrownLonginus longinus && longinus.heavygear$isSmashArmed()) {
            return damage + longinus.heavygear$smashBonus();
        }
        return damage;
    }
}
