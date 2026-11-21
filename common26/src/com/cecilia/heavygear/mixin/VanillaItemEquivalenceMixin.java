package com.cecilia.heavygear.mixin;

import com.cecilia.heavygear.item.HeavyGear;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

/**
 * Vanilla identifies a few items by identity, which makes custom look-alikes miss the behaviour keyed
 * on them:
 *
 * <ul>
 *   <li>{@code Items.CROSSBOW} - the crossbow arm pose and first-person crossbow handling
 *       (see the client side {@code Crossbow*Mixin}s, which have to hook the renderers directly);</li>
 *   <li>{@code Items.FISHING_ROD} - {@code FishingHook#tick} discards a hook whose owner is not
 *       holding {@code Items.FISHING_ROD}, so a custom rod's bobber vanished instantly.</li>
 * </ul>
 *
 * <p>The heavy items report themselves as their vanilla counterpart for the predicate based tests.
 * The identity based tests ({@code stack.is(Items.X)}) resolve to the {@code TypedInstance#is(T)}
 * interface default, which cannot be hooked from a mixin, hence the renderer level hooks.</p>
 */
@Mixin(ItemStack.class)
public abstract class VanillaItemEquivalenceMixin {
    @Inject(method = "is(Ljava/util/function/Predicate;)Z", at = @At("HEAD"),
            cancellable = true)
    private void heavygear$vanillaEquivalence(Predicate<Holder<Item>> predicate,
                                              CallbackInfoReturnable<Boolean> cir) {
        Item self = ((ItemStack) (Object) this).getItem();
        if (self == HeavyGear.HEAVY_CROSSBOW && heavygear$matches(predicate, Items.CROSSBOW)) {
            cir.setReturnValue(true);
        } else if (self == HeavyGear.HEAVY_FISHING_ROD && heavygear$matches(predicate, Items.FISHING_ROD)) {
            cir.setReturnValue(true);
        }
    }

    private static boolean heavygear$matches(Predicate<Holder<Item>> predicate, Item item) {
        return predicate.test(BuiltInRegistries.ITEM.wrapAsHolder(item));
    }
}
