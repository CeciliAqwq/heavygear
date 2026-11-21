package com.cecilia.heavygear.client;

import com.cecilia.heavygear.item.HeavyGear;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Shared helper for the crossbow animation hooks.
 *
 * <p>Vanilla asks "is this a crossbow?" with {@code stack.is(Items.CROSSBOW)}. In 26.x that resolves to
 * the {@code TypedInstance#is(T)} interface default (plain item identity), so a look-alike item never
 * answers true and every crossbow-specific rendering branch is skipped - the heavy crossbow just sits
 * in the hand instead of being wound up. The hooks below answer that one question for the heavy
 * crossbow and leave every other item alone.</p>
 *
 * <p>Lives outside {@code com.cecilia.heavygear.mixin} on purpose: Mixin refuses to load ordinary
 * classes from a package declared as a mixin package.</p>
 */
public final class CrossbowEquivalence {
    private CrossbowEquivalence() {
    }

    public static boolean is(ItemStack stack, Object type) {
        Item self = stack.getItem();
        if (self == HeavyGear.HEAVY_CROSSBOW && type == Items.CROSSBOW) {
            return true;
        }
        return self == type;
    }
}