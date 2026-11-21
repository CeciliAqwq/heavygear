package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.effect.HeavyEffect;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Brewing path for the heavy potion, so it is also reachable from the brewing stand (and therefore
 * visible in JEI's brewing category): awkward potion + heavy core, then redstone for the long one.
 */
@Mixin(PotionBrewing.class)
public abstract class PotionBrewingHeavyMixin {
    @Inject(method = "method_59699(Lnet/minecraft/class_1845$class_9665;)V", at = @At("TAIL"))
    private static void heavygear$addHeavyMix(PotionBrewing.Builder builder, CallbackInfo ci) {
        builder.addMix(Potions.AWKWARD, Items.HEAVY_CORE, HeavyEffect.POTION);
        builder.addMix(HeavyEffect.POTION, Items.REDSTONE, HeavyEffect.POTION_LONG);
    }
}
