package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.item.HeavyGear;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Vanilla identifies a few items by exact item comparison, which makes custom look-alikes miss the
 * behaviour keyed on them:
 *
 * <ul>
 *   <li>{@code Items.CROSSBOW} — the crossbow arm pose and first-person crossbow handling;</li>
 *   <li>{@code Items.FISHING_ROD} — {@code FishingHook#tick} discards a hook whose owner is not
 *       holding {@code Items.FISHING_ROD}, so a custom rod's bobber vanished instantly.</li>
 * </ul>
 *
 * <p>The heavy items report themselves as their vanilla counterpart here, so they animate and behave
 * exactly like the vanilla ones.</p>
 */
@Mixin(ItemStack.class)
public abstract class VanillaItemEquivalenceMixin {
    @Inject(method = "method_31574(Lnet/minecraft/class_1792;)Z", at = @At("HEAD"),
            cancellable = true)
    private void heavygear$vanillaEquivalence(Item item, CallbackInfoReturnable<Boolean> cir) {
        Item self = ((ItemStack) (Object) this).getItem();
        if (item == Items.CROSSBOW && self == HeavyGear.HEAVY_CROSSBOW) {
            cir.setReturnValue(true);
        } else if (item == Items.FISHING_ROD && self == HeavyGear.HEAVY_FISHING_ROD) {
            cir.setReturnValue(true);
        }
    }
}
