package com.cecilia.heavygear.mixin.client;

import com.cecilia.heavygear.client.CrossbowEquivalence;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * First-person crossbow handling is gated on {@code stack.is(Items.CROSSBOW)} in several places of the
 * item renderer: the hand selection (which hands are drawn), the "charged crossbow" helper, and the
 * per-arm item transform that produces the winding motion. All of those resolve to the
 * {@code TypedInstance#is(T)} identity default, so a look-alike item never matches and the crossbow
 * stays in the plain item pose. Every {@code is} call in those methods is redirected; the handler keeps
 * the original identity semantics and only adds the heavy crossbow.
 */
@Mixin(ItemInHandRenderer.class)
public class CrossbowFirstPersonMixin {
    @Redirect(method = "evaluateWhichHandsToRender",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
    private static boolean heavygear$handsToRender(ItemStack stack, Object type) {
        return CrossbowEquivalence.is(stack, type);
    }

    @Redirect(method = "selectionUsingItemWhileHoldingBowLike",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
    private static boolean heavygear$selectionUsingItem(ItemStack stack, Object type) {
        return CrossbowEquivalence.is(stack, type);
    }

    @Redirect(method = "isChargedCrossbow",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
    private static boolean heavygear$isChargedCrossbow(ItemStack stack, Object type) {
        return CrossbowEquivalence.is(stack, type);
    }

    @Redirect(method = "renderArmWithItem",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
    private boolean heavygear$armWithItem(ItemStack stack, Object type) {
        return CrossbowEquivalence.is(stack, type);
    }
}