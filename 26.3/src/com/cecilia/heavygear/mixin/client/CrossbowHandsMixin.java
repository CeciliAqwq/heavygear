package com.cecilia.heavygear.mixin.client;

import com.cecilia.heavygear.client.CrossbowEquivalence;

import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * 26.3 splits the first-person logic: the renderer draws the arm, while this state class decides which
 * hands are drawn and whether a crossbow is charged. Both are gated on
 * {@code stack.is(Items.CROSSBOW)}, so the heavy crossbow has to answer that test here too.
 */
@Mixin(FirstPersonHandsAndItems.class)
public class CrossbowHandsMixin {
    @Redirect(method = "evaluateWhichHandsToRender",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
    private static boolean heavygear$handsToRender(ItemStack stack, Object type) {
        return CrossbowEquivalence.is(stack, type);
    }

    @Redirect(method = "isChargedCrossbow",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
    private static boolean heavygear$isChargedCrossbow(ItemStack stack, Object type) {
        return CrossbowEquivalence.is(stack, type);
    }
}