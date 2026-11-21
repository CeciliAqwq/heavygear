package com.cecilia.heavygear.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

/**
 * Heavy armour piece. Behaves exactly like a plain {@link Item} (all stats come from the armour
 * material in {@link HeavyGear}); the only addition is the grey hint line that explains the
 * meteor-smash bonus shared by the whole set.
 */
public class HeavyArmorItem extends Item {
    public HeavyArmorItem(Properties properties) {
        super(properties);
    }

    /**
     * Declared under its intermediary name: {@code Item#appendHoverText} is still unmapped for
     * {@code Item} itself in the mapping table, so a source-level {@code appendHoverText} would
     * neither compile nor override anything.
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                             Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        tooltip.accept(Component.translatable("tooltip.heavygear.heavy_armor.smash")
                .withStyle(ChatFormatting.GRAY));
    }
}
