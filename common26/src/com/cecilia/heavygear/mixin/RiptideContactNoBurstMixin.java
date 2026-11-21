package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.combat.RiptideContact;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * The riptide dash contact hit must not trigger post-attack enchantment effects from the spear either:
 * with Wind Burst on the Longinus, the gust exploded at the touched creature and threw it into the air,
 * which a vanilla trident never does. Cancelling the shared worker (rather than
 * {@code doPostAttackEffectsWithItemSource}) keeps {@code DiveWindBurstMixin}'s HEAD/RETURN bracket
 * balanced, and the flag is only set for that one contact hit, so charged/stab attacks keep their gust.
 */
@Mixin(EnchantmentHelper.class)
public abstract class RiptideContactNoBurstMixin {
    @Inject(
            method = "doPostAttackEffectsWithItemSourceOnBreak(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void heavygear$noPostAttackDuringRiptideContact(ServerLevel level, Entity target,
                                                                    DamageSource source, ItemStack stack,
                                                                    Consumer<Item> onBreak, CallbackInfo ci) {
        if (RiptideContact.isActive()) {
            ci.cancel();
        }
    }
}
