package com.cecilia.heavygear.enchant;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class HeavyGearEnchantments {
    public static final Identifier DIVE_BOMBING_ID = Identifier.fromNamespaceAndPath("heavygear", "dive_bombing");

    private HeavyGearEnchantments() {
    }

    public static int level(ItemStack stack, ResourceKey<Enchantment> key) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        ItemEnchantments enchantments = ((DataComponentHolder) stack).get(DataComponents.ENCHANTMENTS);
        if (enchantments == null) {
            enchantments = ItemEnchantments.EMPTY;
        }
        for (Holder<Enchantment> holder : enchantments.keySet()) {
            boolean match = holder.unwrapKey()
                    .map(k -> k.equals(key))
                    .orElse(false);
            if (match) {
                return enchantments.getLevel(holder);
            }
        }
        return 0;
    }

    public static int diveBombingLevel(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        ItemEnchantments enchantments = ((DataComponentHolder) stack).get(DataComponents.ENCHANTMENTS);
        if (enchantments == null) {
            enchantments = ItemEnchantments.EMPTY;
        }
        for (Holder<Enchantment> holder : enchantments.keySet()) {
            boolean match = holder.unwrapKey()
                    .map(key -> key.identifier().equals(DIVE_BOMBING_ID))
                    .orElse(false);
            if (match) {
                return enchantments.getLevel(holder);
            }
        }
        return 0;
    }
}
