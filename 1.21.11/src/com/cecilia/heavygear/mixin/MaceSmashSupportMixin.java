package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.combat.SmashAttribution;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mace style weapons (the heavy spear and the Spear of Destiny extend {@code MaceItem}).
 *
 * <ul>
 *   <li>Under the heavy potion the vanilla mace bonus is muted, because {@link EffectSmashMixin}
 *       applies the smash once for every item; otherwise mace weapons would take the hits twice.</li>
 *   <li>The smash attribution is recorded on an actual hit, so only a real smash claims the weapon
 *       specific death message.</li>
 * </ul>
 */
@Mixin(MaceItem.class)
public abstract class MaceSmashSupportMixin {
    @Inject(method = "method_7873(Lnet/minecraft/class_1799;Lnet/minecraft/class_1309;"
            + "Lnet/minecraft/class_1309;)V", at = @At("HEAD"))
    private void heavygear$recordSmash(ItemStack stack, LivingEntity target, LivingEntity attacker,
                                       CallbackInfo ci) {
        // only a real smash (the attacker qualifies) may claim the death message
        if (MaceItem.canSmashAttack(attacker)) {
            SmashAttribution.record(target);
        }
    }
}
