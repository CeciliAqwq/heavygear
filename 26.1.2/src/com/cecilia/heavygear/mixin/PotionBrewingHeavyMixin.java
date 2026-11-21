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
 * Brewing path for the heavy potion (awkward + heavy core, then redstone for the long one) so it is
 * reachable from a brewing stand instead of being creative/command only.
 *
 * <p>26.3 moved brewing to data driven {@code minecraft:brewing} recipes, see
 * {@code data/heavygear/recipe/brewing/}. That is why this mixin only exists on 26.1.2/26.2.</p>
 */
@Mixin(PotionBrewing.class)
public abstract class PotionBrewingHeavyMixin {
    @Inject(method = "addVanillaMixes", at = @At("TAIL"))
    private static void heavygear$addHeavyMix(PotionBrewing.Builder builder, CallbackInfo ci) {
        builder.addMix(Potions.AWKWARD, Items.HEAVY_CORE, HeavyEffect.POTION);
        builder.addMix(HeavyEffect.POTION, Items.REDSTONE, HeavyEffect.POTION_LONG);
    }
}