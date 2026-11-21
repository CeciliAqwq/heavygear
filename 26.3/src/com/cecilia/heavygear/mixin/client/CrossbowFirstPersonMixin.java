package com.cecilia.heavygear.mixin.client;

import com.cecilia.heavygear.client.CrossbowEquivalence;

import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * First-person crossbow handling (the charging/winding transform and the loaded pose) is gated on
 * {@code stack.is(Items.CROSSBOW)}; the heavy crossbow has to pass that test.
 */
@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class CrossbowFirstPersonMixin {
    @Redirect(method = "submitArmWithItem",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
    private boolean heavygear$isCrossbow(ItemStack stack, Object type) {
        return CrossbowEquivalence.is(stack, type);
    }
}