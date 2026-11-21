package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.item.HeavyGear;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Wind Burst on the heavy fishing rod must only work through the bobber: vanilla would otherwise run
 * its {@code post_attack} gust whenever the rod is used as a melee weapon (a rod with Wind Burst
 * would launch itself/the victim on a plain left click). The post-attack pipeline is skipped for the
 * rod only; the hook based launch lives in {@link HeavyBobberSmashMixin}.
 */
@Mixin(EnchantmentHelper.class)
public abstract class HeavyRodMeleeGuardMixin {
    @Inject(
            method = "method_60619(Lnet/minecraft/class_3218;Lnet/minecraft/class_1297;"
                    + "Lnet/minecraft/class_1282;Lnet/minecraft/class_1799;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void heavygear$noMeleeGust(ServerLevel level, Entity target, DamageSource source,
                                              ItemStack weapon, CallbackInfo ci) {
        if (weapon != null && weapon.is(HeavyGear.HEAVY_FISHING_ROD)) {
            ci.cancel();
        }
    }
}
